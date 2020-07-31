package com.allever.app.demo.binder;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

public class BookManagerProxy implements IBookManager {
    private IBinder mRemote;

    public BookManagerProxy(IBinder remote) {
        mRemote = remote;
    }

    @Override
    public IBinder asBinder() {
        return mRemote;
    }

    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        List<Book> result = null;
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
            reply.readException();
            result = reply.createTypedArrayList(Book.CREATOR);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reply.recycle();
            data.recycle();
        }


        return result;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (book != null) {
                data.writeInt(1);
                book.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reply.recycle();
            data.recycle();
        }

    }


}
