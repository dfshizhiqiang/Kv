package com.imzhiqiang.android.kv

interface KvEncoder {

    fun putString(key: String, value: String?)

    fun putStringSet(key: String, values: Set<String>?)

    fun putInt(key: String, value: Int)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putBoolean(key: String, value: Boolean)

    fun remove(key: String)
}