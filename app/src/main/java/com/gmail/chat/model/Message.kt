package com.gmail.chat.model

import java.util.*

data class Message(
    val text: String,
    val senderId: String,
    val name: String,
    val isMessageIsMy: Boolean,
    val date: Date
)