package com.example.shikimoriandroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.adapters.FilterPagerAdapter
import com.example.shikimoriandroid.databinding.FragmentMainAnimeListBinding
import com.example.shikimoriandroid.databinding.FragmentMainListBottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainListBottomSheetFilterFragment(private val listener: OnClickListener) : BottomSheetDialogFragment() {

    private var _binding: FragmentMainListBottomSheetFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainListBottomSheetFilterBinding.inflate(inflater, container, false)
        binding.pager.adapter = FilterPagerAdapter(this)
        binding.pager.isSaveEnabled = false
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Сортировка"
                1 -> tab.text = "Тип"
                2 -> tab.text = "Жанр"
                3 -> tab.text = "Статус"
            }
        }.attach()
        return binding.root
    }

    interface OnClickListener {
        fun onClick()
    }

    companion object {
        const val TAG = "FilterBottomSheet"
    }

}