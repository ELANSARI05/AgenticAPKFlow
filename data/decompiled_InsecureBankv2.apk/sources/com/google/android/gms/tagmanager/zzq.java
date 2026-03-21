package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzq extends zzak {
    private static final String ID = com.google.android.gms.internal.zzad.CONTAINER_VERSION.toString();
    private final String zzTQ;

    public zzq(String str) {
        super(ID, new String[0]);
        this.zzTQ = str;
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public zzag.zza zzE(Map<String, zzag.zza> map) {
        return this.zzTQ == null ? zzdf.zzzQ() : zzdf.zzI(this.zzTQ);
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public boolean zzyh() {
        return true;
    }
}
