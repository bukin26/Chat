package com.gmail.chat.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.chat.R
import com.gmail.chat.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()
    private val adapter = MessageAdapter()

    @Inject
    lateinit var viewModelFactory: ChatViewModelFactory.Factory
    private val viewModel: ChatViewModel by viewModels(factoryProducer = {
        viewModelFactory.create(args.id)
    })

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
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.log_out -> {
                        viewModel.disconnect()
                        findNavController().popBackStack(R.id.loginFragment, false)
                        true
                    }
                    else -> false
                }
            }
            buttonSend.setOnClickListener {
                val text = editText.text.toString()
                if (text.isNotBlank()) {
                    viewModel.sendMessage(text)
                    editText.text.clear()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.messagesListFlow.collect {
                adapter.submitList(it)
            }
        }
    }
}