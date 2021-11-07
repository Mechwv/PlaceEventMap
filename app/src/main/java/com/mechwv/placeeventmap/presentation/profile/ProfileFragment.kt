package com.mechwv.placeeventmap.presentation.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.R
import com.mechwv.placeeventmap.databinding.AuthFragmentBinding
import com.mechwv.placeeventmap.databinding.ProfileFragmentBinding
import com.mechwv.placeeventmap.domain.model.Role
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileFragmentBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileFragmentBinding.inflate(layoutInflater, container, false)

        viewModel.getProfile().observe(viewLifecycleOwner, { profile ->
            binding.email.text = profile?.default_email
            binding.realName.text = profile?.real_name
        })

        viewModel.getCurrentUser().observe(viewLifecycleOwner, { user ->
            if (user?.role != Role.ADMIN.toString()) {
                binding.addModerators.visibility = View.GONE
            }
            if ((user?.role != Role.MODERATOR.toString()) || (user.role != Role.ADMIN.toString())) {
                binding.reviewPlaces.visibility = View.GONE
            }
        })

        binding.logout.setOnClickListener {
            viewModel.logout()
            val action = ProfileFragmentDirections.actionProfileFragmentToAuthFragment()
            findNavController().navigate(action)

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}