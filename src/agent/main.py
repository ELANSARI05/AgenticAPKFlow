import os
import sys
from agno.agent import Agent
from agno.models.google import Gemini

sys.path.append(os.path.join(os.path.dirname(__file__), '..'))

from tools.malware_tools import (
    check_environment, 
    extract_manifest_info, 
    analyze_static_indicators,
    list_directory,
    read_file_content,  
    save_report        
)

api_key = os.getenv("GOOGLE_API_KEY")
if not api_key:
    print("Error: GOOGLE_API_KEY not found.")
    exit(1)

analyst = Agent(
    model=Gemini(id="gemini-flash-latest", api_key=api_key),
    tools=[
        check_environment, 
        extract_manifest_info, 
        analyze_static_indicators, 
        list_directory,
        read_file_content, 
        save_report
    ], 
    description="""You are a Senior Malware Researcher.
    
    Your Goal: Find the hardcoded secret key in the APK.
    
    Execution Plan:
    1. Run 'analyze_static_indicators'. It will give you a list of files with specific paths (e.g., /app/data/.../MainActivity.java).
    2. IMMEDIATELY copy that exact path and use 'read_file_content' to read it. Do not use list_directory.
    3. Look at the code content. If you see a string like "8d12..." inside a SecretKeySpec, that is the key.
    4. Save your findings to 'FINAL_REPORT.md'.
    """,
    markdown=True
)

print("--- Starting Phase 4: Precision Inspection ---")
target_apk = "/app/data/test_app.apk"

analyst.print_response(
    f"Analyze {target_apk}. Find the hardcoded AES key. The static analyzer will tell you exactly which file to open.",
    stream=True,
    show_tool_calls=True 
)