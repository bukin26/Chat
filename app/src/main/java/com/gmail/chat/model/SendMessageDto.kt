package com.gmail.chat.model

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload