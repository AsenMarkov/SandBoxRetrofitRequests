package com.example.sandboxretrofitrequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sandboxretrofitrequests.databinding.FragmentMockBinding


class MockFragment : BaseFragment(), BaseFragment.ActivityActions {

    private lateinit var binding: FragmentMockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMockBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onResume() {
        showHideBottomNavBar(true)
        super.onResume()
    }

    override fun onPause() {
        showHideBottomNavBar(false)
        super.onPause()
    }

    override fun showProgressBar(show: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showHideBottomNavBar(show: Boolean) {
        actions?.showHideBottomNavBar(show)
    }
}