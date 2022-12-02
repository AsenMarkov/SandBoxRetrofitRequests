package com.example.sandboxretrofitrequests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.sandboxretrofitrequests.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BaseFragment.ActivityActions {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SandBoxRetrofitRequests) // Set the theme back to Normal
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showProgressBar(show: Boolean) {
        binding.progressBar.isVisible = show
    }
}