package com.example.githubFollowers.views

import android.os.Bundle
import android.view.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubFollowers.R
import com.example.githubFollowers.adapter.FavoriteAdapter
import com.example.githubFollowers.databinding.FragmentFavoriteBinding
import com.example.githubFollowers.databinding.FragmentFollowersBinding
import com.example.githubFollowers.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentFavoriteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        displayData()

        mainViewModel.readUsers.observe(viewLifecycleOwner) { user ->
            if (user.isNullOrEmpty()) {
                binding!!.rvUsers.visibility = View.GONE
                binding!!.tvError.visibility = View.VISIBLE
                binding!!.imageView4.visibility = View.VISIBLE
            } else {
                binding!!.rvUsers.visibility = View.VISIBLE
                binding!!.tvError.visibility = View.GONE
                binding!!.imageView4.visibility = View.GONE
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
                menuInflater.inflate(R.menu.favorite_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.miDelete -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setIcon(R.drawable.ic_delete)
                            .setTitle("Delete All?")

                            .setMessage("Are you sure to delete all users?")

                            .setNegativeButton("No") { dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Yes") { _, _ ->
                                // Respond to positive button press
                                mainViewModel.deleteData()
                            }
                            .show()

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding!!.root
    }

    private fun displayData() {
        mainViewModel.readUsers.observe(viewLifecycleOwner) {
            binding?.rvUsers?.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    GridLayoutManager.VERTICAL, false
                )
                val recycleAdapter = FavoriteAdapter(mainViewModel)
                adapter = recycleAdapter
                recycleAdapter.submitList(it)
            }
        }
    }


}