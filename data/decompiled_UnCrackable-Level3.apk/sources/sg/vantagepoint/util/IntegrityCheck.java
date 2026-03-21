package sg.vantagepoint.util;

import android.content.Context;

/* loaded from: classes.dex */
public class IntegrityCheck {
    public static boolean isDebuggable(Context context) {
        return (context.getApplicationContext().getApplicationInfo().flags & 2) != 0;
    }
}
