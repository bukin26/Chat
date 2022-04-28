package com.gmail.chat.data

import android.util.Log
import com.gmail.chat.model.BaseDto
import com.gmail.chat.model.UdpDto
import com.gmail.chat.utils.Extensions.toBaseDto
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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

private const val UDP_PORT = 8888
private const val TCP_PORT = 6666
private const val PING_DELAY = 1000L

private const val TAG = "SocketHandler"

@Singleton
class SocketHandler @Inject constructor() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _eventsFlow = MutableSharedFlow<BaseDto>()
    val eventsFlow: SharedFlow<BaseDto>
        get() = _eventsFlow

    private lateinit var socket: Socket
    private lateinit var input: BufferedReader
    private lateinit var output: PrintWriter

    

    fun disconnect(message: String) {
        scope.launch {
            output.println(message)
            output.flush()
        }
    }

    fun sendMessage(message: String) {
        scope.launch {
            output.println(message)
            output.flush()
        }
    }

    fun getUsersList(message: String) {
        scope.launch {
            output.println(message)
            output.flush()
        }
    }

    fun ping(message: String) {
        scope.launch {
            delay(PING_DELAY)
            output.println(message)
            output.flush()
        }
    }

    fun logIn(message: String) {
        scope.launch {
            output.println(message)
            output.flush()
        }
    }

    fun connect() {
        scope.launch {
            val datagramSocket = DatagramSocket()
            val buffer = ByteArray(1500)
            var packet =
                DatagramPacket(
                    buffer,
                    buffer.size,
                    //emulator
                    InetAddress.getByName("10.0.2.2"),
                    //InetAddress.getByName("255.255.255.255"),
                    UDP_PORT
                )
            datagramSocket.send(packet)
            packet = DatagramPacket(
                buffer,
                buffer.size
            )
            datagramSocket.receive(packet)
            val serverAddress = packet.address.hostAddress
            Log.d(TAG, "connect: $serverAddress")
            socket = Socket(serverAddress, TCP_PORT)
            output = PrintWriter(OutputStreamWriter(socket.getOutputStream()))
            input = BufferedReader(InputStreamReader(socket.getInputStream()))
            Log.d(TAG, "connect: ")
            while (true) {
                val response = input.readLine()
                if (!response.isNullOrBlank()) {
                    _eventsFlow.emit(response.toBaseDto())
                }
            }
        }
    }
}
