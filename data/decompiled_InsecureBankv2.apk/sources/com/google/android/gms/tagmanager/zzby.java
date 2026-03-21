package com.google.android.gms.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.internal.zzld;
import com.google.android.gms.tagmanager.zzcx;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.http.impl.client.DefaultHttpClient;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzby implements zzau {
    private static final String zzKg = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL,'%s' INTEGER NOT NULL);", "gtm_hits", "hit_id", "hit_time", "hit_url", "hit_first_send_time");
    private final Context mContext;
    private final zzb zzaMs;
    private volatile zzac zzaMt;
    private final zzav zzaMu;
    private final String zzaMv;
    private long zzaMw;
    private final int zzaMx;
    private zzlb zzpw;

    /* loaded from: classes.dex */
    class zza implements zzcx.zza {
        zza() {
        }

        @Override // com.google.android.gms.tagmanager.zzcx.zza
        public void zza(zzaq zzaqVar) {
            zzby.this.zzq(zzaqVar.zzyO());
        }

        @Override // com.google.android.gms.tagmanager.zzcx.zza
        public void zzb(zzaq zzaqVar) {
            zzby.this.zzq(zzaqVar.zzyO());
            zzbg.zzaB("Permanent failure dispatching hitId: " + zzaqVar.zzyO());
        }

        @Override // com.google.android.gms.tagmanager.zzcx.zza
        public void zzc(zzaq zzaqVar) {
            long zzyP = zzaqVar.zzyP();
            if (zzyP == 0) {
                zzby.this.zzd(zzaqVar.zzyO(), zzby.this.zzpw.currentTimeMillis());
            } else if (zzyP + 14400000 < zzby.this.zzpw.currentTimeMillis()) {
                zzby.this.zzq(zzaqVar.zzyO());
                zzbg.zzaB("Giving up on failed hitId: " + zzaqVar.zzyO());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class zzb extends SQLiteOpenHelper {
        private long zzaMA;
        private boolean zzaMz;

        zzb(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
            this.zzaMA = 0L;
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x0048  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private boolean zza(java.lang.String r11, android.database.sqlite.SQLiteDatabase r12) {
            /*
                r10 = this;
                r8 = 0
                r9 = 0
                java.lang.String r1 = "SQLITE_MASTER"
                r0 = 1
                java.lang.String[] r2 = new java.lang.String[r0]     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L45
                r0 = 0
                java.lang.String r3 = "name"
                r2[r0] = r3     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L45
                java.lang.String r3 = "name=?"
                r0 = 1
                java.lang.String[] r4 = new java.lang.String[r0]     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L45
                r0 = 0
                r4[r0] = r11     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L45
                r5 = 0
                r6 = 0
                r7 = 0
                r0 = r12
                android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L45
                boolean r0 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L4c android.database.sqlite.SQLiteException -> L53
                if (r1 == 0) goto L25
                r1.close()
            L25:
                return r0
            L26:
                r0 = move-exception
                r0 = r9
            L28:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4f
                r1.<init>()     // Catch: java.lang.Throwable -> L4f
                java.lang.String r2 = "Error querying for table "
                java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L4f
                java.lang.StringBuilder r1 = r1.append(r11)     // Catch: java.lang.Throwable -> L4f
                java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L4f
                com.google.android.gms.tagmanager.zzbg.zzaC(r1)     // Catch: java.lang.Throwable -> L4f
                if (r0 == 0) goto L43
                r0.close()
            L43:
                r0 = r8
                goto L25
            L45:
                r0 = move-exception
            L46:
                if (r9 == 0) goto L4b
                r9.close()
            L4b:
                throw r0
            L4c:
                r0 = move-exception
                r9 = r1
                goto L46
            L4f:
                r1 = move-exception
                r9 = r0
                r0 = r1
                goto L46
            L53:
                r0 = move-exception
                r0 = r1
                goto L28
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzby.zzb.zza(java.lang.String, android.database.sqlite.SQLiteDatabase):boolean");
        }

        private void zzc(SQLiteDatabase sQLiteDatabase) {
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM gtm_hits WHERE 0", null);
            HashSet hashSet = new HashSet();
            try {
                for (String str : rawQuery.getColumnNames()) {
                    hashSet.add(str);
                }
                rawQuery.close();
                if (!hashSet.remove("hit_id") || !hashSet.remove("hit_url") || !hashSet.remove("hit_time") || !hashSet.remove("hit_first_send_time")) {
                    throw new SQLiteException("Database column missing");
                }
                if (!hashSet.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } catch (Throwable th) {
                rawQuery.close();
                throw th;
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public SQLiteDatabase getWritableDatabase() {
            if (this.zzaMz && this.zzaMA + 3600000 > zzby.this.zzpw.currentTimeMillis()) {
                throw new SQLiteException("Database creation failed");
            }
            SQLiteDatabase sQLiteDatabase = null;
            this.zzaMz = true;
            this.zzaMA = zzby.this.zzpw.currentTimeMillis();
            try {
                sQLiteDatabase = super.getWritableDatabase();
            } catch (SQLiteException e) {
                zzby.this.mContext.getDatabasePath(zzby.this.zzaMv).delete();
            }
            if (sQLiteDatabase == null) {
                sQLiteDatabase = super.getWritableDatabase();
            }
            this.zzaMz = false;
            return sQLiteDatabase;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase db) {
            zzal.zzbe(db.getPath());
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase db) {
            if (Build.VERSION.SDK_INT < 15) {
                Cursor rawQuery = db.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    rawQuery.moveToFirst();
                } finally {
                    rawQuery.close();
                }
            }
            if (zza("gtm_hits", db)) {
                zzc(db);
            } else {
                db.execSQL(zzby.zzKg);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzby(zzav zzavVar, Context context) {
        this(zzavVar, context, "gtm_urls.db", 2000);
    }

    zzby(zzav zzavVar, Context context, String str, int i) {
        this.mContext = context.getApplicationContext();
        this.zzaMv = str;
        this.zzaMu = zzavVar;
        this.zzpw = zzld.zzoQ();
        this.zzaMs = new zzb(this.mContext, this.zzaMv);
        this.zzaMt = new zzcx(new DefaultHttpClient(), this.mContext, new zza());
        this.zzaMw = 0L;
        this.zzaMx = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzd(long j, long j2) {
        SQLiteDatabase zzer = zzer("Error opening database for getNumStoredHits.");
        if (zzer == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("hit_first_send_time", Long.valueOf(j2));
        try {
            zzer.update("gtm_hits", contentValues, "hit_id=?", new String[]{String.valueOf(j)});
        } catch (SQLiteException e) {
            zzbg.zzaC("Error setting HIT_FIRST_DISPATCH_TIME for hitId: " + j);
            zzq(j);
        }
    }

    private SQLiteDatabase zzer(String str) {
        try {
            return this.zzaMs.getWritableDatabase();
        } catch (SQLiteException e) {
            zzbg.zzaC(str);
            return null;
        }
    }

    private void zzh(long j, String str) {
        SQLiteDatabase zzer = zzer("Error opening database for putHit");
        if (zzer == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("hit_time", Long.valueOf(j));
        contentValues.put("hit_url", str);
        contentValues.put("hit_first_send_time", (Integer) 0);
        try {
            zzer.insert("gtm_hits", null, contentValues);
            this.zzaMu.zzan(false);
        } catch (SQLiteException e) {
            zzbg.zzaC("Error storing hit");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzq(long j) {
        zzf(new String[]{String.valueOf(j)});
    }

    private void zzzb() {
        int zzzc = (zzzc() - this.zzaMx) + 1;
        if (zzzc > 0) {
            List<String> zziY = zziY(zzzc);
            zzbg.zzaB("Store full, deleting " + zziY.size() + " hits to make room.");
            zzf((String[]) zziY.toArray(new String[0]));
        }
    }

    @Override // com.google.android.gms.tagmanager.zzau
    public void dispatch() {
        zzbg.zzaB("GTM Dispatch running...");
        if (this.zzaMt.zzyH()) {
            List<zzaq> zziZ = zziZ(40);
            if (zziZ.isEmpty()) {
                zzbg.zzaB("...nothing to dispatch");
                this.zzaMu.zzan(true);
            } else {
                this.zzaMt.zzr(zziZ);
                if (zzzd() > 0) {
                    zzcu.zzzz().dispatch();
                }
            }
        }
    }

    void zzf(String[] strArr) {
        SQLiteDatabase zzer;
        if (strArr == null || strArr.length == 0 || (zzer = zzer("Error opening database for deleteHits.")) == null) {
            return;
        }
        try {
            zzer.delete("gtm_hits", String.format("HIT_ID in (%s)", TextUtils.join(",", Collections.nCopies(strArr.length, "?"))), strArr);
            this.zzaMu.zzan(zzzc() == 0);
        } catch (SQLiteException e) {
            zzbg.zzaC("Error deleting hits");
        }
    }

    @Override // com.google.android.gms.tagmanager.zzau
    public void zzg(long j, String str) {
        zzis();
        zzzb();
        zzh(j, str);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0041, code lost:
    
        if (r1.moveToFirst() != false) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0043, code lost:
    
        r9.add(java.lang.String.valueOf(r1.getLong(0)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0053, code lost:
    
        if (r1.moveToNext() != false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0055, code lost:
    
        if (r1 == null) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0057, code lost:
    
        r1.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0082  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.util.List<java.lang.String> zziY(int r14) {
        /*
            r13 = this;
            r10 = 0
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            if (r14 > 0) goto Lf
            java.lang.String r0 = "Invalid maxHits specified. Skipping"
            com.google.android.gms.tagmanager.zzbg.zzaC(r0)
            r0 = r9
        Le:
            return r0
        Lf:
            java.lang.String r0 = "Error opening database for peekHitIds."
            android.database.sqlite.SQLiteDatabase r0 = r13.zzer(r0)
            if (r0 != 0) goto L19
            r0 = r9
            goto Le
        L19:
            java.lang.String r1 = "gtm_hits"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            r3 = 0
            java.lang.String r4 = "hit_id"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "%s ASC"
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            r11 = 0
            java.lang.String r12 = "hit_id"
            r8[r11] = r12     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            java.lang.String r7 = java.lang.String.format(r7, r8)     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            java.lang.String r8 = java.lang.Integer.toString(r14)     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: android.database.sqlite.SQLiteException -> L5c java.lang.Throwable -> L7e
            boolean r0 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L86 android.database.sqlite.SQLiteException -> L88
            if (r0 == 0) goto L55
        L43:
            r0 = 0
            long r2 = r1.getLong(r0)     // Catch: java.lang.Throwable -> L86 android.database.sqlite.SQLiteException -> L88
            java.lang.String r0 = java.lang.String.valueOf(r2)     // Catch: java.lang.Throwable -> L86 android.database.sqlite.SQLiteException -> L88
            r9.add(r0)     // Catch: java.lang.Throwable -> L86 android.database.sqlite.SQLiteException -> L88
            boolean r0 = r1.moveToNext()     // Catch: java.lang.Throwable -> L86 android.database.sqlite.SQLiteException -> L88
            if (r0 != 0) goto L43
        L55:
            if (r1 == 0) goto L5a
            r1.close()
        L5a:
            r0 = r9
            goto Le
        L5c:
            r0 = move-exception
            r1 = r10
        L5e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L86
            r2.<init>()     // Catch: java.lang.Throwable -> L86
            java.lang.String r3 = "Error in peekHits fetching hitIds: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L86
            java.lang.String r0 = r0.getMessage()     // Catch: java.lang.Throwable -> L86
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch: java.lang.Throwable -> L86
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L86
            com.google.android.gms.tagmanager.zzbg.zzaC(r0)     // Catch: java.lang.Throwable -> L86
            if (r1 == 0) goto L5a
            r1.close()
            goto L5a
        L7e:
            r0 = move-exception
            r1 = r10
        L80:
            if (r1 == 0) goto L85
            r1.close()
        L85:
            throw r0
        L86:
            r0 = move-exception
            goto L80
        L88:
            r0 = move-exception
            goto L5e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzby.zziY(int):java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0049, code lost:
    
        if (r13.moveToFirst() != false) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004b, code lost:
    
        r12.add(new com.google.android.gms.tagmanager.zzaq(r13.getLong(0), r13.getLong(1), r13.getLong(2)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0066, code lost:
    
        if (r13.moveToNext() != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0068, code lost:
    
        if (r13 == null) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x006a, code lost:
    
        r13.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x006e, code lost:
    
        r3 = r2.query("gtm_hits", new java.lang.String[]{"hit_id", "hit_url"}, null, null, null, null, java.lang.String.format("%s ASC", "hit_id"), java.lang.Integer.toString(r17));
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x009b, code lost:
    
        if (r3.moveToFirst() == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x009d, code lost:
    
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00aa, code lost:
    
        if (((android.database.sqlite.SQLiteCursor) r3).getWindow().getNumRows() <= 0) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ac, code lost:
    
        ((com.google.android.gms.tagmanager.zzaq) r12.get(r4)).zzev(r3.getString(1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00ba, code lost:
    
        r2 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c0, code lost:
    
        if (r3.moveToNext() != false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x017d, code lost:
    
        r4 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00f6, code lost:
    
        com.google.android.gms.tagmanager.zzbg.zzaC(java.lang.String.format("HitString for hitId %d too large.  Hit will be deleted.", java.lang.Long.valueOf(((com.google.android.gms.tagmanager.zzaq) r12.get(r4)).zzyO())));
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00c2, code lost:
    
        if (r3 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c4, code lost:
    
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:?, code lost:
    
        return r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0114, code lost:
    
        r2 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0115, code lost:
    
        r13 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0116, code lost:
    
        com.google.android.gms.tagmanager.zzbg.zzaC("Error in peekHits fetching hit url: " + r2.getMessage());
        r3 = new java.util.ArrayList();
        r4 = false;
        r5 = r12.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0140, code lost:
    
        r2 = (com.google.android.gms.tagmanager.zzaq) r5.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x014e, code lost:
    
        if (android.text.TextUtils.isEmpty(r2.zzyQ()) == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0150, code lost:
    
        if (r4 != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x015a, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x015b, code lost:
    
        r3.add(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0152, code lost:
    
        if (r13 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0154, code lost:
    
        r13.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:?, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0166, code lost:
    
        r2 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0167, code lost:
    
        r13 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0160, code lost:
    
        if (r13 != null) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0162, code lost:
    
        r13.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0165, code lost:
    
        throw r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0169, code lost:
    
        r2 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x015f, code lost:
    
        r2 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:80:0x00f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<com.google.android.gms.tagmanager.zzaq> zziZ(int r17) {
        /*
            Method dump skipped, instructions count: 384
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzby.zziZ(int):java.util.List");
    }

    int zzis() {
        long currentTimeMillis = this.zzpw.currentTimeMillis();
        if (currentTimeMillis <= this.zzaMw + 86400000) {
            return 0;
        }
        this.zzaMw = currentTimeMillis;
        SQLiteDatabase zzer = zzer("Error opening database for deleteStaleHits.");
        if (zzer == null) {
            return 0;
        }
        int delete = zzer.delete("gtm_hits", "HIT_TIME < ?", new String[]{Long.toString(this.zzpw.currentTimeMillis() - 2592000000L)});
        this.zzaMu.zzan(zzzc() == 0);
        return delete;
    }

    int zzzc() {
        Cursor cursor = null;
        SQLiteDatabase zzer = zzer("Error opening database for getNumStoredHits.");
        try {
            if (zzer != null) {
                try {
                    cursor = zzer.rawQuery("SELECT COUNT(*) from gtm_hits", null);
                    r0 = cursor.moveToFirst() ? (int) cursor.getLong(0) : 0;
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SQLiteException e) {
                    zzbg.zzaC("Error getting numStoredHits");
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            return r0;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int zzzd() {
        /*
            r10 = this;
            r8 = 0
            r9 = 0
            java.lang.String r0 = "Error opening database for getNumStoredHits."
            android.database.sqlite.SQLiteDatabase r0 = r10.zzer(r0)
            if (r0 != 0) goto Lb
        La:
            return r8
        Lb:
            java.lang.String r1 = "gtm_hits"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: android.database.sqlite.SQLiteException -> L2f java.lang.Throwable -> L3d
            r3 = 0
            java.lang.String r4 = "hit_id"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L2f java.lang.Throwable -> L3d
            r3 = 1
            java.lang.String r4 = "hit_first_send_time"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L2f java.lang.Throwable -> L3d
            java.lang.String r3 = "hit_first_send_time=0"
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L2f java.lang.Throwable -> L3d
            int r0 = r1.getCount()     // Catch: java.lang.Throwable -> L44 android.database.sqlite.SQLiteException -> L4b
            if (r1 == 0) goto L2d
            r1.close()
        L2d:
            r8 = r0
            goto La
        L2f:
            r0 = move-exception
            r0 = r9
        L31:
            java.lang.String r1 = "Error getting num untried hits"
            com.google.android.gms.tagmanager.zzbg.zzaC(r1)     // Catch: java.lang.Throwable -> L47
            if (r0 == 0) goto L4e
            r0.close()
            r0 = r8
            goto L2d
        L3d:
            r0 = move-exception
        L3e:
            if (r9 == 0) goto L43
            r9.close()
        L43:
            throw r0
        L44:
            r0 = move-exception
            r9 = r1
            goto L3e
        L47:
            r1 = move-exception
            r9 = r0
            r0 = r1
            goto L3e
        L4b:
            r0 = move-exception
            r0 = r1
            goto L31
        L4e:
            r0 = r8
            goto L2d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzby.zzzd():int");
    }
}
