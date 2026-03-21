package android.support.v4.b;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class h {
    public static File a(Context context) {
        String str = ".font" + Process.myPid() + "-" + Process.myTid() + "-";
        for (int i = 0; i < 100; i++) {
            File file = new File(context.getCacheDir(), str + i);
            if (file.createNewFile()) {
                return file;
            }
        }
        return null;
    }

    public static ByteBuffer a(Context context, Resources resources, int i) {
        File a = a(context);
        if (a == null) {
            return null;
        }
        try {
            if (a(a, resources, i)) {
                return a(a);
            }
            return null;
        } finally {
            a.delete();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0049 A[Catch: all -> 0x004d, Throwable -> 0x0050, TryCatch #4 {Throwable -> 0x0050, blocks: (B:10:0x0013, B:13:0x002c, B:22:0x004c, B:21:0x0049, B:28:0x0045), top: B:9:0x0013 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[Catch: IOException -> 0x0067, SYNTHETIC, TRY_LEAVE, TryCatch #1 {IOException -> 0x0067, blocks: (B:3:0x0005, B:6:0x000f, B:15:0x0031, B:47:0x005a, B:44:0x0063, B:51:0x005f, B:45:0x0066), top: B:2:0x0005, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.nio.ByteBuffer a(android.content.Context r8, android.os.CancellationSignal r9, android.net.Uri r10) {
        /*
            android.content.ContentResolver r8 = r8.getContentResolver()
            r0 = 0
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r8 = r8.openFileDescriptor(r10, r1, r9)     // Catch: java.io.IOException -> L67
            if (r8 != 0) goto L13
            if (r8 == 0) goto L12
            r8.close()     // Catch: java.io.IOException -> L67
        L12:
            return r0
        L13:
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
            java.io.FileDescriptor r10 = r8.getFileDescriptor()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
            r9.<init>(r10)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
            java.nio.channels.FileChannel r1 = r9.getChannel()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            long r5 = r1.size()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            java.nio.channels.FileChannel$MapMode r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            r3 = 0
            java.nio.MappedByteBuffer r10 = r1.map(r2, r3, r5)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L38
            r9.close()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
            if (r8 == 0) goto L34
            r8.close()     // Catch: java.io.IOException -> L67
        L34:
            return r10
        L35:
            r10 = move-exception
            r1 = r0
            goto L3e
        L38:
            r10 = move-exception
            throw r10     // Catch: java.lang.Throwable -> L3a
        L3a:
            r1 = move-exception
            r7 = r1
            r1 = r10
            r10 = r7
        L3e:
            if (r1 == 0) goto L49
            r9.close()     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L4d
            goto L4c
        L44:
            r9 = move-exception
            r1.addSuppressed(r9)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
            goto L4c
        L49:
            r9.close()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
        L4c:
            throw r10     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L50
        L4d:
            r9 = move-exception
            r10 = r0
            goto L56
        L50:
            r9 = move-exception
            throw r9     // Catch: java.lang.Throwable -> L52
        L52:
            r10 = move-exception
            r7 = r10
            r10 = r9
            r9 = r7
        L56:
            if (r8 == 0) goto L66
            if (r10 == 0) goto L63
            r8.close()     // Catch: java.lang.Throwable -> L5e java.io.IOException -> L67
            goto L66
        L5e:
            r8 = move-exception
            r10.addSuppressed(r8)     // Catch: java.io.IOException -> L67
            goto L66
        L63:
            r8.close()     // Catch: java.io.IOException -> L67
        L66:
            throw r9     // Catch: java.io.IOException -> L67
        L67:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.b.h.a(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x002e A[Catch: IOException -> 0x0032, TryCatch #1 {IOException -> 0x0032, blocks: (B:3:0x0001, B:6:0x0016, B:20:0x0025, B:18:0x0031, B:17:0x002e, B:24:0x002a), top: B:2:0x0001, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0025 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.nio.ByteBuffer a(java.io.File r9) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.io.IOException -> L32
            r1.<init>(r9)     // Catch: java.io.IOException -> L32
            java.nio.channels.FileChannel r2 = r1.getChannel()     // Catch: java.lang.Throwable -> L1a java.lang.Throwable -> L1d
            long r6 = r2.size()     // Catch: java.lang.Throwable -> L1a java.lang.Throwable -> L1d
            java.nio.channels.FileChannel$MapMode r3 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L1a java.lang.Throwable -> L1d
            r4 = 0
            java.nio.MappedByteBuffer r9 = r2.map(r3, r4, r6)     // Catch: java.lang.Throwable -> L1a java.lang.Throwable -> L1d
            r1.close()     // Catch: java.io.IOException -> L32
            return r9
        L1a:
            r9 = move-exception
            r2 = r0
            goto L23
        L1d:
            r9 = move-exception
            throw r9     // Catch: java.lang.Throwable -> L1f
        L1f:
            r2 = move-exception
            r8 = r2
            r2 = r9
            r9 = r8
        L23:
            if (r2 == 0) goto L2e
            r1.close()     // Catch: java.lang.Throwable -> L29 java.io.IOException -> L32
            goto L31
        L29:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch: java.io.IOException -> L32
            goto L31
        L2e:
            r1.close()     // Catch: java.io.IOException -> L32
        L31:
            throw r9     // Catch: java.io.IOException -> L32
        L32:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.b.h.a(java.io.File):java.nio.ByteBuffer");
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static boolean a(File file, Resources resources, int i) {
        InputStream inputStream;
        try {
            inputStream = resources.openRawResource(i);
            try {
                boolean a = a(file, inputStream);
                a(inputStream);
                return a;
            } catch (Throwable th) {
                th = th;
                a(inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
    }

    public static boolean a(File file, InputStream inputStream) {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file, false);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    a(fileOutputStream);
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream2 = fileOutputStream;
            Log.e("TypefaceCompatUtil", "Error copying resource contents to temp file: " + e.getMessage());
            a(fileOutputStream2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            a(fileOutputStream2);
            throw th;
        }
    }
}
