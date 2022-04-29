package com.gmail.chat.presentation.userslist

import androidx.lifecycle.ViewModel
import com.gmail.chat.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val usersList = repository.usersList

    fun disconnect() {
        repository.disconnect()
    }

    fun refreshUsersList() {
        repository.getUsersList()
    }
}