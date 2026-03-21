package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.internal.zzag;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzg extends zzak {
    private static final String ID = com.google.android.gms.internal.zzad.APP_NAME.toString();
    private final Context mContext;

    public zzg(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public zzag.zza zzE(Map<String, zzag.zza> map) {
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            return zzdf.zzI(packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mContext.getPackageName(), 0)).toString());
        } catch (PackageManager.NameNotFoundException e) {
            zzbg.zzb("App name is not found.", e);
            return zzdf.zzzQ();
        }
    }

    @Override // com.google.android.gms.tagmanager.zzak
    public boolean zzyh() {
        return true;
    }
}
