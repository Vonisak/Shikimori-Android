package com.example.shikimoriandroid.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.shikimoriandroid.MainActivity

open class BaseBottomNavFragment: Fragment() {
    private var isNavigated = false
    private lateinit var title: String
    private var isCreate = true


    fun navigateWithAction(action: NavDirections) {
        isNavigated = true
        findNavController().navigate(action)
    }

    fun navigate(resId: Int) {
        isNavigated = true
        findNavController().navigate(resId)
    }

    fun navigate(resId: Int, bundle: Bundle) {
        isNavigated = true
        findNavController().navigate(resId, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isCreate) {
            title = (activity as MainActivity).supportActionBar?.title as String
            isCreate = false
        }
    }

    override fun onResume() {
        (activity as MainActivity).supportActionBar?.title = title
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        isCreate = true
    }
}