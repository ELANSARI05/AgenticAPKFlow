"""
main.py — Multi-Agent APK Analysis Orchestrator v3.0
=====================================================
Architecture:
  ┌─────────────────────────────────────────────────────────┐
  │  FastMCP Server  (src/mcp/apk_server.py)                │
  │  13 high-level tools exposed over stdio MCP             │
  └───────────────────┬─────────────────────────────────────┘
                      │ MCPTools (shared connection)
          ┌───────────┴───────────┐
          ▼                       ▼
  ┌───────────────┐       ┌─────────────────────┐
  │  Scout Agent  │──────▶│  Specialist Agent   │
  │  (5 tools)    │ brief │  (6 tools + report) │
  └───────────────┘       └─────────────────────┘

True ReAct architecture — agents autonomously decide which tools
to call based on intermediate findings, not a hardcoded task list.

Scout (surface):
  apk_metadata, permissions_risk_profile,
  obfuscation_detection, anti_analysis_detection,
  permission_behavior_correlation

Specialist (deep):
  hardcoded_secrets_scan, crypto_usage_analysis,
  network_behavior_analysis, data_flow_tracing,
  mitre_attack_mapping, get_reasoning_trace,
  generate_full_report

Usage:
    python src/agent/main.py /app/data/your_app.apk
    python src/agent/main.py                          # defaults to test_app.apk
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

MCP_SERVER = "python src/mcp/apk_server.py"


# ── Rate-limit retry wrappers ─────────────────────────────────────────────────

def _is_rate_limit(e: Exception) -> bool:
    return "429" in str(e) or "RESOURCE_EXHAUSTED" in str(e)


async def safe_arun(agent: Agent, message: str, **kwargs):
    """agent.arun() with exponential backoff on 429."""
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
    """agent.aprint_response() with exponential backoff on 429."""
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


# ── Model factory ─────────────────────────────────────────────────────────────

def _get_model() -> OpenAILike:
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
        model=_get_model(),
        tools=[mcp_tools],
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
   - If debuggable=true → this is highly unusual for a release app, prioritize
     anti_analysis_detection next to understand its defenses
   - Always run permissions_risk_profile to see what the app declared

3. After permissions, reason: what threats are plausible?
   - If you see SMS + INTERNET → run permission_behavior_correlation
     to confirm exfiltration hypothesis
   - If permissions look clean → still run correlation, you might find
     dangerous combos you didn't notice

4. Always run obfuscation_detection AND anti_analysis_detection regardless
   of permission findings — obfuscated apps often hide defenses in code,
   not in the manifest.

5. Run anti_analysis_detection — presence of root/debugger checks suggests
   the app is aware of being analyzed

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
        debug_mode=True,
    )


def _make_specialist(mcp_tools: MCPTools) -> Agent:
    return Agent(
        name="Specialist",
        model=_get_model(),
        tools=[mcp_tools],
        description="""You are a senior malware reverse engineer performing
deep static code analysis on an Android APK.

You will receive a Scout Brief summarizing surface findings.
Your job is to investigate the specific threats the Scout identified.

You have access to these tools:
  - hardcoded_secrets_scan    → API keys, crypto keys, tokens in source
  - crypto_usage_analysis     → cipher algorithms, key material, native crypto
  - network_behavior_analysis → URLs, domains, hardcoded IPs, C2 candidates
  - data_flow_tracing         → sensitive data → network/storage sink paths
  - mitre_attack_mapping      → MITRE ATT&CK technique classification
  - get_reasoning_trace       → builds full ReAct investigation chain
  - generate_full_report      → saves complete Markdown + JSON report

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

4. When investigation is complete:
   - Call mitre_attack_mapping to classify all behaviors
   - Call get_reasoning_trace to build the formal investigation chain
   - Call generate_full_report LAST to save everything

Example reasoning chain:
  Scout said: "AES encryption detected, key origin unknown"
  → I will run hardcoded_secrets_scan to find the key
  → Found hex string 8d127684... passed to SecretKeySpec
  → Running crypto_usage_analysis confirms AES/ECB mode — weak
  → No network traffic found → encryption is for local obfuscation
  → Conclusion: app encrypts its secret check locally, not exfiltrating

GUARDRAILS:
  - Do not call the same tool twice
  - Do not call generate_full_report until you have run at least
    3 investigation tools
  - Maximum 8 tool calls total
  - Always call get_reasoning_trace before generate_full_report""",
        markdown=True,
        debug_mode=True,
    )


# ── Emergency fallback report ─────────────────────────────────────────────────

def _write_emergency_report(apk_path: str, scout_brief: str) -> None:
    report_path = "/app/data/SECURITY_REPORT.md"
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
    with open(report_path, "w") as f:
        f.write(content)
    print(f"⚠️  Partial report saved → {report_path}")


# ── Main orchestration ────────────────────────────────────────────────────────

async def run_analysis(apk_path: str) -> None:
    if not os.path.exists(apk_path):
        print(f"❌  APK not found: {apk_path}")
        sys.exit(1)

    print(f"\n{'═' * 60}")
    print(f"  Agentic-APK Analysis System v3.0")
    print(f"  Target : {apk_path}")
    print(f"{'═' * 60}\n")

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

        try:
            await safe_aprint(
                specialist,
                f"APK path: {apk_path}\n\n"
                f"=== SCOUT BRIEF ===\n{scout_brief}\n=== END BRIEF ===\n\n"
                "Read the Scout Brief carefully. Identify the threat hypotheses "
                "and investigate each one using the most relevant tools. "
                "Think out loud after each tool result. "
                "When investigation is complete, call get_reasoning_trace "
                "then generate_full_report.",
                stream=True,
                show_tool_calls=True,
            )
        except RuntimeError as e:
            print(f"❌ Specialist failed: {e}")
            _write_emergency_report(apk_path, scout_brief)

    report_path = "/app/data/SECURITY_REPORT.md"
    if os.path.exists(report_path):
        print(f"\n{'═' * 60}")
        print(f"  ✅ Analysis complete → {report_path}")
        print(f"{'═' * 60}\n")
    else:
        print(f"\n{'═' * 60}")
        print("  ⚠️  No report generated — check errors above.")
        print(f"{'═' * 60}\n")


# ── Entry point ───────────────────────────────────────────────────────────────
if __name__ == "__main__":
    target_apk = sys.argv[1] if len(sys.argv) > 1 else "/app/data/test_app.apk"
    asyncio.run(run_analysis(target_apk))