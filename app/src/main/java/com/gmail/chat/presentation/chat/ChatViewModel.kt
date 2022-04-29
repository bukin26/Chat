package com.gmail.chat.presentation.chat

import androidx.lifecycle.ViewModel
import com.gmail.chat.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val messages = repository.messages

    fun sendMessage(receiverId: String, text: String) {
        repository.sendMessage(receiverId, text)
    }

    fun disconnect() {
        repository.disconnect()
    }
}