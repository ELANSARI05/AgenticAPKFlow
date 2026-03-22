"""
main.py — Multi-Agent APK Analysis Orchestrator v3.1
=====================================================
Key improvements over v3.0:
  - Pre-caches all analysis results BEFORE agents run
  - generate_full_report reads from cache (no timeout)
  - Agent reasoning captured to file and injected into report Section 1
  - Scout uses llama-3.1-8b-instant (fast, cheap)
  - Specialist uses llama-3.3-70b-versatile (powerful)
  - output_path locked to /app/data/SECURITY_REPORT.md (no hallucination)
"""

import os
import sys
import asyncio

from agno.agent import Agent
from agno.models.openai.like import OpenAILike
from agno.tools.mcp import MCPTools

# ── Environment check ─────────────────────────────────────────────────────────
api_key = (os.getenv("GROQ_API_KEY") or
           os.getenv("OPENROUTER_API_KEY") or
           os.getenv("GOOGLE_API_KEY"))
if not api_key:
    print("❌  No API key found. Set GROQ_API_KEY, OPENROUTER_API_KEY or GOOGLE_API_KEY.")
    sys.exit(1)

MCP_SERVER      = "python src/mcp/apk_server.py"
REASONING_FILE  = "/app/data/agent_reasoning.md"
REPORT_PATH     = "/app/data/SECURITY_REPORT.md"


# ── Rate-limit retry wrappers ─────────────────────────────────────────────────

def _is_rate_limit(e: Exception) -> bool:
    return "429" in str(e) or "RESOURCE_EXHAUSTED" in str(e)


async def safe_arun(agent: Agent, message: str, **kwargs):
    for attempt in range(6):
        try:
            return await agent.arun(message, **kwargs)
        except Exception as e:
            if _is_rate_limit(e):
                wait = 30 * (attempt + 1)
                print(f"⏳ Rate limited. Waiting {wait}s (attempt {attempt + 1}/6)...")
                await asyncio.sleep(wait)
            else:
                raise
    raise RuntimeError("❌ Max retries exceeded.")


async def safe_aprint(agent: Agent, message: str, **kwargs):
    for attempt in range(6):
        try:
            attempt_kwargs = kwargs.copy()
            if attempt > 0:
                attempt_kwargs["stream"] = False
            return await agent.aprint_response(message, **attempt_kwargs)
        except Exception as e:
            if _is_rate_limit(e):
                wait = 30 * (attempt + 1)
                print(f"⏳ Rate limited. Waiting {wait}s (attempt {attempt + 1}/6)...")
                await asyncio.sleep(wait)
            else:
                raise
    raise RuntimeError("❌ Max retries exceeded.")


# ── Model factories ───────────────────────────────────────────────────────────

def _scout_model() -> OpenAILike:
    """Small fast model for surface analysis."""
    if os.getenv("GROQ_API_KEY"):
        return OpenAILike(
            id="llama-3.1-8b-instant",
            api_key=os.getenv("GROQ_API_KEY"),
            base_url="https://api.groq.com/openai/v1",
        )
    return OpenAILike(
        id="meta-llama/llama-3.3-70b-instruct:free",
        api_key=api_key,
        base_url="https://openrouter.ai/api/v1",
    )


def _specialist_model() -> OpenAILike:
    """Full-power model for deep analysis."""
    if os.getenv("GROQ_API_KEY"):
        return OpenAILike(
            id="llama-3.3-70b-versatile",
            api_key=os.getenv("GROQ_API_KEY"),
            base_url="https://api.groq.com/openai/v1",
        )
    return OpenAILike(
        id="meta-llama/llama-3.3-70b-instruct:free",
        api_key=api_key,
        base_url="https://openrouter.ai/api/v1",
    )


# ── Agent factories ───────────────────────────────────────────────────────────

def _make_scout(mcp_tools: MCPTools) -> Agent:
    return Agent(
        name="Scout",
        model=_scout_model(),
        tools=[mcp_tools],
        tool_call_limit=6,
        description="""You are an expert Android malware analyst performing
surface-level reconnaissance on an APK file.

You have access to these tools:
  - apk_metadata                   → package info, SDK, debuggable flag
  - permissions_risk_profile       → declared permissions with risk ratings
  - permission_behavior_correlation → dangerous permission combos + threat hypotheses
  - obfuscation_detection          → ProGuard, reflection, native bridges
  - anti_analysis_detection        → debugger/root/emulator/Frida detection

INVESTIGATION APPROACH — think and reason at every step:

1. Always start with apk_metadata — it tells you what kind of app this is
   and whether it has obvious security misconfigurations (debuggable, low SDK).

2. After metadata, reason: does this app look suspicious so far?
   - If debuggable=true → prioritize anti_analysis_detection next
   - Always run permissions_risk_profile to see what the app declared

3. After permissions, reason: what threats are plausible?
   - If you see SMS + INTERNET → run permission_behavior_correlation
   - Always run correlation — you might find combos you didn't notice

4. Always run obfuscation_detection AND anti_analysis_detection regardless
   of permission findings — obfuscated apps often hide defenses in code,
   not in the manifest.

After all relevant tools, write a Scout Brief with:
  - Package info and security flags
  - Permission risk summary
  - Threat hypotheses (confirmed or suspected)
  - Obfuscation level and what it implies
  - Anti-analysis defenses and bypass hints
  - Your overall suspicion level and why

GUARDRAILS:
  - Do not call the same tool twice
  - Maximum 6 tool calls total
  - Stop and write the Scout Brief once you have enough information""",
        markdown=True,
        debug_mode=False,
    )


