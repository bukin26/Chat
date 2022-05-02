package com.gmail.chat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.chat.data.Repository
import com.gmail.chat.model.Message
import com.gmail.chat.utils.MySharedPreferences
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class ChatViewModel constructor(
    private val repository: Repository,
    private val id: String,
    private val prefs: MySharedPreferences
) : ViewModel() {

    private val messagesList = mutableListOf<Message>()
    private val _messagesListFlow = MutableSharedFlow<List<Message>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messagesListFlow: SharedFlow<List<Message>>
        get() = _messagesListFlow

    init {
        observeMessages()
    }

    fun sendMessage(text: String) {
        repository.sendMessage(id, text)
    }

    fun disconnect() {
        repository.disconnect()
        prefs.removeUserName()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            repository.messages.filter { it.id == id }.collect {
                messagesList.add(it)
                _messagesListFlow.emit(messagesList.toList())
            }
        }
    }
}