package com.example.githubFollowers.views

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import coil.load
import com.example.githubFollowers.R
import com.example.githubFollowers.databinding.FragmentFollowersBinding
import com.example.githubFollowers.databinding.FragmentHomeBinding
import com.example.githubFollowers.databinding.FragmentProfileBinding
import com.example.githubFollowers.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.

 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentProfileBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        mainViewModel.userData.observe(viewLifecycleOwner) { user ->
            binding!!.ivAvatar.load(user.avatar_url) {
                crossfade(600)
                error(R.drawable.ic_person)
            }
            binding!!.tvUsername.text = user.login
            binding!!.tvName.text = user.name
            user.location?.let { binding!!.tvLocation.text = it }
            user.bio?.let { binding!!.tvBio.text = it }
            binding!!.tvFollowingNumber.text = user.following.toString()
            binding!!.tvFollowersNumber.text = user.followers.toString()
            binding!!.tvRepoNumber.text = user.public_repos.toString()
            binding!!.tvGistsNumber.text = user.public_gists.toString()
            binding!!.tvJoinedAt.text = "GitHub since ${user.created_at}"

        }
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.favMenu -> {
                        mainViewModel.userData.observe(viewLifecycleOwner) { user ->
                            mainViewModel.insertData(user)
                        }
                        changeMenuItemColor(menuItem, R.color.yellow)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding!!.root
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }


}