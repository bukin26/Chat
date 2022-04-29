package com.gmail.chat.data

import com.gmail.chat.model.BaseDto
import com.gmail.chat.utils.Constants
import com.gmail.chat.utils.Extensions.toBaseDto
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

@Singleton
class SocketHandler @Inject constructor() {

    private val _eventsFlow = MutableSharedFlow<BaseDto>()
    val eventsFlow: SharedFlow<BaseDto>
        get() = _eventsFlow
    private lateinit var socket: Socket
    private lateinit var input: BufferedReader
    private lateinit var output: PrintWriter

    fun sendBaseDto(message: String) {
        output.println(message)
        output.flush()
    }

    suspend fun connect() {
        val datagramSocket = DatagramSocket()
        val buffer = ByteArray(1500)
        var packet =
            DatagramPacket(
                buffer,
                buffer.size,
                InetAddress.getByName("10.0.2.2"),
                //InetAddress.getByName("255.255.255.255"),
                Constants.UDP_PORT
            )
        datagramSocket.send(packet)
        packet = DatagramPacket(
            buffer,
            buffer.size
        )
        datagramSocket.receive(packet)
        socket = Socket(packet.address.hostAddress, Constants.TCP_PORT)
        output = PrintWriter(OutputStreamWriter(socket.getOutputStream()))
        input = BufferedReader(InputStreamReader(socket.getInputStream()))
        while (true) {
            val response = input.readLine()
            if (!response.isNullOrBlank()) {
                _eventsFlow.emit(response.toBaseDto())
            }
        }
    }
}
