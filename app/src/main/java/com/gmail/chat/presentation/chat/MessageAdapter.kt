package com.gmail.chat.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gmail.chat.databinding.ItemGetMessageBinding
import com.gmail.chat.databinding.ItemSendMessageBinding
import com.gmail.chat.model.Message
import com.gmail.chat.utils.Constants
import com.gmail.chat.utils.DateUtil

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(
    ItemDiffCallback
) {

    abstract class MessageViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(message: Message)
    }

    class SentMessageViewHolder(private val binding: ItemSendMessageBinding) :
        MessageViewHolder(binding) {
        override fun bind(message: Message) {
            with(binding) {
                textMessageSend.text = message.text
                textDateSend.text = DateUtil.formatDate(message.date)
            }
        }
    }

    class GetMessageViewHolder(private val binding: ItemGetMessageBinding) :
        MessageViewHolder(binding) {
        override fun bind(message: Message) {
            with(binding) {
                textMessageGet.text = message.text
                textDateGet.text = DateUtil.formatDate(message.date)
                textUserNameGet.text = message.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == Constants.SEND_MESSAGE_TYPE) {
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
        return if (getItem(position).isMessageIsMy) {
            Constants.SEND_MESSAGE_TYPE
        } else {
            Constants.GET_MESSAGE_TYPE
        }
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
