package com.gmail.chat.data

import com.gmail.chat.model.BaseDto
import com.gmail.chat.utils.Constants
import com.gmail.chat.utils.Extensions.toBaseDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketHandler @Inject constructor() {

    private var isConnected = false
    private val _connectionState = MutableStateFlow(isConnected)
    val connectionState: StateFlow<Boolean>
        get() = _connectionState
    private val _eventsFlow = MutableSharedFlow<BaseDto>()
    val eventsFlow: SharedFlow<BaseDto>
        get() = _eventsFlow
    private var socket: Socket? = null
    private var input: BufferedReader? = null
    private var output: PrintWriter? = null

    suspend fun disconnect() {
        isConnected = false
        _connectionState.emit(isConnected)
        socket = null
        input?.close()
        output?.close()
        input = null
        output = null
    }

    fun sendBaseDto(message: String) {
        output?.println(message)
        output?.flush()
    }

    suspend fun connect() {
        val datagramSocket = DatagramSocket()
        val buffer = ByteArray(1500)
        var packet =
            DatagramPacket(
                buffer,
                buffer.size,
                InetAddress.getByName(Constants.EMULATOR_INET_ADDRESS),
                //InetAddress.getByName(Constants.DEVICE_INET_ADDRESS),
                Constants.UDP_PORT
            )
        datagramSocket.send(packet)
        packet = DatagramPacket(
            buffer,
            buffer.size
        )
        datagramSocket.receive(packet)
        socket = Socket(packet.address.hostAddress, Constants.TCP_PORT)
        output = PrintWriter(OutputStreamWriter(socket?.getOutputStream()))
        input = BufferedReader(InputStreamReader(socket?.getInputStream()))
        isConnected = true
        _connectionState.emit(isConnected)
        while (isConnected) {
            val response = input?.readLine()
            if (!response.isNullOrBlank()) {
                _eventsFlow.emit(response.toBaseDto())
            }
        }
    }
}
