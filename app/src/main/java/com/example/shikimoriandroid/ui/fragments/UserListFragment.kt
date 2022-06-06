package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shikimoriandroid.ui.adapters.UserListPagerAdapter
import com.example.shikimoriandroid.databinding.FragmentUserListBinding
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {

    companion object {

        const val TITLE = "Список пользователя"
    }

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = TITLE
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as MainActivity).showBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        initTabLayout()

        return binding.root
    }

    private fun initTabLayout() {
        binding.pager.adapter = UserListPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Смотрю"
                1 -> tab.text = "Просмотрено"
                2 -> tab.text = "Запланировано"
                3 -> tab.text = "Отложено"
                4 -> tab.text = "Брошено"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as MainActivity).showBottomNav(true)
    }
}