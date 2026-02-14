#!/bin/bash
set -e

# 1. Print Header
echo "========================================"
echo "   Agentic-APK Environment Launcher"
echo "========================================"

# 2. Check for API Keys
if [ -z "$GOOGLE_API_KEY" ]; then
    echo "WARNING: GOOGLE_API_KEY is not set."
    echo "Please set it in your docker-compose.yml or .env file."
fi

# 3. Check for Tools
echo "Checking dependencies..."
java -version 2>&1 | head -n 1
jadx --version
echo "Apktool: $(apktool --version | head -n 1)"

# 4. Execute the command passed to docker run
echo "Starting process: $@"
exec "$@"