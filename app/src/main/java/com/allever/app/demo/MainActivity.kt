package com.allever.app.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.allever.app.demo.memoryleak.MemoryLeakActivity
import com.allever.app.demo.util.ActivityCollector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartActivity.setOnClickListener {
            ActivityCollector.startActivity(this, MemoryLeakActivity::class.java)
        }

    }
}