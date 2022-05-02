package com.gmail.data

interface MySharedPreferences {
    fun getUserName(): String?
    fun saveUserName(userName: String)
    fun removeUserName()
}