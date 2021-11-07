package com.mechwv.placeeventmap.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.databinding.AuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var binding: AuthFragmentBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AuthFragmentBinding.inflate(layoutInflater, container, false)
        viewModel.setAdmin()

        viewModel.getProfile().observe(viewLifecycleOwner, { profile ->
            if (profile?.client_id != null) {
                val action = AuthFragmentDirections.actionAuthFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        })

        binding.register.setOnClickListener {

        }

        binding.login.setOnClickListener {
            viewModel.getUser(binding.editTextTextEmail.text.toString(),
                binding.editTextTextPassword.text.toString())
                .observe(viewLifecycleOwner, { user ->
                    if (user != null) {
                        Toast.makeText(context, user.role, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "WRONG USERNAME/PASSWORD", Toast.LENGTH_SHORT).show()
                    }
            })
        }


        binding.yandexOauth.setOnClickListener {
            val action = AuthFragmentDirections.actionProfileFragment2ToWebFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}