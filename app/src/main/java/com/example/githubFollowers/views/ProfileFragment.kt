package com.example.githubFollowers.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import coil.load
import com.example.githubFollowers.R
import com.example.githubFollowers.databinding.FragmentProfileBinding
import com.example.githubFollowers.models.UserData
import com.example.githubFollowers.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.

 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentProfileBinding? = null
    private lateinit var currentUser: UserData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        mainViewModel.userData.observe(viewLifecycleOwner) { user ->
            currentUser = user
            binding!!.ivAvatar.load(user.avatar_url) {
                crossfade(600)
                error(R.drawable.ic_person)
            }
            binding!!.tvUsername.text = user.login
            binding!!.tvName.text = user.name
            if (user.location.isNullOrEmpty() || user.location == "null") {
                binding!!.tvLocation.visibility = View.INVISIBLE
                binding!!.imageView3.visibility = View.INVISIBLE
            } else {
                binding!!.tvLocation.text = user.location
            }
            if (user.bio.isNullOrEmpty() || user.bio == "null") {
                binding!!.tvBio.visibility = View.GONE
            } else {
                binding!!.tvBio.text = user.bio
            }
            binding!!.tvFollowingNumber.text = user.following.toString()
            binding!!.tvFollowersNumber.text = user.followers.toString()
            binding!!.tvRepoNumber.text = user.public_repos.toString()
            binding!!.tvGistsNumber.text = user.public_gists.toString()

            binding!!.tvJoinedAt.text = "GitHub since ${user.created_at.substring(0,10)}"
        }



        binding!!.btnGithubProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/${currentUser.login}"))
            startActivity(intent)
        }
        binding!!.btnGetFollowers.setOnClickListener {
            mainViewModel.getFollowers(currentUser.login)
            mainViewModel.followersData.observe(viewLifecycleOwner) { followers ->
                if (followers != null) {
                    val followersFragment = FollowersFragment()
                    (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.flayout, followersFragment)
                        .addToBackStack(null).commit()
                }

            }
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
                        Snackbar
                            .make(
                                binding!!.ivAvatar, "You have successfully " +
                                        "added this user to your favorites", Snackbar.LENGTH_SHORT
                            )
                            .show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding!!.root
    }




}