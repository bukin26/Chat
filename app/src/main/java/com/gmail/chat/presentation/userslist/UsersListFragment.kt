package com.gmail.chat.presentation.userslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.chat.databinding.FragmentUsersListBinding
import com.gmail.chat.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding
    private val viewModel: UsersListViewModel by viewModels()
    private val adapter = UsersAdapter { user -> adapterOnClick(user) }

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
        }
        lifecycleScope.launch {
            viewModel.usersList.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun adapterOnClick(user: User) {
        val directions = UsersListFragmentDirections.actionUsersListFragmentToChatFragment(user.id)
        findNavController().navigate(directions)
    }
}