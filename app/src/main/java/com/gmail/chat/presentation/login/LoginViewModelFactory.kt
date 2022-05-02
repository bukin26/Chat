package com.gmail.chat.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.data.Repository
import com.gmail.data.MySharedPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory @AssistedInject constructor(
    private val repository: Repository,
    private val prefs: MySharedPreferences,
    @Assisted("navigate") private val navigate: (String) -> Unit
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(repository, prefs, navigate) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("navigate") navigate: (String) -> Unit
        ): LoginViewModelFactory
    }
}