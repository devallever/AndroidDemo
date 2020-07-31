package com.allever.app.demo.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * @author allever
 */
public interface IBookManager extends IInterface {

    static final String DESCRIPTOR = "com.allever.app.demo.binder.IBookManager";

    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;

    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;
}