def _make_specialist(mcp_tools: MCPTools) -> Agent:
    return Agent(
        name="Specialist",
        model=_specialist_model(),
        tools=[mcp_tools],
        tool_call_limit=8,
        description="""You are a senior malware reverse engineer performing
deep static code analysis on an Android APK.

You will receive a Scout Brief summarizing surface findings.
Your job is to investigate the specific threats the Scout identified.

You have access to these tools:
  - hardcoded_secrets_scan       → API keys, crypto keys, tokens in source
  - crypto_usage_analysis        → cipher algorithms, key material, native crypto
  - network_behavior_analysis    → URLs, domains, hardcoded IPs, C2 candidates
  - data_flow_tracing            → sensitive data → network/storage sink paths
  - mitre_attack_mapping         → MITRE ATT&CK technique classification
  - get_reasoning_trace          → builds full ReAct investigation chain
  - cape_dynamic_analysis        → parse CAPEv2 sandbox report for behavioral IOCs
  - hybrid_static_dynamic_report → combine static + dynamic findings into one report
  - generate_full_report         → saves complete Markdown + JSON report

INVESTIGATION APPROACH — driven by Scout findings:

1. Read the Scout Brief carefully. Identify the key hypotheses.

2. For each threat hypothesis from the Scout:
   - "Crypto suspected" → run hardcoded_secrets_scan first, then
     crypto_usage_analysis to understand key management
   - "Network exfiltration" → run network_behavior_analysis, then
     data_flow_tracing to trace what data reaches those endpoints
   - "Heavy obfuscation" → note this limits static analysis, mention
     in report that dynamic analysis (Frida) is recommended
   - "No permissions / clean surface" → still run secrets scan —
     apps can be malicious without dangerous permissions

3. After each tool result, think:
   - What did I just find?
   - Does this confirm or refute the Scout's hypothesis?
   - What should I investigate next based on this finding?
   - Do I have enough evidence to conclude?

4. If a CAPEv2 report is provided (path ending in .json in /app/data/):
   - Call cape_dynamic_analysis with the report path
   - If both static + dynamic are done, call hybrid_static_dynamic_report instead
     of generate_full_report for the most complete analysis

5. When investigation is complete:
   - Call mitre_attack_mapping to classify all behaviors
   - Call get_reasoning_trace to build the formal investigation chain
   - Call generate_full_report LAST — use ONLY these exact arguments:
     apk_path=<the APK path>, output_path=/app/data/SECURITY_REPORT.md

Example reasoning chain:
  Scout said: "AES encryption detected, key origin unknown"
  → I will run hardcoded_secrets_scan to find the key
  → Found hex string 8d127684... passed to SecretKeySpec
  → Running crypto_usage_analysis confirms AES/ECB mode — weak
  → No network traffic found → encryption is for local obfuscation
  → Conclusion: app encrypts its secret check locally, not exfiltrating

GUARDRAILS:
  - Do not call the same tool twice
  - Do not call generate_full_report until you have run at least 3 tools
  - Maximum 8 tool calls total
  - Always call get_reasoning_trace before generate_full_report
  - generate_full_report output_path MUST be /app/data/SECURITY_REPORT.md""",
        markdown=True,
        debug_mode=False,
    )


# ── Reasoning capture ─────────────────────────────────────────────────────────

def _save_reasoning(scout_brief: str, specialist_output: str) -> None:
    """Saves both agents' reasoning to file for injection into the report."""
    os.makedirs("/app/data", exist_ok=True)
    content = f"""## 🔍 Scout Agent Reasoning

{scout_brief}

---

## 🔬 Specialist Agent Reasoning

{specialist_output}
"""
    with open(REASONING_FILE, "w") as f:
        f.write(content)


# ── Emergency fallback ────────────────────────────────────────────────────────

