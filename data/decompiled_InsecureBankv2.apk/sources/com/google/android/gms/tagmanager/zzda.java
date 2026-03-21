package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzda extends zzak {
    private static final String ID = com.google.android.gms.internal.zzad.TIME.toString();

    public zzda() {
        super(ID, new String[0]);
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public zzag.zza zzE(Map<String, zzag.zza> map) {
        return zzdf.zzI(Long.valueOf(System.currentTimeMillis()));
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public boolean zzyh() {
        return false;
    }
}
