package com.gmail.chat.presentation.login

import androidx.lifecycle.ViewModel
import com.gmail.chat.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun connect(name: String) {
        repository.connect(name)
    }
}