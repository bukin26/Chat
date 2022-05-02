package com.gmail.chat.utils

import android.content.Context

class MySharedPreferences private constructor(context: Context) {

    private val prefs = context.getSharedPreferences(
        Constants.PACKAGE_NAME, Context.MODE_PRIVATE
    )

    fun getUserName(): String? {
        return prefs.getString(Constants.KEY_USER_NAME, null)
    }

    fun saveUserName(userName: String) {
        prefs.edit().putString(Constants.KEY_USER_NAME, userName).apply()
    }

    fun removeUserName() {
        prefs.edit().remove(Constants.KEY_USER_NAME).apply()
    }

    companion object {

        @Volatile
        private var INSTANCE: MySharedPreferences? = null

        fun getInstance(context: Context): MySharedPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySharedPreferences(context).also { INSTANCE = it }
            }
        }
    }
}