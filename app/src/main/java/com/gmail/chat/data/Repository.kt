package com.gmail.chat.data

import com.gmail.chat.model.*
import com.gmail.chat.utils.Constants
import com.gmail.chat.utils.Extensions.toConnectedDto
import com.gmail.chat.utils.Extensions.toMessage
import com.gmail.chat.utils.Extensions.toUsersList
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

interface Repository {

    val connectionState: StateFlow<Boolean>
    val messages: SharedFlow<Message>
    val usersList: SharedFlow<List<User>>

    fun disconnect()
    fun sendMessage(id: String, text: String)
    fun connect(name: String)
    fun getUsersList()
}

class RepositoryImpl @Inject constructor(
    private val socketHandler: SocketHandler,
    private val scope: CoroutineScope
) : Repository {

    override val connectionState = socketHandler.connectionState
    private var currentUserId = ""
    private var currentUserName = ""
    private val _usersListFlow = MutableSharedFlow<List<User>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val usersList: SharedFlow<List<User>>
        get() = _usersListFlow
    private val _messagesFlow = MutableSharedFlow<Message>(
        replay = 100,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val messages: SharedFlow<Message>
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
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        setUsers(it)
                    }
                    BaseDto.Action.NEW_MESSAGE -> {
                        getUsersList()
                        _messagesFlow.emit(it.toMessage())
                    }
                    else -> {}
                }
            }
        }
    }

    override fun disconnect() {
        scope.launch {
            val message = formJson(
                BaseDto.Action.DISCONNECT,
                DisconnectDto(currentUserId, 0)
            )
            socketHandler.sendBaseDto(message)
            socketHandler.disconnect()
        }
    }

    override fun sendMessage(id: String, text: String) {
        scope.launch {
            val message = formJson(
                BaseDto.Action.SEND_MESSAGE,
                SendMessageDto(currentUserId, id, text)
            )
            socketHandler.sendBaseDto(message)
            val sentMessage = Message(text, id, currentUserName, true, Date())
            _messagesFlow.emit(sentMessage)
        }
    }

    private suspend fun setUsers(baseDto: BaseDto) {
        _usersListFlow.emit(baseDto.toUsersList())
    }

    override fun getUsersList() {
        scope.launch {
            val message = formJson(BaseDto.Action.GET_USERS, GetUsersDto(currentUserId))
            socketHandler.sendBaseDto(message)
        }
    }

    private fun ping() {
        scope.launch {
            delay(Constants.PING_DELAY)
            val message = formJson(BaseDto.Action.PING, PingDto(currentUserId))
            socketHandler.sendBaseDto(message)
        }
    }

    private fun login() {
        scope.launch {
            val message = formJson(
                BaseDto.Action.CONNECT,
                ConnectDto(currentUserId, currentUserName)
            )
            socketHandler.sendBaseDto(message)
        }
    }

    override fun connect(name: String) {
        scope.launch {
            currentUserName = name
            socketHandler.connect()
        }
    }

    private fun formJson(myAction: BaseDto.Action, myPayload: Payload): String {
        val baseDto = BaseDto(myAction, Gson().toJson(myPayload))
        return Gson().toJson(baseDto)
    }
}

