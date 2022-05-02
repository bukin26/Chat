package com.gmail.chat.presentation.userslist

import androidx.lifecycle.ViewModel
import com.gmail.chat.data.Repository
import com.gmail.chat.utils.MySharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val repository: Repository,
    private val prefs: MySharedPreferences
) : ViewModel() {

    val usersList = repository.usersList

    fun disconnect() {
        repository.disconnect()
        prefs.removeUserName()
    }

    fun refreshUsersList() {
        repository.getUsersList()
    }
}