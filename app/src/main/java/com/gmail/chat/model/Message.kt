package com.gmail.chat.model

import java.util.*

data class Message(
    val text: String,
    val id: String,
    val name: String,
    val isMessageIsMy: Boolean,
    val date: Date
)