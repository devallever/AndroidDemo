package com.allever.app.demo.memoryleak

import android.content.Context

object MemoryLeakSingleton {
    private var mContext: Context? = null
    private val mListeners = mutableListOf<Observer>()

    fun init(context: Context) {
        mContext = context

        //解决内存泄漏
        mContext = context.applicationContext
    }

    fun addObserver(observer: Observer) {
        mListeners.add(observer)
    }

    fun removeObserver(observer: Observer) {
        mListeners.remove(observer)
    }

    interface Observer{
        fun testFun()
    }
}