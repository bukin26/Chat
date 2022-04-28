package com.gmail.chat.data

import android.util.Log
import com.gmail.chat.model.*
import com.gmail.chat.utils.Extensions.toConnectedDto
import com.gmail.chat.utils.Extensions.toMessage
import com.gmail.chat.utils.Extensions.toUsersList
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val socketHandler: SocketHandler) {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentUserId = ""
    private var currentUserName = ""

    private val _usersListFlow = MutableSharedFlow<List<User>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val usersList: SharedFlow<List<User>>
        get() = _usersListFlow

    private val _messagesFlow = MutableSharedFlow<Message>()
    val messages: SharedFlow<Message>
        get() = _messagesFlow

    init {
        scope.launch {
            socketHandler.eventsFlow.collect {
                when (it.action) {
                    BaseDto.Action.CONNECTED -> {
                        currentUserId = it.toConnectedDto().id
                        login()
                        ping()
                        getUsersList()
                    }
                    BaseDto.Action.PONG -> {
                        ping()
                        getUsersList()
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        setUsers(it)
                    }
                    BaseDto.Action.NEW_MESSAGE -> {
                        getUsersList()
                        _messagesFlow.emit(it.toMessage())
                    }
                }
            }
        }
    }

    fun disconnect() {
        val baseDto = BaseDto(
            BaseDto.Action.DISCONNECT,
            Gson().toJson(DisconnectDto(currentUserId, 0))
        )
        val message = Gson().toJson(baseDto)
        socketHandler.disconnect(message)
    }

    fun sendMessage(receiverId: String, text: String) {
        val baseDto = BaseDto(
            BaseDto.Action.SEND_MESSAGE,
            Gson().toJson(SendMessageDto(currentUserId, receiverId, text))
        )
        val sentMessage = Gson().toJson(baseDto)
        socketHandler.sendMessage(sentMessage)
    }

    suspend fun setUsers(baseDto: BaseDto) {
        _usersListFlow.emit(baseDto.toUsersList())
    }

    fun getUsersList() {
        val baseDto = BaseDto(BaseDto.Action.GET_USERS, Gson().toJson(PingDto(currentUserId)))
        val message = Gson().toJson(baseDto)
        socketHandler.getUsersList(message)
    }

    fun ping() {
        val baseDto = BaseDto(BaseDto.Action.PING, Gson().toJson(PingDto(currentUserId)))
        val message = Gson().toJson(baseDto)
        socketHandler.ping(message)
    }

    fun login() {
        val baseDto = BaseDto(
            BaseDto.Action.CONNECT,
            Gson().toJson(ConnectDto(currentUserId, currentUserName))
        )
        val message = Gson().toJson(baseDto)
        socketHandler.logIn(message)
    }

    fun connect(name: String) {
        currentUserName = name
        socketHandler.connect()
    }
}

