package com.example.sandboxretrofitrequests

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sandboxretrofitrequests.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSplashBinding.inflate(layoutInflater)
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedPreferences.getBoolean("logged", false)) {
            findNavController().navigate(R.id.action_splashFragment_to_unsplashPhotosFragment)
        }
        if (!sharedPreferences.getBoolean("logged", false)) {
            Handler(Looper.myLooper()!!).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }, 1000)
        }
    }
}