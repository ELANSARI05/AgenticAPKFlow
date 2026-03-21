package sg.vantagepoint.uncrackable3;

/* loaded from: classes.dex */
public class CodeCheck {
    private static final String TAG = "CodeCheck";

    private native boolean bar(byte[] bArr);

    public boolean check_code(String str) {
        return bar(str.getBytes());
    }
}
