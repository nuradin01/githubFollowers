package com.example.githubFollowers.views

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.githubFollowers.R
import com.example.githubFollowers.databinding.FragmentHomeBinding
import com.example.githubFollowers.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentHomeBinding? = null

    /**
     * Inflate the layout for this fragment and set [binding]
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)



        binding!!.btnGetUser.setOnClickListener {

            val username = binding!!.userNameTextInput.text.toString()
            if (username.isEmpty()) {
                binding!!.userNameTextField.error = "This field can not be empty"
            } else {
                mainViewModel.getUser(username)
                mainViewModel.errorCode.observe(viewLifecycleOwner) { code ->
                    if (code >= 400) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Something Went wrong")
                            .setMessage("this username does not exist!")
                            .setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }

                }
                mainViewModel.userData.observe(viewLifecycleOwner) { user ->
                    if (user != null) {
                        val profileFragment = ProfileFragment()
                        (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.flayout, profileFragment)
                            .addToBackStack(null).commit()
                    }

                }
            }

        }











        return binding!!.root
    }

    // Hiding the top app bar
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}