package com.imzhiqiang.android.kv

import android.content.Context

internal object KvCache {

    var appContext: Context? = null
        set(value) {
            field = value?.applicationContext ?: value
        }

    @JvmStatic
    private val kvMap = HashMap<String, Kv>()

    fun requireContext(): Context {
        return appContext
            ?: throw RuntimeException("Did you initialize Kv in your Application class?")
    }

    @Synchronized
    fun getKv(name: String): Kv {
        var kv = kvMap[name]
        if (kv == null) {
            kv = KvImpl(name)
            kvMap[name] = kv
        }
        return kv
    }

}