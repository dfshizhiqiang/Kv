package com.imzhiqiang.android.kv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Kv.default().putStringArray("test", arrayOf("1","2","3","4","5","6","9"))
    }
}
