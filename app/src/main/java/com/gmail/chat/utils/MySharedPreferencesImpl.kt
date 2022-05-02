package com.gmail.chat.utils

import android.content.Context
import com.gmail.data.MySharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MySharedPreferencesImpl @Inject constructor(@ApplicationContext context: Context) :
    MySharedPreferences {

    private val prefs = context.getSharedPreferences(
        Constants.PACKAGE_NAME, Context.MODE_PRIVATE
    )

    override fun getUserName(): String? {
        return prefs.getString(Constants.KEY_USER_NAME, null)
    }

    override fun saveUserName(userName: String) {
        prefs.edit().putString(Constants.KEY_USER_NAME, userName).apply()
    }

    override fun removeUserName() {
        prefs.edit().remove(Constants.KEY_USER_NAME).apply()
    }
}