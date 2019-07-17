package com.aidl.binder.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IService extends IInterface {
    /**
     * 进程间，调用的方法
     */
    void callMethodInService() throws RemoteException;

    /* ----------------------------以下内容，默认系统实现---------------------------------- */
    public static abstract class Stub extends Binder implements IService {
        private static final java.lang.String DESCRIPTOR = "com.aidl.binder.binder.IService";

        static final int TRANSACTION_callMethodInService = (FIRST_CALL_TRANSACTION + 0);

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IService asInterface(IBinder obj) {
            if (null == obj) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IService) {
                return (IService) iin;
            }
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(DESCRIPTOR);
                    return true;
                case TRANSACTION_callMethodInService:
                    data.enforceInterface(descriptor);
                    this.callMethodInService();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IService {
            private IBinder mRemote;

            public Proxy(IBinder obj) {
                this.mRemote = obj;
            }

            @Override
            public void callMethodInService() throws RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();

                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_callMethodInService, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }
        }
    }
}
