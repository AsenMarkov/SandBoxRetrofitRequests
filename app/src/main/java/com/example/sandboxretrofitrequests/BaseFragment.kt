package com.example.sandboxretrofitrequests

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    var actions: ActivityActions? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (it is ActivityActions) {
                actions = it
            }
        }
    }

    interface ActivityActions {
        fun showProgressBar(show: Boolean)
    }
}