package com.example.githubFollowers.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubFollowers.R
import com.example.githubFollowers.adapter.UserAdapter
import com.example.githubFollowers.databinding.FragmentFollowersBinding
import com.example.githubFollowers.models.UserData
import com.example.githubFollowers.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass.

 */
class FollowersFragment : Fragment(R.layout.fragment_followers), SearchView.OnQueryTextListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentFollowersBinding? = null
    private val mAdapter by lazy { UserAdapter(mainViewModel) }
    private var page by Delegates.notNull<Int>()
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
        mainViewModel.pageNum.observe(viewLifecycleOwner) { pageNum ->
            page = pageNum
            page++
        }
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)


        mainViewModel.followersData.observe(viewLifecycleOwner) { followers ->
            if (followers.isEmpty()) {
                Timber.d(followers.toString())
                binding!!.tvError.visibility = View.VISIBLE
                binding!!.tvError.text = "This user doesn't have any followers"
                binding!!.ivError.visibility = View.VISIBLE
                binding!!.rvUsers.visibility = View.GONE
            } else if (!followers.isNullOrEmpty()) {
                binding!!.tvError.visibility = View.GONE
                binding!!.ivError.visibility = View.GONE
                displayData(gridLayoutManager, followers)
            }
        }


        binding!!.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount: Int = gridLayoutManager.childCount
                val pastVisibleItem: Int =
                    gridLayoutManager.findFirstCompletelyVisibleItemPosition()
                val total = mAdapter.itemCount
                if (visibleItemCount + pastVisibleItem >= total) {

                    mainViewModel.getFollowers(currentUser, page)
                }
                super.onScrolled(recyclerView, dx, dy)
            }


        })
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.followers_menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search?.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@FollowersFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_search -> {
                        mainViewModel.userData.observe(viewLifecycleOwner) { user ->
                            mainViewModel.insertData(user)
                        }


                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding!!.root
    }

    private fun displayData(gridLayoutManager: GridLayoutManager, followers: List<UserData>) {

        binding?.rvUsers?.apply {
            layoutManager = gridLayoutManager
            val recycleAdapter = mAdapter
            adapter = recycleAdapter
            recycleAdapter.submitList(followers)
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val filteredFollowers = arrayListOf<UserData>()
        if (!newText.isNullOrEmpty()) {
            mainViewModel.followersData.observe(viewLifecycleOwner) { followers ->
                followers.forEach {
                    if (it.login.lowercase().contains(newText.lowercase())) {
                        filteredFollowers.add(it)
                    }
                }
            }
            if (filteredFollowers.isNotEmpty()) {

                displayData(GridLayoutManager(requireContext(), 3), filteredFollowers.toList())
            }
        } else {
            mainViewModel.followersData.observe(viewLifecycleOwner) { followers ->
                displayData(GridLayoutManager(requireContext(), 3), followers)

            }

        }
        return true
    }

}