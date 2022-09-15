package com.example.githubFollowers.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.githubFollowers.R
import com.example.githubFollowers.databinding.FragmentFollowersBinding
import com.example.githubFollowers.viewmodels.MainViewModel


/**
 * A simple [Fragment] subclass.

 */
class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentFollowersBinding? = null

    /**
     * Inflate the layout for this fragment and set [binding]
     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater,container,false)

        mainViewModel.webData.observe(viewLifecycleOwner) { followers->
            if (followers.isEmpty()){
                binding!!.tvError.visibility = View.VISIBLE
                binding!!.tvError.text= "This user doesn't have any followers"
                binding!!.ivError.visibility = View.VISIBLE
            } else if (!followers.isNullOrEmpty()){
                binding!!.tvError.visibility = View.GONE
                binding!!.ivError.visibility = View.GONE
            }

        }

        return binding!!.root
    }


}