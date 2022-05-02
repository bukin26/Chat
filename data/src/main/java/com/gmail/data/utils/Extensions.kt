package com.gmail.data.utils

import com.google.gson.Gson
import java.util.*

object Extensions {

    fun String.toBaseDto(): com.gmail.data.model.BaseDto {
        return Gson().fromJson(this, com.gmail.data.model.BaseDto::class.java)
    }

    fun com.gmail.data.model.BaseDto.toConnectedDto(): com.gmail.data.model.ConnectedDto {
        return Gson().fromJson(this.payload, com.gmail.data.model.ConnectedDto::class.java)
    }

    fun com.gmail.data.model.BaseDto.toUsersList(): List<com.gmail.data.model.User> {
        val usersDto =
            Gson().fromJson(this.payload, com.gmail.data.model.UsersReceivedDto::class.java)
        return usersDto.users
    }

    fun com.gmail.data.model.BaseDto.toMessage(): com.gmail.data.model.Message {
        val messageDto = Gson().fromJson(this.payload, com.gmail.data.model.MessageDto::class.java)
        return com.gmail.data.model.Message(
            text = messageDto.message,
            id = messageDto.from.id,
            name = messageDto.from.name,
            isMessageIsMy = false,
            date = Date()
        )
    }
}