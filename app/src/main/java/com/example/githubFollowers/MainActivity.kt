package com.example.githubFollowers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.ViewModelProvider

import com.example.githubFollowers.databinding.ActivityMainBinding
import com.example.githubFollowers.viewmodels.MainViewModel
import com.example.githubFollowers.views.FavoriteFragment
import com.example.githubFollowers.views.FollowersFragment

import com.example.githubFollowers.views.HomeFragment
import com.example.githubFollowers.views.ProfileFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)

        val fragmentHome = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.flayout,fragmentHome).commit()

        mainViewModel.userData.observe(this) { user ->
            if (user != null) {
                val profileFragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.flayout, profileFragment)
                    .addToBackStack(null).commit()
            }
        }
        mainViewModel.followersData.observe(this) { followers->
            if (followers!=null){
                val followersFragment = FollowersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.flayout,followersFragment).addToBackStack(null).commit()
            }

        }
        mainViewModel.errorCode.observe(this) { code ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.flayout, fragmentHome)
                .commit()
            if (code >= 400) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Something Went wrong")
                    .setMessage("this username does not exist!")
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.miHome ->{


                    supportFragmentManager.popBackStack()
                }
                R.id.miFavorites -> {
                    val favoriteFragment =FavoriteFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.flayout,favoriteFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            true
        }

    }



}