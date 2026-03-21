#!/bin/bash
set -e

if [ -z "$GOOGLE_API_KEY" ]; then
  echo "❌ Error: GOOGLE_API_KEY is not set."
  exit 1
fi

APK_PATH="${1:-/app/data/test_app.apk}"

if [ ! -f "$APK_PATH" ]; then
  echo "❌ Error: APK not found at $APK_PATH"
  echo "   Place your APK in the data/ folder and pass its name as argument."
  exit 1
fi

echo "✅ Environment OK"
echo "📱 Target: $APK_PATH"
exec python src/agent/main.py "$APK_PATH"