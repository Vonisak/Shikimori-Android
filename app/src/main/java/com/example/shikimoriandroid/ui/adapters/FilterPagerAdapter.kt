package com.example.shikimoriandroid.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shikimoriandroid.ui.fragments.FilterPagerItemFragment
import com.example.shikimoriandroid.ui.fragments.FilterPagerItemFragment.Companion.ARG_OBJECT
import com.example.shikimoriandroid.ui.fragments.MainListBottomSheetFilterFragment

//TODO сделать отдельный фрагмент для радио батанов (засунуть ресайклер в радиогрупп)
// и проверять по позиции когда создаем фрагмент


class FilterPagerAdapter(bs: MainListBottomSheetFilterFragment) : FragmentStateAdapter(bs) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val fragment = FilterPagerItemFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }
        return fragment
    }

}