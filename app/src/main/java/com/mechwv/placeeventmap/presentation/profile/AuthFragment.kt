package com.mechwv.placeeventmap.presentation.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.databinding.AuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var binding: AuthFragmentBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AuthFragmentBinding.inflate(layoutInflater, container, false)
//        viewModel.setAdmin()
        viewModel.getProfile().observe(viewLifecycleOwner) { profile ->
            if (profile?.client_id != null) {
                val action = AuthFragmentDirections.actionAuthFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }
        viewModel.getOauthUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.e("USER TOKEN", user.jwtToken.toString())
                viewModel.setCurrentUser(user)
                if (user.jwtToken != null) {

                    GlobalScope.launch(Dispatchers.Main) {
                        delay(2000L)
//                        if (viewModel.getProfile().value == null) {
//                            Toast.makeText(context, "PLZ ENABLE INTERNET", Toast.LENGTH_LONG).show()
//                        }
                    }

                    val token = user.jwtToken!!
                    viewModel.getProfileByToken(token).observe(viewLifecycleOwner) { profile ->
                        if (profile?.client_id != null) {
                            viewModel.setProfile(profile)
                        }
                    }
                }
            }
        }

//        GlobalScope.launch(Dispatchers.Main) {
//            delay(1000L)
//            Log.e("HELP1", viewModel.getProfile().value.toString())
//            if (viewModel.getProfile().value != null) {
//                Toast.makeText(context, "PLZ ENABLE INTERNET", Toast.LENGTH_SHORT).show()
//            }
//        }


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