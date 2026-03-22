# 🔍 AgenticAPKFlow
### AI-Powered Agentic Workflow for Automated Android APK Security Analysis

> **Course Project** — Malware Analysis | Agno + FastMCP + LLaMA 3.3

---

## Overview

AgenticAPKFlow is a multi-agent AI system that autonomously analyzes Android APK files for security threats. Unlike traditional scanners that run static rule checks on every file, this system uses **LLM-based ReAct reasoning** to investigate selectively — forming hypotheses, invoking specialized tools, and validating findings like a human security analyst.

**Example:** When an app declares `READ_SMS` and `INTERNET` permissions, the Scout agent hypothesizes data exfiltration, then directs the Specialist to trace SMS access patterns to network sinks — confirming or refuting the threat with evidence.

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│  FastMCP Server  (src/mcp/apk_server.py)                │
│  15 high-level analysis tools over stdio MCP            │
└───────────────────┬─────────────────────────────────────┘
                    │ MCPTools (shared connection)
        ┌───────────┴───────────┐
        ▼                       ▼
┌───────────────┐       ┌─────────────────────┐
│  Scout Agent  │──────▶│  Specialist Agent   │
│  5 tools      │ brief │  7 tools + report   │
└───────────────┘       └─────────────────────┘
```

### ReAct Investigation Loop

```
OBSERVE → HYPOTHESIZE → INVESTIGATE → VALIDATE → CONCLUDE
```

The Scout performs surface reconnaissance (metadata, permissions, obfuscation, anti-analysis). It passes a structured brief to the Specialist, which performs deep code analysis driven by the Scout's hypotheses — not a hardcoded task list.

---

## Features

### Analysis Capabilities

| Tool | Description |
|------|-------------|
| `apk_metadata` | Package info, SDK targets, debuggable/backup flags |
| `permissions_risk_profile` | 25-entry risk database, HIGH/MEDIUM/LOW per permission |
| `permission_behavior_correlation` | Risk Correlator — matches permissions to API usage, confirms/refutes threat hypotheses |
| `find_hardcoded_secrets` | 10 pattern types: crypto keys, API tokens, Base64 blobs, private keys |
| `analyze_network_behavior` | URLs, domains, hardcoded IPs, C2 detection, whitelist for false positives |
| `extract_crypto_usage` | Cipher detection, hardcoded key material, ECB mode, native crypto warning |
| `detect_obfuscation_techniques` | ProGuard/R8, reflection, DexClassLoader, native bridges |
| `check_anti_analysis` | Debugger/root/emulator/Frida detection with bypass hints |
| `trace_data_flows` | Logic Tracer — SMS/Location/Contacts → network/storage sink paths |
| `mitre_attack_mapping` | Maps findings to 15 MITRE ATT&CK Mobile techniques |
| `build_reasoning_trace` | ReAct investigation chain with evidence and verdict |
| `generate_full_report` | Full Markdown + JSON report from cached results |
| `cape_dynamic_analysis` | **[NEW]** Parse CAPEv2 sandbox JSON report — extracts runtime API calls, network IOCs, dropped files, registry persistence, process injection |
| `hybrid_static_dynamic_report` | **[NEW]** Combines static APK analysis + CAPEv2 dynamic findings into unified report with merged MITRE ATT&CK mapping |

### Output

- **Markdown report** (`SECURITY_REPORT.md`) — 11-section human-readable report
- **JSON report** (`SECURITY_REPORT.json`) — machine-readable structured output
- **Agent reasoning** (`agent_reasoning.md`) — captured LLM investigation chain

---

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Agent Framework | [Agno](https://github.com/agno-agi/agno) |
| MCP Server | [FastMCP](https://github.com/jlowin/fastmcp) |
| LLM | Groq — LLaMA 3.3 70B (Scout: LLaMA 3.1 8B) |
| Decompiler | JADX 1.5.0 |
| Resource Extractor | Apktool 2.9.3 |
| Containerization | Docker + Docker Compose |
| Language | Python 3.11 |

---

## Project Structure

```
AgenticAPKFlow/
├── src/
│   ├── agent/
│   │   ├── main.py          # Multi-agent orchestrator (Scout + Specialist)
│   │   └── direct_run.py    # Direct runner (no LLM, uses cache)
│   ├── mcp/
│   │   └── apk_server.py    # FastMCP server — 13 tools over stdio
│   └── tools/
│       └── malware_tools.py # All analysis functions
├── data/                    # APK samples + generated reports
├── Dockerfile
├── docker-compose.yml
├── start.sh
├── requirements.txt
└── README.md
```

---

## Quick Start

### Prerequisites

- Docker Desktop installed
- A free API key from [Groq](https://console.groq.com) (recommended) or Google AI Studio

### 1. Clone the repository

```bash
git clone https://github.com/ELANSARI05/AgenticAPKFlow.git
cd AgenticAPKFlow
```

### 2. Configure environment

Create a `.env` file in the project root:

```env
GROQ_API_KEY=gsk_your_key_here
```

> **Supported providers:** Groq (recommended, 100k free tokens/day), OpenRouter, or Google Gemini

### 3. Place your APK

```
data/
└── your_app.apk
```

### 4. Build and run

```bash
# Build the Docker image
docker compose build

