package sg.vantagepoint.uncrackable3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sg.vantagepoint.util.IntegrityCheck;
import sg.vantagepoint.util.RootDetection;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "UnCrackable3";
    static int tampered = 0;
    private static final String xorkey = "pizzapizzapizzapizzapizz";
    private CodeCheck check;
    Map<String, Long> crc;

    private native long baz();

    private native void init(byte[] bArr);

    /* JADX INFO: Access modifiers changed from: private */
    public void showDialog(String str) {
        AlertDialog create = new AlertDialog.Builder(this).create();
        create.setTitle(str);
        create.setMessage("This is unacceptable. The app is now going to exit.");
        create.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: sg.vantagepoint.uncrackable3.MainActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        create.setCancelable(false);
        create.show();
    }

    private void verifyLibs() {
        this.crc = new HashMap();
        this.crc.put("armeabi-v7a", Long.valueOf(Long.parseLong(getResources().getString(owasp.mstg.uncrackable3.R.string.armeabi_v7a))));
        this.crc.put("arm64-v8a", Long.valueOf(Long.parseLong(getResources().getString(owasp.mstg.uncrackable3.R.string.arm64_v8a))));
        this.crc.put("x86", Long.valueOf(Long.parseLong(getResources().getString(owasp.mstg.uncrackable3.R.string.x86))));
        this.crc.put("x86_64", Long.valueOf(Long.parseLong(getResources().getString(owasp.mstg.uncrackable3.R.string.x86_64))));
        try {
            ZipFile zipFile = new ZipFile(getPackageCodePath());
            for (Map.Entry<String, Long> entry : this.crc.entrySet()) {
                String str = "lib/" + entry.getKey() + "/libfoo.so";
                ZipEntry entry2 = zipFile.getEntry(str);
                Log.v(TAG, "CRC[" + str + "] = " + entry2.getCrc());
                if (entry2.getCrc() != entry.getValue().longValue()) {
                    tampered = 31337;
                    Log.v(TAG, str + ": Invalid checksum = " + entry2.getCrc() + ", supposed to be " + entry.getValue());
                }
            }
            ZipEntry entry3 = zipFile.getEntry("classes.dex");
            Log.v(TAG, "CRC[classes.dex] = " + entry3.getCrc());
            if (entry3.getCrc() != baz()) {
                tampered = 31337;
                Log.v(TAG, "classes.dex: crc = " + entry3.getCrc() + ", supposed to be " + baz());
            }
        } catch (IOException unused) {
            Log.v(TAG, "Exception");
            System.exit(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v2, types: [sg.vantagepoint.uncrackable3.MainActivity$2] */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        verifyLibs();
        init(xorkey.getBytes());
        new AsyncTask<Void, String, String>() { // from class: sg.vantagepoint.uncrackable3.MainActivity.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public String doInBackground(Void... voidArr) {
                while (!Debug.isDebuggerConnected()) {
                    SystemClock.sleep(100L);
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(String str) {
                MainActivity.this.showDialog("Debugger detected!");
                System.exit(0);
            }
        }.execute(null, null, null);
        if (RootDetection.checkRoot1() || RootDetection.checkRoot2() || RootDetection.checkRoot3() || IntegrityCheck.isDebuggable(getApplicationContext()) || tampered != 0) {
            showDialog("Rooting or tampering detected.");
        }
        this.check = new CodeCheck();
        super.onCreate(bundle);
        setContentView(owasp.mstg.uncrackable3.R.layout.activity_main);
    }

    public void verify(View view) {
        String obj = ((EditText) findViewById(owasp.mstg.uncrackable3.R.id.edit_text)).getText().toString();
        AlertDialog create = new AlertDialog.Builder(this).create();
        if (this.check.check_code(obj)) {
            create.setTitle("Success!");
            create.setMessage("This is the correct secret.");
        } else {
            create.setTitle("Nope...");
            create.setMessage("That's not it. Try again.");
        }
        create.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: sg.vantagepoint.uncrackable3.MainActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        create.show();
    }

    static {
        System.loadLibrary("foo");
    }
}
