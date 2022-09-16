package com.example.githubFollowers.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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

        mainViewModel.userData.observe(viewLifecycleOwner){user ->
            binding!!.ivAvatar.load(user.avatar_url){
                crossfade(600)
                error(R.drawable.ic_person)
            }
            binding!!.tvUsername.text = user.login
            binding!!.tvName.text = user.name
            user.location?.let { binding!!.tvLocation.text= it}
            user.bio?.let { binding!!.tvBio.text=it}
            binding!!.tvFollowingNumber.text = user.following.toString()
            binding!!.tvFollowersNumber.text = user.followers.toString()
            binding!!.tvRepoNumber.text = user.public_repos.toString()
            binding!!.tvGistsNumber.text = user.public_gists.toString()
            binding!!.tvJoinedAt.text = "GitHub since ${user.created_at}"






        }

        return binding!!.root
    }


}