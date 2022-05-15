package com.example.shikimoriandroid.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shikimoriandroid.ui.fragments.UserListFragment
import com.example.shikimoriandroid.ui.fragments.UserListRecyclerFragment

class UserListPagerAdapter(userListFragment: UserListFragment): FragmentStateAdapter(userListFragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment = UserListRecyclerFragment(position)
}