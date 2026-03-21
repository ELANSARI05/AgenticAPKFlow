package com.google.android.gms.nearby.sharing.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.nearby.sharing.internal.zzc;

/* loaded from: classes.dex */
public interface zzd extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class zza extends Binder implements zzd {

        /* renamed from: com.google.android.gms.nearby.sharing.internal.zzd$zza$zza, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0227zza implements zzd {
            private IBinder zznF;

            C0227zza(IBinder iBinder) {
                this.zznF = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznF;
            }

            @Override // com.google.android.gms.nearby.sharing.internal.zzd
            public void zza(ProvideContentRequest provideContentRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    if (provideContentRequest != null) {
                        obtain.writeInt(1);
                        provideContentRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.nearby.sharing.internal.zzd
            public void zza(ReceiveContentRequest receiveContentRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    if (receiveContentRequest != null) {
                        obtain.writeInt(1);
                        receiveContentRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.nearby.sharing.internal.zzd
            public void zza(StopProvidingContentRequest stopProvidingContentRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    if (stopProvidingContentRequest != null) {
                        obtain.writeInt(1);
                        stopProvidingContentRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.nearby.sharing.internal.zzd
            public void zza(TrustedDevicesRequest trustedDevicesRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    if (trustedDevicesRequest != null) {
                        obtain.writeInt(1);
                        trustedDevicesRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zznF.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.nearby.sharing.internal.zzd
            public void zza(zzc zzcVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    obtain.writeStrongBinder(zzcVar != null ? zzcVar.asBinder() : null);
                    this.zznF.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static zzd zzdk(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzd)) ? new C0227zza(iBinder) : (zzd) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    zza(parcel.readInt() != 0 ? ProvideContentRequest.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    zza(parcel.readInt() != 0 ? StopProvidingContentRequest.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    zza(parcel.readInt() != 0 ? TrustedDevicesRequest.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    zza(parcel.readInt() != 0 ? ReceiveContentRequest.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 5:
                    parcel.enforceInterface("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    zza(zzc.zza.zzdj(parcel.readStrongBinder()));
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.nearby.sharing.internal.INearbySharingService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(ProvideContentRequest provideContentRequest) throws RemoteException;

    void zza(ReceiveContentRequest receiveContentRequest) throws RemoteException;

    void zza(StopProvidingContentRequest stopProvidingContentRequest) throws RemoteException;

    void zza(TrustedDevicesRequest trustedDevicesRequest) throws RemoteException;

    void zza(zzc zzcVar) throws RemoteException;
}