def _write_emergency_report(apk_path: str, scout_brief: str) -> None:
    os.makedirs("/app/data", exist_ok=True)
    content = f"""# APK Security Analysis Report (Partial — Scout Only)

> ⚠️ Specialist phase skipped due to API quota limits.
> Re-run when quota resets to complete the deep analysis.

**Target:** `{apk_path}`

---

## Scout Findings

{scout_brief}

---
*Partial report — emergency fallback*
"""
    with open(REPORT_PATH, "w") as f:
        f.write(content)
    print(f"⚠️  Partial report saved → {REPORT_PATH}")


# ── Pre-cache analysis ────────────────────────────────────────────────────────

def _precache_analysis(apk_path: str) -> None:
    """
    Runs all analysis tools ONCE before agents start.
    This means generate_full_report just reads from cache — no timeout.
    """
    print("📊 Pre-caching analysis results (this runs all tools once)...")
    sys.path.append(os.path.join(os.path.dirname(__file__), ".."))
    from tools.malware_tools import run_full_analysis
    run_full_analysis(apk_path)
    print("✅ Analysis cache ready\n")


# ── Main orchestration ────────────────────────────────────────────────────────

async def run_analysis(apk_path: str) -> None:
    if not os.path.exists(apk_path):
        print(f"❌  APK not found: {apk_path}")
        sys.exit(1)

    print(f"\n{'═' * 60}")
    print(f"  Agentic-APK Analysis System v3.1")
    print(f"  Target : {apk_path}")
    print(f"{'═' * 60}\n")

    # Pre-cache all analysis results synchronously before agents start
    # This makes generate_full_report instant (reads cache, no re-analysis)
    _precache_analysis(apk_path)

    async with MCPTools(MCP_SERVER, timeout_seconds=120) as mcp_tools:

        # ── Phase 1 : Scout ──────────────────────────────────────────────────
        print("▶  PHASE 1 — Scout Agent (ReAct mode)\n")
        scout = _make_scout(mcp_tools)
        scout_brief = ""

        try:
            scout_result = await safe_arun(
                scout,
                f"Investigate this APK: {apk_path}\n\n"
                "Start with apk_metadata, then reason about what you find "
                "and decide which tools to use next. "
                "Think step by step. Produce a Scout Brief when done.",
                stream=False,
                show_tool_calls=True,
            )
            scout_brief = (
                scout_result.content
                if hasattr(scout_result, "content")
                else str(scout_result)
            )
        except RuntimeError as e:
            print(f"❌ Scout failed: {e}")
            scout_brief = "Scout phase failed — quota exhausted."

        print(f"\n{'─' * 60}")
        print("  SCOUT BRIEF")
        print(f"{'─' * 60}")
        print(scout_brief)
        print(f"{'─' * 60}\n")

        # Wait between phases to respect per-minute quota
        print("⏳ Waiting 60s between phases to respect rate limits...")
        await asyncio.sleep(60)

        # ── Phase 2 : Specialist ─────────────────────────────────────────────
        print("▶  PHASE 2 — Specialist Agent (ReAct mode)\n")
        specialist = _make_specialist(mcp_tools)
        specialist_output = ""

        try:
            spec_result = await safe_arun(
                specialist,
                f"APK path: {apk_path}\n\n"
                f"=== SCOUT BRIEF ===\n{scout_brief}\n=== END BRIEF ===\n\n"
                "Read the Scout Brief carefully. Investigate the threat hypotheses "
                "using the most relevant tools. Think out loud after each result. "
                "When done, call get_reasoning_trace then call generate_full_report "
                f"with apk_path={apk_path} and output_path=/app/data/SECURITY_REPORT.md",
                stream=False,
                show_tool_calls=True,
            )
            specialist_output = (
                spec_result.content
                if hasattr(spec_result, "content")
                else str(spec_result)
            )
            print(specialist_output)
        except RuntimeError as e:
            print(f"❌ Specialist failed: {e}")
            _write_emergency_report(apk_path, scout_brief)

        # Save both agents' reasoning for report injection
        _save_reasoning(scout_brief, specialist_output)

    if os.path.exists(REPORT_PATH):
        print(f"\n{'═' * 60}")
        print(f"  ✅ Analysis complete → {REPORT_PATH}")
        print(f"{'═' * 60}\n")
    else:
        # If report wasn't generated (specialist failed), generate from cache
        print("⚠️  Report not generated by agent — generating from cache...")
        sys.path.append(os.path.join(os.path.dirname(__file__), ".."))
        from tools.malware_tools import generate_security_report
        result = generate_security_report(apk_path, REPORT_PATH)
        print(f"  {result}")


# ── Entry point ───────────────────────────────────────────────────────────────
if __name__ == "__main__":
    target_apk = sys.argv[1] if len(sys.argv) > 1 else "/app/data/test_app.apk"
    asyncio.run(run_analysis(target_apk))