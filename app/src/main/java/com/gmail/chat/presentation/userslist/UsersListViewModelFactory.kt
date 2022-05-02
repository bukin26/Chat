package com.gmail.chat.presentation.userslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.chat.data.Repository
import com.gmail.chat.utils.MySharedPreferences

@Suppress("UNCHECKED_CAST")
class UsersListViewModelFactory(
    private val repository: Repository, private val prefs: MySharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            UsersListViewModel::class.java -> UsersListViewModel(repository, prefs) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}