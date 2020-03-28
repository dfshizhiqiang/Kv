package com.imzhiqiang.android.kv

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData

abstract class Kv : KvEncoder, KvDecoder {

    protected open val name: String = DEFAULT_NAME

    companion object {

        private const val DEFAULT_NAME = "default"

        @JvmStatic
        @Volatile
        private var sIsInitialized = false

        /**
         * 在使用 kv 之前初始化，否则会 Crash
         */
        @SuppressLint("LogNotTimber")
        @JvmStatic
        fun init(appContext: Context) {
            if (sIsInitialized) {
                Log.w("Kv", "Kv has already been initialized!")
            } else {
                sIsInitialized = true
            }
            setAppContext(appContext)
        }

        @JvmStatic
        private fun setAppContext(appContext: Context) {
            KvCache.appContext = appContext
        }

        /**
         * 获取缺省的 kv 对象
         */
        @JvmStatic
        fun default(): Kv = KvCache.getKv(DEFAULT_NAME)

        /**
         * 获取指定名字的 kv 对象
         * @param name kv 对象的名字
         */
        @JvmStatic
        fun with(name: String): Kv = KvCache.getKv(name)
    }

    abstract fun addOnKvChangeListener(listener: OnKvChangeListener)

    abstract fun removeOnKvChangeListener(listener: OnKvChangeListener)

}

typealias OnKvChangeListener = (kv: Kv, key: String) -> Unit

fun Kv.getStringLiveData(key: String, defValue: String?): LiveData<String?> =
    KvStringLiveData(this, key, defValue)

fun Kv.getStringSetLiveData(key: String, defValue: Set<String>?): LiveData<Set<String>?> =
    KvStringSetLiveData(this, key, defValue)

fun Kv.getStringArrayLiveData(key: String, defValue: Array<String?>?): LiveData<Array<String?>?> =
    KvStringArrayLiveData(this, key, defValue)

fun Kv.getIntLiveData(key: String, defValue: Int): LiveData<Int> =
    KvIntLiveData(this, key, defValue)

fun Kv.getLongLiveData(key: String, defValue: Long): LiveData<Long> =
    KvLongLiveData(this, key, defValue)

fun Kv.getFloatLiveData(key: String, defValue: Float): LiveData<Float> =
    KvFloatLiveData(this, key, defValue)

fun Kv.getBooleanLiveData(key: String, defValue: Boolean): LiveData<Boolean> =
    KvBooleanLiveData(this, key, defValue)
