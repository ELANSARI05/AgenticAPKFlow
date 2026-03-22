"""
apk_server.py — FastMCP APK Analysis Server v2.0
=================================================
Exposes all analysis tools from malware_tools.py as MCP tools.

New tools in v2:
  - permission_behavior_correlation : Risk Correlator
  - data_flow_tracing               : Logic Tracer
  - mitre_attack_mapping            : MITRE ATT&CK mapping
  - reasoning_trace                 : ReAct investigation trace
  - generate_json_report            : Structured JSON output
"""

import sys
import os

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
    correlate_permission_behavior,
    trace_data_flows,
    map_mitre_attack,
    build_reasoning_trace,
    generate_json_report,
    generate_security_report,
    run_full_analysis,
    analyze_cape_report,
    generate_hybrid_report,
)

mcp = FastMCP(
    name="APK Security Analyzer",
    instructions=(
        "Tools for static security analysis of Android APK files. "
        "All tools accept an apk_path string and return structured dicts "
        "with risk levels (HIGH/MEDIUM/LOW/INFO) and a summary field. "
        "Call generate_full_report last to persist all findings."
    ),
)


@mcp.tool()
def apk_metadata(apk_path: str) -> dict:
    """Extract APK metadata: package name, version, SDK targets, debuggable flag, backup flag."""
    return get_apk_metadata(apk_path)


@mcp.tool()
def permissions_risk_profile(apk_path: str) -> dict:
    """Return all declared permissions with risk ratings (HIGH/MEDIUM/LOW/INFO)."""
    return extract_permissions_with_risk(apk_path)


@mcp.tool()
def hardcoded_secrets_scan(apk_path: str) -> dict:
    """Scan decompiled source for hardcoded secrets: crypto keys, API tokens, private keys."""
    return find_hardcoded_secrets(apk_path)


@mcp.tool()
def network_behavior_analysis(apk_path: str) -> dict:
    """Extract network indicators: URLs, domains, hardcoded IPs, C2 candidates."""
    return analyze_network_behavior(apk_path)


@mcp.tool()
def crypto_usage_analysis(apk_path: str) -> dict:
    """Identify crypto operations, hardcoded key material, weak modes (ECB), native crypto."""
    return extract_crypto_usage(apk_path)


@mcp.tool()
def obfuscation_detection(apk_path: str) -> dict:
    """Detect obfuscation: ProGuard/R8, reflection, dynamic loading, native bridges."""
    return detect_obfuscation_techniques(apk_path)


@mcp.tool()
def anti_analysis_detection(apk_path: str) -> dict:
    """Detect anti-analysis: debugger/root/emulator detection, cert pinning, Frida detection."""
    return check_anti_analysis(apk_path)


@mcp.tool()
def permission_behavior_correlation(apk_path: str) -> dict:
    """
    Risk Correlator — matches declared permissions against API usage to identify
    dangerous combinations and form threat hypotheses (e.g., READ_SMS + INTERNET
    → SMS exfiltration). Returns confirmed vs suspected threats with MITRE IDs.
    """
    return correlate_permission_behavior(apk_path)


@mcp.tool()
def data_flow_tracing(apk_path: str) -> dict:
    """
    Logic Tracer — tracks data flows from sensitive sources (SMS, Location,
    Contacts, IMEI, Audio, Camera) to network/storage sinks.
    Identifies potential exfiltration paths.
    """
    return trace_data_flows(apk_path)


@mcp.tool()
def mitre_attack_mapping(apk_path: str) -> dict:
    """
    Maps detected behaviors to MITRE ATT&CK Mobile framework techniques.
    Returns matched techniques with tactic, evidence, and confidence level.
    """
    return map_mitre_attack(apk_path)


@mcp.tool()
def get_reasoning_trace(apk_path: str) -> dict:
    """
    Builds a ReAct-style reasoning trace: Observe → Hypothesize → Investigate
    → Validate → Conclude. Returns the full investigation chain with evidence
    and final verdict. This is the transparent reasoning trace from the project plan.
    """
    return build_reasoning_trace(apk_path)


@mcp.tool()
def generate_full_report(
    apk_path: str,
    output_path: str = "/app/data/SECURITY_REPORT.md",
) -> str:
    """
    Run all analysis modules and write a complete Markdown + JSON security report.
    Call this as the FINAL step. Returns the saved file path on success.
    """
    return generate_security_report(apk_path, output_path)




@mcp.tool()
def cape_dynamic_analysis(report_path: str) -> dict:
    """
    Parse a CAPEv2 JSON sandbox report and extract behavioral findings:
    suspicious API calls, runtime network connections, dropped files,
    registry persistence, process injection, and MITRE ATT&CK mapping.
    Provide the path to a local CAPEv2 report.json file.
    """
    return analyze_cape_report(report_path)


@mcp.tool()
def hybrid_static_dynamic_report(
    apk_path: str,
    cape_report_path: str,
    output_path: str = "/app/data/HYBRID_REPORT.md",
) -> str:
    """
    Generate a combined static + dynamic analysis report.
    Merges your static APK findings with CAPEv2 sandbox behavioral results
    into a single comprehensive Markdown report with unified MITRE ATT&CK mapping.
    Call this as the FINAL step when a CAPEv2 report is available.
    """
    return generate_hybrid_report(apk_path, cape_report_path, output_path)

if __name__ == "__main__":
    mcp.run()