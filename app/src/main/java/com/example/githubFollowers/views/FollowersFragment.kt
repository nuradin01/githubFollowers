package com.example.githubFollowers.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubFollowers.R
import com.example.githubFollowers.adapter.UserAdapter
import com.example.githubFollowers.databinding.FragmentFollowersBinding
import com.example.githubFollowers.models.UserData
import com.example.githubFollowers.viewmodels.MainViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.

 */
class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentFollowersBinding? = null
    private val mAdapter by lazy { UserAdapter(mainViewModel) }
    private var page = 1
    private lateinit var currentUser: String


    /**
     * Inflate the layout for this fragment and set [binding]
     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        mainViewModel.userData.observe(viewLifecycleOwner) { user ->
            currentUser = user.login
        }
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)


        mainViewModel.followersData.observe(viewLifecycleOwner) { followers ->
                if (followers.isEmpty()) {
                    binding!!.tvError.visibility = View.VISIBLE
                    binding!!.tvError.text = "This user doesn't have any followers"
                    binding!!.ivError.visibility = View.VISIBLE
                    binding!!.rvUsers.visibility = View.GONE
                } else if (!followers.isNullOrEmpty()) {
                    binding!!.tvError.visibility = View.GONE
                    binding!!.ivError.visibility = View.GONE
                    displayData(gridLayoutManager)
                }
            }


        binding!!.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount: Int = gridLayoutManager.childCount
                val pastVisibleItem: Int =
                    gridLayoutManager.findFirstCompletelyVisibleItemPosition()
                val total = mAdapter.itemCount
                if (visibleItemCount + pastVisibleItem >= total) {
                    mainViewModel.pageNum.observe(viewLifecycleOwner) { pageNum ->
                        page = pageNum
                        page++
                    }
                    mainViewModel.getFollowers(currentUser, page)
                }
                super.onScrolled(recyclerView, dx, dy)
            }


        })

        return binding!!.root
    }

    private fun displayData(gridLayoutManager: GridLayoutManager) {
        mainViewModel.followersData.observe(viewLifecycleOwner) {
            binding?.rvUsers?.apply {
                layoutManager = gridLayoutManager
                val recycleAdapter = mAdapter
                adapter = recycleAdapter
                recycleAdapter.submitList(it)
            }
        }
    }

}