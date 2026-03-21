"""
main.py — Multi-Agent APK Analysis Orchestrator
================================================
Architecture:
  ┌─────────────────────────────────────────────────────┐
  │  FastMCP Server  (src/mcp/apk_server.py)            │
  │  8 high-level tools exposed over stdio MCP          │
  └───────────────────┬────────────────────────────────┘
                      │ MCPTools (shared connection)
          ┌───────────┴───────────┐
          ▼                       ▼
  ┌───────────────┐       ┌───────────────────┐
  │  Scout Agent  │──────▶│ Specialist Agent  │
  │  (4 tools)    │ brief │  (3 tools + save) │
  └───────────────┘       └───────────────────┘

Usage:
    python src/agent/main.py /app/data/your_app.apk
    python src/agent/main.py                          # defaults to test_app.apk
"""

import os
import sys
import asyncio

from agno.agent import Agent
from agno.models.google import Gemini
from agno.tools.mcp import MCPTools

# ── Environment check ─────────────────────────────────────────────────────────
api_key = os.getenv("GOOGLE_API_KEY")
if not api_key:
    print("❌  GOOGLE_API_KEY environment variable not set.")
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
    """
    agent.aprint_response() with exponential backoff on 429.
    Streaming 429s surface as exceptions after the stream starts,
    so we disable streaming on retry to get a cleaner error boundary.
    """
    for attempt in range(6):
        try:
            # First attempt uses caller's stream preference;
            # retries fall back to non-streaming to avoid partial output issues
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

def _gemini() -> Gemini:
    return Gemini(id="gemini-2.5-flash", api_key=api_key)


# ── Agent factories ───────────────────────────────────────────────────────────

def _make_scout(mcp_tools: MCPTools) -> Agent:
    return Agent(
        name="Scout",
        model=_gemini(),
        tools=[mcp_tools],
        description="""You are the Scout Agent.
Your job is surface-level reconnaissance of an Android APK.

STRICT TASK LIST — follow in order, no deviations:
  Step 1: Call `apk_metadata`             → get package info and security flags
  Step 2: Call `permissions_risk_profile` → assess declared permissions
  Step 3: Call `obfuscation_detection`    → check for code obfuscation
  Step 4: Call `anti_analysis_detection`  → detect anti-debug/root/emulator tricks

After Step 4, write a structured Scout Brief containing:
  - Package name, version, debuggable flag, allow-backup flag
  - All HIGH-risk permissions (list their names and reasons)
  - Obfuscation level and techniques detected
  - Anti-analysis techniques found with bypass hints

RULES:
  ✅ Call each tool EXACTLY ONCE
  ✅ Write the Scout Brief after all 4 calls complete
  ❌ Do NOT call any tool not listed above
  ❌ Do NOT analyze code or read files
  ❌ Do NOT loop or re-check results
  STOP after writing the Scout Brief.""",
        markdown=True,
        debug_mode=False,
    )


def _make_specialist(mcp_tools: MCPTools) -> Agent:
    return Agent(
        name="Specialist",
        model=_gemini(),
        tools=[mcp_tools],
        description="""You are the Specialist Agent.
You perform deep static code analysis of an Android APK.
You will receive a Scout Brief summarizing surface-level findings.

STRICT TASK LIST — follow in order, no deviations:
  Step 1: Call `hardcoded_secrets_scan`    → find API keys, crypto keys, tokens
  Step 2: Call `crypto_usage_analysis`     → identify cipher algorithms and key material
  Step 3: Call `network_behavior_analysis` → map URLs, domains, hardcoded IPs
  Step 4: Call `generate_full_report`      → persist the complete security report

RULES:
  ✅ Call each tool EXACTLY ONCE
  ✅ After `generate_full_report` returns, report the saved path to the user
  ❌ Do NOT call `apk_metadata`, `permissions_risk_profile`, `obfuscation_detection`,
     or `anti_analysis_detection` — the Scout already covered those
  ❌ Do NOT loop or revisit any tool
  ❌ Do NOT read individual source files
  STOP immediately after `generate_full_report` completes.""",
        markdown=True,
        debug_mode=False,
    )


# ── Emergency report (when Specialist quota fails entirely) ───────────────────

def _write_emergency_report(apk_path: str, scout_brief: str) -> None:
    """
    If the Specialist is blocked by quota, save whatever we have from the Scout
    so the run is never a complete loss.
    """
    report_path = "/app/data/SECURITY_REPORT.md"
    os.makedirs("/app/data", exist_ok=True)
    content = f"""# APK Security Analysis Report (Partial — Scout Only)

> ⚠️ Specialist phase was skipped due to API quota limits.
> Re-run when quota resets to complete the deep analysis.

**Target:** `{apk_path}`

---

## Scout Findings

{scout_brief}

---

*Partial report saved by emergency fallback.*
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
    print(f"  Agentic-APK Analysis System")
    print(f"  Target : {apk_path}")
    print(f"{'═' * 60}\n")

    async with MCPTools(MCP_SERVER) as mcp_tools:

        # ── Phase 1 : Scout ──────────────────────────────────────────────────
        print("▶  PHASE 1 — Scout Agent\n")
        scout = _make_scout(mcp_tools)
        scout_brief = ""

        try:
            scout_result = await safe_arun(
                scout,
                f"Analyse this APK: {apk_path}\n"
                "Run your 4 tools in order and produce the Scout Brief.",
                stream=False,
                show_tool_calls=True,
            )
            scout_brief = (
                scout_result.content
                if hasattr(scout_result, "content")
                else str(scout_result)
            )
        except RuntimeError as e:
            print(f"❌ Scout failed after all retries: {e}")
            scout_brief = "Scout phase failed — quota exhausted."

        print(f"\n{'─' * 60}")
        print("  SCOUT BRIEF")
        print(f"{'─' * 60}")
        print(scout_brief)
        print(f"{'─' * 60}\n")

        # ── Phase 2 : Specialist ─────────────────────────────────────────────
        print("▶  PHASE 2 — Specialist Agent\n")
        specialist = _make_specialist(mcp_tools)

        try:
            await safe_aprint(
                specialist,
                f"APK path: {apk_path}\n\n"
                f"=== SCOUT BRIEF ===\n{scout_brief}\n=== END BRIEF ===\n\n"
                "Now run your 4 steps: secrets scan → crypto analysis → "
                "network analysis → generate full report.",
                stream=True,
                show_tool_calls=True,
            )
        except RuntimeError as e:
            print(f"❌ Specialist failed after all retries: {e}")
            print("📝 Saving partial report from Scout findings...")
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