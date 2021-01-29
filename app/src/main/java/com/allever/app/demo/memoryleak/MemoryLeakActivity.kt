package com.allever.app.demo.memoryleak

import android.animation.ObjectAnimator
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.allever.app.demo.R
import allever.android.lib.compent.common.util.log

class MemoryLeakActivity: AppCompatActivity(), MemoryLeakSingleton.Observer {
    private val mHandler = Handler()

    private lateinit var animator: ObjectAnimator
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_leak)

        //内存泄漏场景1
        val leakInnerClass = LeakInnerClass()
        leakInnerClass.startTask()

        //内存泄漏场景2-单例持有Activity全局Context,
        //解决办法使用ApplicationContext
        //MemoryLeakSingleton.init(this)
        MemoryLeakSingleton.init(applicationContext)

        //内存泄漏场景3-单例持有Activity实现的监听器，没有取消注册
        //解决办法-移除监听
        MemoryLeakSingleton.addObserver(this)

        //内存泄漏场景4-AsyncTask导致的内存泄漏-本质同非静态内部类
        //解决办法，取消任务 或者使用静态内部类方式
        task = Task()
        task.execute()

        Toast.makeText(this, "Hello LeakCanary", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        MemoryLeakSingleton.removeObserver(this)
        task.cancel(true)
    }

    inner class LeakInnerClass {
        fun startTask() {
            Thread(
                Runnable {
                    log("线程开始启动")
                    Thread.sleep(60 * 1000)
                    log("线程睡60秒完成")
                }
            ).start()
        }
    }

    //class Task: AsyncTask<Void, Void, Void>() {
    inner class Task: AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            log("执行Task")
            Thread.sleep(60_000)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            log("finish task")
        }
    }

    override fun testFun() {

    }
}