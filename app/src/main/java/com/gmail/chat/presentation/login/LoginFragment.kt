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
import com.gmail.chat.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory.Factory
    private val prefs: MySharedPreferences by lazy {
        MySharedPreferences.getInstance(requireContext())
    }
    private val viewModel: LoginViewModel by viewModels(factoryProducer = {
        viewModelFactory.create(prefs) {
            navigateToUsersList(it)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.chekIsUserAlreadyLoggedIn()
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
                    viewModel.connect(name)
                    viewModel.saveUserName(name)
                }
            }
        }
    }

    private fun navigateToUsersList(name: String) {
        val directions = LoginFragmentDirections.actionLoginFragmentToUsersListFragment(name)
        findNavController().navigate(directions)
    }
}