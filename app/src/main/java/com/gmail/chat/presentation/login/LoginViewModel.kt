package com.gmail.chat.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.chat.data.Repository
import com.gmail.chat.utils.MySharedPreferences
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: Repository,
    private val prefs: MySharedPreferences,
    private val navigate: (String) -> Unit
) : ViewModel() {

    init {
        viewModelScope.launch() {
            repository.connectionState.collect { connected ->
                if (connected) {
                    prefs.getUserName()?.let { name -> navigate(name) }
                }
            }
        }
    }

    fun connect(name: String) {
        repository.connect(name)
    }

    fun saveUserName(name: String) {
        prefs.saveUserName(name)
    }

    fun chekIsUserAlreadyLoggedIn() {
        prefs.getUserName()?.let { name -> connect(name) }
    }
}