package Util

import android.content.SharedPreferences

object LoginUtil {

    private const val SHARED_NAME = "Login"

    fun put(key: String?, value: Any?) {
        val preference: SharedPreferences =
            AppContext.instance.getSharedPreferences(SHARED_NAME, 0)
        val editor = preference.edit()
        if (value is String) {
            editor.putString(key, value as String?)
        } else if (value is Int) {
            editor.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float?)!!)
        } else if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean?)!!)
        }
        editor.commit()
    }

    operator fun get(key: String?, defaultValue: Any?): Any? {
        val preference: SharedPreferences =
            AppContext.instance.getSharedPreferences(SHARED_NAME, 0)
        return if (defaultValue is String) {
            preference.getString(key, defaultValue as String?)
        } else if (defaultValue is Int) {
            preference.getInt(key, (defaultValue as Int?)!!)
        } else if (defaultValue is Float) {
            preference.getFloat(key, (defaultValue as Float?)!!)
        } else if (defaultValue is Boolean) {
            preference.getBoolean(key, (defaultValue as Boolean?)!!)
        } else {
            //            return preference.get(key, (Object) defaultValue);
            throw RuntimeException("错误类型")
        }
    }

    fun clear() {
        val preference: SharedPreferences =
            AppContext.instance.getSharedPreferences(SHARED_NAME, 0)
        val editor = preference.edit()
        editor.clear().apply()
    }
}