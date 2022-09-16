package com.example.githubFollowers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.githubFollowers.databinding.ActivityMainBinding
import com.example.githubFollowers.viewmodels.MainViewModel
import com.example.githubFollowers.views.FavoriteFragment
import com.example.githubFollowers.views.FollowersFragment

import com.example.githubFollowers.views.HomeFragment
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

        mainViewModel.followersData.observe(this) { followers->
            if (followers!=null){
                val followersFragment = FollowersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.flayout,followersFragment).addToBackStack(null).commit()
            }

        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.miHome ->{

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.flayout,fragmentHome)
                        .commit()
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