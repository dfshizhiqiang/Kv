package com.imzhiqiang.android.kv

import android.content.Context
import android.content.SharedPreferences

internal class KvImpl(private val kvName: String) : Kv() {

    override val name: String
        get() = kvName

    var delegateKv: Kv = SharedPreferencesImpl(name)

    override fun putString(key: String, value: String?) {
        delegateKv.putString(key, value)
    }

    override fun putStringSet(key: String, values: Set<String>?) {
        delegateKv.putStringSet(key, values)
    }

    override fun putStringArray(key: String, values: Array<String?>?) {
        delegateKv.putStringArray(key, values)
    }

    override fun putInt(key: String, value: Int) {
        delegateKv.putInt(key, value)
    }

    override fun putLong(key: String, value: Long) {
        delegateKv.putLong(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        delegateKv.putFloat(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        delegateKv.putBoolean(key, value)
    }

    override fun remove(key: String) {
        delegateKv.remove(key)
    }

    override fun getString(key: String, defValue: String?): String? =
        delegateKv.getString(key, defValue)

    override fun getStringArray(key: String, defValues: Array<String?>?): Array<String?>? =
        delegateKv.getStringArray(key, defValues)

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? =
        delegateKv.getStringSet(key, defValues)

    override fun getInt(key: String, defValue: Int): Int = delegateKv.getInt(key, defValue)

    override fun getLong(key: String, defValue: Long): Long = delegateKv.getLong(key, defValue)

    override fun getFloat(key: String, defValue: Float): Float = delegateKv.getFloat(key, defValue)

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        delegateKv.getBoolean(key, defValue)

    override fun containsKey(key: String): Boolean = delegateKv.containsKey(key)

    override fun addOnKvChangeListener(listener: OnKvChangeListener) {
        delegateKv.addOnKvChangeListener(listener)
    }

    override fun removeOnKvChangeListener(listener: OnKvChangeListener) {
        delegateKv.removeOnKvChangeListener(listener)
    }
}

internal class SharedPreferencesImpl(private val fileName: String) : Kv() {

    override val name: String
        get() = fileName

    private val sharedPreferences =
        KvCache.requireContext().getSharedPreferences(name, Context.MODE_PRIVATE)

    private val kvListenerMap = hashMapOf<OnKvChangeListener, KvPreferencesListener>()

    override fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun putStringSet(key: String, values: Set<String>?) {
        sharedPreferences.edit().putStringSet(key, values).apply()
    }

    override fun putStringArray(key: String, values: Array<String?>?) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, values?.contentHashCode() ?: 0)
        editor.putInt("${key}_size", values?.size ?: 0)
        values?.forEachIndexed { index, value ->
            editor.putString("${key}_${index}", value)
        }
        editor.apply()
    }

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    override fun putFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun getString(key: String, defValue: String?): String? =
        sharedPreferences.getString(key, defValue)

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? =
        sharedPreferences.getStringSet(key, defValues)

    override fun getStringArray(key: String, defValues: Array<String?>?): Array<String?>? {
        val size: Int = sharedPreferences.getInt(key + "${key}_size", 0)
        val array = arrayListOf<String?>()
        for (index in 0 until size) {
            array.add(sharedPreferences.getString("${key}_${index}", null))
        }
        return array.toTypedArray()
    }

    override fun getInt(key: String, defValue: Int): Int = sharedPreferences.getInt(key, defValue)

    override fun getLong(key: String, defValue: Long): Long =
        sharedPreferences.getLong(key, defValue)

    override fun getFloat(key: String, defValue: Float): Float =
        sharedPreferences.getFloat(key, defValue)

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defValue)

    override fun containsKey(key: String): Boolean = sharedPreferences.contains(key)

    override fun addOnKvChangeListener(listener: OnKvChangeListener) {
        synchronized(kvListenerMap) {
            val kvPreferencesListener = KvPreferencesListener(listener)
            kvListenerMap[listener] = kvPreferencesListener
            sharedPreferences.registerOnSharedPreferenceChangeListener(kvPreferencesListener)
        }
    }

    override fun removeOnKvChangeListener(listener: OnKvChangeListener) {
        synchronized(kvListenerMap) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(kvListenerMap[listener])
            kvListenerMap.remove(listener)
        }
    }

    private inner class KvPreferencesListener(
        private val kvChangeListener: OnKvChangeListener
    ) : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences,
            key: String
        ) {
            kvChangeListener.invoke(this@SharedPreferencesImpl, key)
        }
    }

}