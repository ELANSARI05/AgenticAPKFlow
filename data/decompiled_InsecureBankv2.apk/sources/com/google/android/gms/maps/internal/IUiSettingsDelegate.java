package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IUiSettingsDelegate extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class zza extends Binder implements IUiSettingsDelegate {

        /* renamed from: com.google.android.gms.maps.internal.IUiSettingsDelegate$zza$zza, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0186zza implements IUiSettingsDelegate {
            private IBinder zznF;

            C0186zza(IBinder iBinder) {
                this.zznF = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznF;
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isCompassEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isIndoorLevelPickerEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isMapToolbarEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isMyLocationButtonEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isRotateGesturesEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isScrollGesturesEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isTiltGesturesEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isZoomControlsEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public boolean isZoomGesturesEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    this.zznF.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setAllGesturesEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setCompassEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setIndoorLevelPickerEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setMapToolbarEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setMyLocationButtonEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setRotateGesturesEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setScrollGesturesEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setTiltGesturesEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setZoomControlsEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
            public void setZoomGesturesEnabled(boolean enabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    obtain.writeInt(enabled ? 1 : 0);
                    this.zznF.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IUiSettingsDelegate zzcJ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IUiSettingsDelegate)) ? new C0186zza(iBinder) : (IUiSettingsDelegate) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setZoomControlsEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setCompassEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setMyLocationButtonEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setScrollGesturesEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setZoomGesturesEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setTiltGesturesEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setRotateGesturesEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setAllGesturesEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isZoomControlsEnabled = isZoomControlsEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isZoomControlsEnabled ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isCompassEnabled = isCompassEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isCompassEnabled ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isMyLocationButtonEnabled = isMyLocationButtonEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isMyLocationButtonEnabled ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isScrollGesturesEnabled = isScrollGesturesEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isScrollGesturesEnabled ? 1 : 0);
                    return true;
                case 13:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isZoomGesturesEnabled = isZoomGesturesEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isZoomGesturesEnabled ? 1 : 0);
                    return true;
                case 14:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isTiltGesturesEnabled = isTiltGesturesEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isTiltGesturesEnabled ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isRotateGesturesEnabled = isRotateGesturesEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isRotateGesturesEnabled ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setIndoorLevelPickerEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isIndoorLevelPickerEnabled = isIndoorLevelPickerEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isIndoorLevelPickerEnabled ? 1 : 0);
                    return true;
                case 18:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    setMapToolbarEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 19:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    boolean isMapToolbarEnabled = isMapToolbarEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isMapToolbarEnabled ? 1 : 0);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.internal.IUiSettingsDelegate");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean isCompassEnabled() throws RemoteException;

    boolean isIndoorLevelPickerEnabled() throws RemoteException;

    boolean isMapToolbarEnabled() throws RemoteException;

    boolean isMyLocationButtonEnabled() throws RemoteException;

    boolean isRotateGesturesEnabled() throws RemoteException;

    boolean isScrollGesturesEnabled() throws RemoteException;

    boolean isTiltGesturesEnabled() throws RemoteException;

    boolean isZoomControlsEnabled() throws RemoteException;

    boolean isZoomGesturesEnabled() throws RemoteException;

    void setAllGesturesEnabled(boolean z) throws RemoteException;

    void setCompassEnabled(boolean z) throws RemoteException;

    void setIndoorLevelPickerEnabled(boolean z) throws RemoteException;

    void setMapToolbarEnabled(boolean z) throws RemoteException;

    void setMyLocationButtonEnabled(boolean z) throws RemoteException;

    void setRotateGesturesEnabled(boolean z) throws RemoteException;

    void setScrollGesturesEnabled(boolean z) throws RemoteException;

    void setTiltGesturesEnabled(boolean z) throws RemoteException;

    void setZoomControlsEnabled(boolean z) throws RemoteException;

    void setZoomGesturesEnabled(boolean z) throws RemoteException;
}
