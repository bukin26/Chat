package com.gmail.chat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.chat.data.Repository
import com.gmail.chat.utils.MySharedPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory @AssistedInject constructor(
    private val repository: Repository,
    @Assisted("id") private val id: String,
    @Assisted("prefs") private val prefs: MySharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ChatViewModel::class.java -> ChatViewModel(repository, id, prefs) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("id") id: String,
            @Assisted("prefs") prefs: MySharedPreferences
        ): ChatViewModelFactory
    }
}