package com.allever.app.demo.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author allever
 */
public class BookManagerImpl extends Binder implements IBookManager{

    public BookManagerImpl() {
        //用于将特定接口与Binder关联
        //调用之后，将为您实现queryLocalInterface（），
        // 以便在请求相应的描述符时返回给定的所有者IInterface。
        attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder binder) {
        if (binder == null) {
            return null;
        }

        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
        if (iInterface != null && iInterface instanceof IBookManager) {
            return (IBookManager) iInterface;
        }

        return new BookManagerProxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if (data.readInt() != 0) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.addBook(book);
                reply.writeNoException();
                return true;
            }
            default:
                break;
        }

        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }


}
