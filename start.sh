#!/bin/bash
set -e

# ── 1. Check required environment variables ───────────────────────────────────
if [ -z "$GROQ_API_KEY" ] && [ -z "$OPENROUTER_API_KEY" ] && [ -z "$GOOGLE_API_KEY" ]; then
  echo "❌ Error: No API key found. Set GROQ_API_KEY, OPENROUTER_API_KEY or GOOGLE_API_KEY."
  exit 1
fi

# ── 2. Validate dependencies ──────────────────────────────────────────────────
echo "🔍 Validating dependencies..."

check_cmd() {
  if ! command -v "$1" &> /dev/null; then
    echo "❌ Missing dependency: $1"
    exit 1
  else
    echo "  ✅ $1 found"
  fi
}

check_cmd java
check_cmd jadx
check_cmd apktool
check_cmd aapt
check_cmd python3

# Check Python packages
python3 -c "import agno" 2>/dev/null || { echo "❌ Python package 'agno' not installed"; exit 1; }
python3 -c "import fastmcp" 2>/dev/null || { echo "❌ Python package 'fastmcp' not installed"; exit 1; }

echo "✅ All dependencies validated"

# ── 3. Validate APK path ──────────────────────────────────────────────────────
APK_PATH="${1:-/app/data/test_app.apk}"

if [ ! -f "$APK_PATH" ]; then
  echo "❌ Error: APK not found at $APK_PATH"
  echo "   Place your APK in the data/ folder and pass its name as argument."
  exit 1
fi

echo "✅ Environment OK"
echo "📱 Target: $APK_PATH"

# ── 4. Start the workflow ─────────────────────────────────────────────────────
if [ -n "$GROQ_API_KEY" ] || [ -n "$OPENROUTER_API_KEY" ] || [ -n "$GOOGLE_API_KEY" ]; then
  echo " LLM key found — running multi-agent analysis"
  exec python3 src/agent/main.py "$APK_PATH"
else
  echo "⚠️  No LLM API key — running direct analysis"
  exec python3 src/agent/direct_run.py "$APK_PATH"
fi