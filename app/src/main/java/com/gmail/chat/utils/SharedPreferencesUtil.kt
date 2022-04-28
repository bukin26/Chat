package com.gmail.chat.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    private const val PACKAGE_NAME = "com.gmail.chat.utils"
    private const val KEY_USER_NAME = "user_info"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getUserName(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_NAME, null)
    }

    fun saveUserName(context: Context, userName: String) {
        getPrefs(context).edit().putString(KEY_USER_NAME, userName).apply()
    }

    fun removeUserName(context: Context) {
        getPrefs(context).edit().remove(KEY_USER_NAME).apply()
    }
}