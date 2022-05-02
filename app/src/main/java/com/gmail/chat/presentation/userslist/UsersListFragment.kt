package com.gmail.chat.presentation.userslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.chat.R
import com.gmail.chat.databinding.FragmentUsersListBinding
import com.gmail.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding
    private val adapter = UsersAdapter { user -> adapterOnClick(user) }
    private val viewModel: UsersListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.refresh -> {
                        viewModel.refreshUsersList()
                        true
                    }
                    R.id.log_out -> {
                        viewModel.disconnect()
                        findNavController().popBackStack(R.id.loginFragment, false)
                        true
                    }
                    else -> false
                }
            }
            lifecycleScope.launch {
                viewModel.usersList.collect {
                    adapter.submitList(it)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.disconnect()
            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }

    private fun adapterOnClick(user: User) {
        val directions = UsersListFragmentDirections.actionUsersListFragmentToChatFragment(user.id)
        findNavController().navigate(directions)
    }
}