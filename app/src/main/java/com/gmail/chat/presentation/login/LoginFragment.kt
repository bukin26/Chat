package com.gmail.chat.presentation.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gmail.chat.R
import com.gmail.chat.databinding.FragmentLoginBinding
import com.gmail.chat.utils.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        SharedPreferencesUtil.getUserName(requireContext())?.let {
            viewModel.connect(it)
            navigateToUsersList(it)
        }
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonLogin.setOnClickListener {
                val name = edittextName.text.toString()
                if (name.isBlank()) {
                    textLayoutName.error = resources.getString(R.string.must_be_filled)
                    textLayoutName.errorIconDrawable = null
                } else {
                    SharedPreferencesUtil.saveUserName(requireContext(), name)
                    viewModel.connect(name)
                    navigateToUsersList(name)
                }
            }
        }
    }

    private fun navigateToUsersList(name: String) {
        val directions = LoginFragmentDirections.actionLoginFragmentToUsersListFragment(name)
        findNavController().navigate(directions)
    }
}