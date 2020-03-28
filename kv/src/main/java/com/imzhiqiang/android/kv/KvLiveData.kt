package com.imzhiqiang.android.kv

import androidx.lifecycle.LiveData

abstract class KvLiveData<T>(
    protected val kv: Kv,
    private val key: String,
    private val defValue: T
) : LiveData<T>() {

    private val onKvChangeListener = { _: Kv, k: String ->
        if (k == key) {
            value = getValueFromKv(key, defValue)
        }
    }

    abstract fun getValueFromKv(key: String, defValue: T): T

    override fun onActive() {
        super.onActive()
        value = getValueFromKv(key, defValue)
        kv.addOnKvChangeListener(onKvChangeListener)
    }

    override fun onInactive() {
        kv.removeOnKvChangeListener(onKvChangeListener)
        super.onInactive()
    }
}

class KvStringLiveData(kv: Kv, key: String, defValue: String?) :
    KvLiveData<String?>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: String?): String? =
        kv.getString(key, defValue)
}

class KvStringSetLiveData(kv: Kv, key: String, defValue: Set<String>?) :
    KvLiveData<Set<String>?>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: Set<String>?): Set<String>? =
        kv.getStringSet(key, defValue)
}

class KvStringArrayLiveData(kv: Kv, key: String, defValue: Array<String?>?) :
    KvLiveData<Array<String?>?>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: Array<String?>?): Array<String?>? =
        kv.getStringArray(key, defValue)
}

class KvIntLiveData(kv: Kv, key: String, defValue: Int) :
    KvLiveData<Int>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: Int): Int = kv.getInt(key, defValue)
}

class KvLongLiveData(kv: Kv, key: String, defValue: Long) :
    KvLiveData<Long>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: Long): Long = kv.getLong(key, defValue)
}

class KvFloatLiveData(kv: Kv, key: String, defValue: Float) :
    KvLiveData<Float>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: Float): Float = kv.getFloat(key, defValue)
}

class KvBooleanLiveData(kv: Kv, key: String, defValue: Boolean) :
    KvLiveData<Boolean>(kv, key, defValue) {

    override fun getValueFromKv(key: String, defValue: Boolean): Boolean =
        kv.getBoolean(key, defValue)
}


