package com.gmail.chat.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.chat.data.Repository
import com.gmail.chat.utils.MySharedPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory @AssistedInject constructor(
    private val repository: Repository,
    @Assisted("prefs") private val prefs: MySharedPreferences,
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
            @Assisted("prefs") prefs: MySharedPreferences,
            @Assisted("navigate") navigate: (String) -> Unit
        ): LoginViewModelFactory
    }
}