# Analyze an APK
docker compose run --rm agentic-apk /app/data/your_app.apk

# Or use the default (data/test_app.apk)
docker compose run --rm agentic-apk
```

### 5. View results

Reports are saved to the `data/` folder on your host machine:

```
data/
├── SECURITY_REPORT.md          # Full Markdown report
├── SECURITY_REPORT.json        # Structured JSON output
├── agent_reasoning.md          # LLM investigation chain
└── analysis_cache_<apk>.json   # Raw tool outputs
```

---

## Sample Results

### UnCrackable Level 1 (OWASP MASTG)
```
✅ Found hardcoded AES key: 8d127684cbc37c17616d806cf50473cc
✅ Found Base64 ciphertext: 5UJiFctbmgbDoLXmpL12mkno8HT4Lv8dlat8FxR2GOc=
✅ Detected: Root Detection (bypass: Magisk Hide / Frida)
✅ MITRE: T1406 (Obfuscation), T1521.001 (Symmetric Crypto)
```

### InsecureBankv2
```
✅ 3 confirmed threat hypotheses:
   - Contact Harvesting (T1636.003) — getContentResolver detected
   - Toll Fraud (T1616) — SmsManager.sendTextMessage detected
   - Dynamic Code Loading (T1407) — DexClassLoader detected
✅ 13 MITRE ATT&CK techniques across 5 tactics
✅ 56 URLs, 18 domains, 1 hardcoded IP
✅ MD5 and AES/CBC crypto usage flagged
```

---

## Evaluation Results

| APK | Size | Secrets | MITRE Techniques | Confirmed Threats | Verdict |
|-----|------|---------|-----------------|-------------------|---------|
| UnCrackable L1 | 13KB | 2 | 2 | 0 | 🔴 HIGH |
| UnCrackable L2 | ~500KB | 1 | 7 | 0 | 🔴 HIGH |
| UnCrackable L3 | ~1MB | 1 | 9 | 0 | 🔴 HIGH |
| InsecureBankv2 | 3MB | 5 | 13 | 3 | 🔴 HIGH |

---

## Hybrid Analysis (Static + Dynamic)

When a CAPEv2 sandbox report is available, the system produces a **hybrid report** combining static and dynamic findings:

```bash
# Place your CAPEv2 report JSON in the data/ folder
docker compose run --rm agentic-apk /app/data/malware.apk
# Agent automatically detects cape_report.json and calls hybrid analysis
```

The hybrid report provides:
- **Runtime API calls** — suspicious calls observed during actual execution (CreateRemoteThread, VirtualAllocEx, RegSetValueExA...)
- **Live network IOCs** — actual TCP connections, HTTP POST requests, DNS queries made during sandbox run
- **Dropped files** — executables/payloads dropped during execution
- **Registry persistence** — Run keys written for auto-start persistence
- **Process injection** — confirmed via API call sequence (VirtualAllocEx → WriteProcessMemory → CreateRemoteThread)
- **Unified MITRE table** — techniques from both static and dynamic labeled by source

### Getting CAPEv2 Reports

Use any of these sources:
- [AVAST-CTU CAPE Dataset](https://github.com/avast/avast-ctu-cape-dataset) — thousands of real malware reports
- [MalwareBazaar](https://bazaar.abuse.ch) — upload a sample, get sandbox report
- Your own CAPEv2 instance — `https://capev2.readthedocs.io`
- Sample report included: `data/sample_cape_report.json` (Emotet sample)

---

## Limitations

- **Static analysis only** — dynamic behaviors (runtime decryption, C2 communication) require Frida-based dynamic analysis as a follow-up
- **Native libraries** — crypto in `.so` files is detected but not inspected; Ghidra or radare2 needed for native analysis
- **False positives** — Android support library code may trigger some patterns (e.g., accessibility `password` strings)
- **API quota** — free LLM tiers have daily token limits; `direct_run.py` provides quota-free analysis using cached results

---

## Running Without LLM (No API Key)

For quota-free analysis using the Python tools directly:

```bash
docker compose run --rm \
  -e GROQ_API_KEY= -e GOOGLE_API_KEY= -e OPENROUTER_API_KEY= \
  agentic-apk python3 src/agent/direct_run.py /app/data/your_app.apk
```

This produces the same full report without any LLM API calls.

---

## License

MIT License — see [LICENSE](LICENSE) for details.

---

*Built with Agno, FastMCP, and LLaMA 3.3 · Static + Dynamic Analysis · Course Project 2026*