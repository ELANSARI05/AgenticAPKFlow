# Use a stable Python base image (Debian Bookworm)
FROM python:3.11-slim-bookworm

# Prevent Python from writing pyc files and buffering stdout
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Install System Dependencies
# ADDED: 'dos2unix' to fix Windows line ending issues
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk-headless \
    wget \
    unzip \
    curl \
    git \
    dos2unix \
    && rm -rf /var/lib/apt/lists/*

# --- Install JADX (Decompiler) ---
ENV JADX_VERSION=1.5.0
RUN wget https://github.com/skylot/jadx/releases/download/v${JADX_VERSION}/jadx-${JADX_VERSION}.zip -O /tmp/jadx.zip && \
    mkdir -p /opt/jadx && \
    unzip /tmp/jadx.zip -d /opt/jadx && \
    rm /tmp/jadx.zip
ENV PATH="/opt/jadx/bin:${PATH}"

# --- Install Apktool (Resource Extractor) ---
ENV APKTOOL_VERSION=2.9.3
RUN wget https://raw.githubusercontent.com/iBotPeaches/Apktool/master/scripts/linux/apktool -O /usr/local/bin/apktool && \
    chmod +x /usr/local/bin/apktool && \
    wget https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_${APKTOOL_VERSION}.jar -O /usr/local/bin/apktool.jar

# Set Work Directory
WORKDIR /app

# Install Python Dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy Source Code
COPY src/ ./src/
COPY start.sh .

# CRITICAL FIX: Convert Windows line endings to Linux
RUN dos2unix start.sh
RUN chmod +x start.sh

# Define entrypoint
ENTRYPOINT ["./start.sh"]