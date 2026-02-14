import os
from google import genai

print("--- Connecting to Google API ---")
key = os.getenv("GOOGLE_API_KEY")

try:
    client = genai.Client(api_key=key)
    print("✅ Connection Successful! Scanning models...")
    print("-" * 30)
    
    # Simple loop: just print the name
    for m in client.models.list():
        print(f"👉 {m.name}")
            
except Exception as e:
    print(f"❌ Error: {e}")