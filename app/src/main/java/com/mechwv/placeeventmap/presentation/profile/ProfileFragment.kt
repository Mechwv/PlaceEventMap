package com.mechwv.placeeventmap.presentation.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.databinding.ProfileFragmentBinding
import com.mechwv.placeeventmap.domain.model.Role
import dagger.hilt.android.AndroidEntryPoint

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
                binding.changeModerators.visibility = View.GONE
            }
            if (user?.role != Role.MODERATOR.toString()) {
                binding.reviewPlaces.visibility = View.GONE
            }
        })

        binding.logout.setOnClickListener {
            viewModel.logout()
            val action = ProfileFragmentDirections.actionProfileFragmentToAuthFragment()
            findNavController().navigate(action)
        }

        binding.reviewPlaces.setOnClickListener {
            viewModel.getCurrentUser().observe(viewLifecycleOwner, { user ->
                println("USER_MODERATOR_JWT: ${user?.jwtToken}")
                val action = ProfileFragmentDirections.actionProfileFragmentToModeratorFragment2(user!!.jwtToken!!)
                findNavController().navigate(action)
            })
        }

        binding.upload.setOnClickListener {
            showCloudDataConfirmationDialog(
                text = "Сохранение данных в облако удалит информацию сохраненную в облаке, вы уверены?",
                context = requireContext(),
                action = {
                    viewModel.getCurrentUser().observe(viewLifecycleOwner) { user ->
                        viewModel.getDBPlaces().observe(viewLifecycleOwner) { places ->
                            viewModel.saveCurrentPlaces(jwt_token = user?.jwtToken!!, places = places).observe(viewLifecycleOwner) {
                                println("ONLINE SAVE RESULT $it")
                            }
                        }
                    }
                }
            )
        }

        binding.download.setOnClickListener {
            showCloudDataConfirmationDialog(
                text = "Загрузка данных из облака удалит информацию сохраненную локально, вы уверены?",
                context = requireContext(),
                action = {
                    viewModel.getCurrentUser().observe(viewLifecycleOwner) { user ->
                        viewModel.getOnlinePlaces(jwt_token = user?.jwtToken!!).observe(viewLifecycleOwner) {
                            println("ONLINE GOT RESULT $it")
                        }
                    }
                }
            )
        }


        return binding.root
    }

    private fun showCloudDataConfirmationDialog(text: String, context: Context, action: () -> Unit) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setMessage(text)
            .setPositiveButton("Сохранить") { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}