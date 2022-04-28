package com.gmail.chat.presentation.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gmail.chat.databinding.ItemGetMessageBinding
import com.gmail.chat.databinding.ItemSendMessageBinding
import com.gmail.chat.model.Message
import java.text.SimpleDateFormat

private const val SEND_MESSAGE_TYPE = 1
private const val GET_MESSAGE_TYPE = 2

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(
    ItemDiffCallback
) {

    abstract class MessageViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(message: Message)
    }

    class SentMessageViewHolder(private val binding: ItemSendMessageBinding) :
        MessageViewHolder(binding) {
        @SuppressLint("SimpleDateFormat")
        override fun bind(message: Message) {
            Log.d("TAG", "bind: ")
            with(binding) {
                textMessageSend.text = message.text
                val dateFormatter = SimpleDateFormat("HH:mm MMM d")
                textDateSend.text = dateFormatter.format(message.date)
            }
        }
    }

    class GetMessageViewHolder(private val binding: ItemGetMessageBinding) :
        MessageViewHolder(binding) {
        @SuppressLint("SimpleDateFormat")
        override fun bind(message: Message) {
            with(binding) {
                Log.d("TAG", "bind: ")
                textMessageGet.text = message.text
                val dateFormatter = SimpleDateFormat("HH:mm MMM d")
                textDateGet.text = dateFormatter.format(message.date)
                textUserNameGet.text = message.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == SEND_MESSAGE_TYPE) {
            SentMessageViewHolder(
                ItemSendMessageBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
        } else {
            GetMessageViewHolder(
                ItemGetMessageBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.isMessageIsMy) SEND_MESSAGE_TYPE else GET_MESSAGE_TYPE
    }
}

object ItemDiffCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
