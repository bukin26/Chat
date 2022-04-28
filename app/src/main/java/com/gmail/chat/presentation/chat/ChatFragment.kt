package com.gmail.chat.presentation.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.chat.databinding.FragmentChatBinding
import com.gmail.chat.model.Message
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import java.util.*

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private val list = mutableListOf<Message>()
    private val adapter = MessageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        binding.buttonSend.setOnClickListener {
            val text = binding.editText.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendMessage(args.id, text)
                val savedMessage = Message(text, "", "", true, Date())
                list.add(savedMessage)
                adapter.submitList(list.toList())
                binding.editText.text.clear()
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.messages.filter {
                it.senderId == args.id
            }.collect {
                Log.d("TAG", "onViewCreated: ${it.isMessageIsMy}")
                list.add(it)
                adapter.submitList(list.toList())
            }
        }
    }
}