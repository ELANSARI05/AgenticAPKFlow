package android.support.v7.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ar {
    private static final ThreadLocal<TypedValue> i = new ThreadLocal<>();
    static final int[] a = {-16842910};
    static final int[] b = {R.attr.state_focused};
    static final int[] c = {R.attr.state_activated};
    static final int[] d = {R.attr.state_pressed};
    static final int[] e = {R.attr.state_checked};
    static final int[] f = {R.attr.state_selected};
    static final int[] g = {-16842919, -16842908};
    static final int[] h = new int[0];
    private static final int[] j = new int[1];

    public static int a(Context context, int i2) {
        j[0] = i2;
        aw a2 = aw.a(context, (AttributeSet) null, j);
        try {
            return a2.b(0, 0);
        } finally {
            a2.a();
        }
    }

    static int a(Context context, int i2, float f2) {
        return android.support.v4.b.a.b(a(context, i2), Math.round(Color.alpha(r0) * f2));
    }

    private static TypedValue a() {
        TypedValue typedValue = i.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        i.set(typedValue2);
        return typedValue2;
    }

    public static ColorStateList b(Context context, int i2) {
        j[0] = i2;
        aw a2 = aw.a(context, (AttributeSet) null, j);
        try {
            return a2.e(0);
        } finally {
            a2.a();
        }
    }

    public static int c(Context context, int i2) {
        ColorStateList b2 = b(context, i2);
        if (b2 != null && b2.isStateful()) {
            return b2.getColorForState(a, b2.getDefaultColor());
        }
        TypedValue a2 = a();
        context.getTheme().resolveAttribute(R.attr.disabledAlpha, a2, true);
        return a(context, i2, a2.getFloat());
    }
}
