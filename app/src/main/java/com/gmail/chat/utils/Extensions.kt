package com.gmail.chat.utils

import com.gmail.chat.model.*
import com.google.gson.Gson
import java.util.*

object Extensions {

    fun String.toBaseDto(): BaseDto {
        return Gson().fromJson(this, BaseDto::class.java)
    }

    fun BaseDto.toConnectedDto(): ConnectedDto {
        return Gson().fromJson(this.payload, ConnectedDto::class.java)
    }

    fun BaseDto.toUsersList(): List<User> {
        val usersDto = Gson().fromJson(this.payload, UsersReceivedDto::class.java)
        return usersDto.users
    }

    fun BaseDto.toMessage(): Message {
        val messageDto = Gson().fromJson(this.payload, MessageDto::class.java)
        return Message(
            text = messageDto.message,
            senderId = messageDto.from.id,
            name = messageDto.from.name,
            isMessageIsMy = false,
            date = Date()
        )
    }
}