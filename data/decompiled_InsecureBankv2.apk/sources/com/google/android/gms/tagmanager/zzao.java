package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzao extends zzak {
    private static final String ID = com.google.android.gms.internal.zzad.GTM_VERSION.toString();

    public zzao() {
        super(ID, new String[0]);
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public zzag.zza zzE(Map<String, zzag.zza> map) {
        return zzdf.zzI("4.00");
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public boolean zzyh() {
        return true;
    }
}
