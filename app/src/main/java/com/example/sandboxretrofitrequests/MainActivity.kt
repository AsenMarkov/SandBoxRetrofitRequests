package com.example.sandboxretrofitrequests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.sandboxretrofitrequests.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BaseFragment.ActivityActions {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavBar: BottomNavigationView


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SandBoxRetrofitRequests) // Set the theme back to Normal
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavBar = binding.bottomNavBar
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_gallery_fragment -> {
                    navHostFragment.findNavController()
                        .navigate(R.id.action_global_to_unsplashPhotosFragment)
                }
                R.id.item_mock_fragment -> {
                    navHostFragment.findNavController()
                        .navigate(R.id.action_global_to_mockFragment)
                }
            }
            true
        }

    }

    override fun showProgressBar(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    override fun showHideBottomNavBar(show: Boolean) {
        binding.bottomNavBar.isVisible = show
    }
}