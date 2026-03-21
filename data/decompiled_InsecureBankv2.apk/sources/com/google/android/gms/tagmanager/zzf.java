package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzag;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzf extends zzak {
    private static final String ID = com.google.android.gms.internal.zzad.APP_ID.toString();
    private final Context mContext;

    public zzf(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public zzag.zza zzE(Map<String, zzag.zza> map) {
        return zzdf.zzI(this.mContext.getPackageName());
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public boolean zzyh() {
        return true;
    }
}
