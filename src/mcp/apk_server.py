"""
apk_server.py — FastMCP APK Analysis Server
=============================================
Exposes every high-level analysis function from malware_tools.py
as an MCP tool.  The Agno agents connect to this server at runtime.

Usage (standalone test):
    python src/mcp/apk_server.py

Usage via Agno MCPTools (see main.py):
    MCPTools("python src/mcp/apk_server.py")
"""

import sys
import os

# Make sibling packages importable when run from repo root
sys.path.append(os.path.join(os.path.dirname(__file__), ".."))

from fastmcp import FastMCP
from tools.malware_tools import (
    get_apk_metadata,
    extract_permissions_with_risk,
    find_hardcoded_secrets,
    analyze_network_behavior,
    extract_crypto_usage,
    detect_obfuscation_techniques,
    check_anti_analysis,
    generate_security_report,
)

# ── Server definition ─────────────────────────────────────────────────────────
mcp = FastMCP(
    name="APK Security Analyzer",
    instructions=(
        "Tools for static security analysis of Android APK files. "
        "All tools accept an apk_path string and return structured JSON dicts "
        "with risk levels (HIGH / MEDIUM / LOW / INFO) and a summary field. "
        "Call generate_full_report last to persist all findings."
    ),
)


# ── Tool registrations ────────────────────────────────────────────────────────

@mcp.tool()
def apk_metadata(apk_path: str) -> dict:
    """
    Extract high-level APK metadata: package name, version, SDK targets,
    debuggable flag, backup flag, and manifest-level risk flags.
    Use this first to get an overview of the application.
    """
    return get_apk_metadata(apk_path)


@mcp.tool()
def permissions_risk_profile(apk_path: str) -> dict:
    """
    Return all declared permissions with a risk rating (HIGH / MEDIUM / LOW / INFO)
    and a human-readable reason for each.
    Provides an overall_risk field for quick triage.
    """
    return extract_permissions_with_risk(apk_path)


@mcp.tool()
def hardcoded_secrets_scan(apk_path: str) -> dict:
    """
    Scan decompiled source for hardcoded secrets: crypto keys (byte arrays,
    hex strings), API tokens, Base64-encoded blobs, embedded private keys,
    and credential URLs.
    Returns file/line/context for every finding.
    """
    return find_hardcoded_secrets(apk_path)


@mcp.tool()
def network_behavior_analysis(apk_path: str) -> dict:
    """
    Extract all network indicators: URLs, unique domains, hardcoded public IPs,
    cleartext HTTP endpoints, and dynamic-DNS domains (C2 indicators).
    """
    return analyze_network_behavior(apk_path)


@mcp.tool()
def crypto_usage_analysis(apk_path: str) -> dict:
    """
    Identify cryptographic operations (cipher algorithms, message digests, MACs),
    extract hardcoded key material (byte arrays, variable sources), detect
    hardcoded IVs, and flag weak patterns like ECB mode.
    """
    return extract_crypto_usage(apk_path)


@mcp.tool()
def obfuscation_detection(apk_path: str) -> dict:
    """
    Detect code obfuscation: ProGuard/R8 name mangling ratio, Java reflection,
    dynamic class loading (DexClassLoader), and native code bridges.
    Returns an obfuscation_level (NONE / LIGHT / MODERATE / HEAVY) and score.
    """
    return detect_obfuscation_techniques(apk_path)


@mcp.tool()
def anti_analysis_detection(apk_path: str) -> dict:
    """
    Detect anti-analysis defenses: debugger detection (isDebuggerConnected),
    root detection, emulator detection, certificate pinning, and APK integrity
    checks. Each finding includes a concrete bypass hint.
    """
    return check_anti_analysis(apk_path)


@mcp.tool()
def generate_full_report(
    apk_path: str,
    output_path: str = "/app/data/SECURITY_REPORT.md",
) -> str:
    """
    Run all analysis modules and write a complete Markdown security report.
    Call this as the FINAL step after reviewing individual tool results.
    Returns the saved file path on success.
    """
    return generate_security_report(apk_path, output_path)


# ── Entry point ───────────────────────────────────────────────────────────────
if __name__ == "__main__":
    # Runs the MCP server over stdio (default transport for Agno MCPTools)
    mcp.run()