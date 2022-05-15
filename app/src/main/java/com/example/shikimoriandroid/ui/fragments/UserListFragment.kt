package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shikimoriandroid.ui.adapters.UserListPagerAdapter
import com.example.shikimoriandroid.databinding.FragmentUserListBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

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

        // Inflate the layout for this fragment
        return binding.root
    }

}