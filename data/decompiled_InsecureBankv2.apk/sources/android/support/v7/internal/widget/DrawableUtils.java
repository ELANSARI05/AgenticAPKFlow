package android.support.v7.internal.widget;

import android.graphics.Rect;
import android.os.Build;

/* loaded from: classes.dex */
public class DrawableUtils {
    public static final Rect INSETS_NONE = new Rect();
    private static final String TAG = "DrawableUtils";
    private static Class<?> sInsetsClazz;

    static {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
            } catch (ClassNotFoundException e) {
            }
        }
    }

    private DrawableUtils() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003c, code lost:
    
        switch(r8) {
            case 0: goto L24;
            case 1: goto L30;
            case 2: goto L31;
            case 3: goto L32;
            default: goto L37;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x006a, code lost:
    
        r7.left = r2.getInt(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003f, code lost:
    
        r4 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x007c, code lost:
    
        r7.top = r2.getInt(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0083, code lost:
    
        r7.right = r2.getInt(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x008a, code lost:
    
        r7.bottom = r2.getInt(r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.graphics.Rect getOpticalBounds(android.graphics.drawable.Drawable r12) {
        /*
            r9 = 0
            java.lang.Class<?> r8 = android.support.v7.internal.widget.DrawableUtils.sInsetsClazz
            if (r8 == 0) goto L79
            android.graphics.drawable.Drawable r12 = android.support.v4.graphics.drawable.DrawableCompat.unwrap(r12)     // Catch: java.lang.Exception -> L71
            java.lang.Class r8 = r12.getClass()     // Catch: java.lang.Exception -> L71
            java.lang.String r10 = "getOpticalInsets"
            r11 = 0
            java.lang.Class[] r11 = new java.lang.Class[r11]     // Catch: java.lang.Exception -> L71
            java.lang.reflect.Method r3 = r8.getMethod(r10, r11)     // Catch: java.lang.Exception -> L71
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: java.lang.Exception -> L71
            java.lang.Object r5 = r3.invoke(r12, r8)     // Catch: java.lang.Exception -> L71
            if (r5 == 0) goto L79
            android.graphics.Rect r7 = new android.graphics.Rect     // Catch: java.lang.Exception -> L71
            r7.<init>()     // Catch: java.lang.Exception -> L71
            java.lang.Class<?> r8 = android.support.v7.internal.widget.DrawableUtils.sInsetsClazz     // Catch: java.lang.Exception -> L71
            java.lang.reflect.Field[] r0 = r8.getFields()     // Catch: java.lang.Exception -> L71
            int r6 = r0.length     // Catch: java.lang.Exception -> L71
            r4 = 0
        L2c:
            if (r4 >= r6) goto L7b
            r2 = r0[r4]     // Catch: java.lang.Exception -> L71
            java.lang.String r10 = r2.getName()     // Catch: java.lang.Exception -> L71
            r8 = -1
            int r11 = r10.hashCode()     // Catch: java.lang.Exception -> L71
            switch(r11) {
                case -1383228885: goto L60;
                case 115029: goto L4c;
                case 3317767: goto L42;
                case 108511772: goto L56;
                default: goto L3c;
            }     // Catch: java.lang.Exception -> L71
        L3c:
            switch(r8) {
                case 0: goto L6a;
                case 1: goto L7c;
                case 2: goto L83;
                case 3: goto L8a;
                default: goto L3f;
            }     // Catch: java.lang.Exception -> L71
        L3f:
            int r4 = r4 + 1
            goto L2c
        L42:
            java.lang.String r11 = "left"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L71
            if (r10 == 0) goto L3c
            r8 = r9
            goto L3c
        L4c:
            java.lang.String r11 = "top"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L71
            if (r10 == 0) goto L3c
            r8 = 1
            goto L3c
        L56:
            java.lang.String r11 = "right"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L71
            if (r10 == 0) goto L3c
            r8 = 2
            goto L3c
        L60:
            java.lang.String r11 = "bottom"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L71
            if (r10 == 0) goto L3c
            r8 = 3
            goto L3c
        L6a:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L71
            r7.left = r8     // Catch: java.lang.Exception -> L71
            goto L3f
        L71:
            r1 = move-exception
            java.lang.String r8 = "DrawableUtils"
            java.lang.String r9 = "Couldn't obtain the optical insets. Ignoring."
            android.util.Log.e(r8, r9)
        L79:
            android.graphics.Rect r7 = android.support.v7.internal.widget.DrawableUtils.INSETS_NONE
        L7b:
            return r7
        L7c:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L71
            r7.top = r8     // Catch: java.lang.Exception -> L71
            goto L3f
        L83:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L71
            r7.right = r8     // Catch: java.lang.Exception -> L71
            goto L3f
        L8a:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L71
            r7.bottom = r8     // Catch: java.lang.Exception -> L71
            goto L3f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.internal.widget.DrawableUtils.getOpticalBounds(android.graphics.drawable.Drawable):android.graphics.Rect");
    }
}
