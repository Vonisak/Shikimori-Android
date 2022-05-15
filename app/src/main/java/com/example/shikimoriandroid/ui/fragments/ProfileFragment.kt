package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.shikimoriandroid.*
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.databinding.FragmentProfileBinding
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.presentation.viewModels.ProfileViewModel

class ProfileFragment : BaseBottomNavFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val glideAdapter = GlideAdapter(this)
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as MainActivity).users[0].let { user ->
            user.accessToken?.let { profileViewModel.getProfileInfo(it, user.uid) }
        }
        observeModel()
        listeners()
        return binding.root
    }

    private fun listeners() {
        binding.swipeRefresh.setOnRefreshListener {
            (activity as MainActivity).users[0].let { user ->
                user.accessToken?.let { profileViewModel.getProfileInfo(it, user.uid) }
            }
        }
        binding.userList.setOnClickListener {
            navigate(R.id.action_profileFragment_to_userListFragment)
        }
    }

    private fun observeModel() {
        profileViewModel.getState().observe(viewLifecycleOwner) { it ->
            when (it) {
                is State.Pending -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is State.Fail -> {
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), "Fail: ${it.error}", Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    if ((activity as MainActivity).users.isNotEmpty()) {
                        Log.i("TAG", it.data.toString())
                        binding.profileHeader.profileNickname.text = it.data.nickname

                        glideAdapter.loadImage(
                            it.data.images.imageX148,
                            binding.profileHeader.profileImage)

                        binding.profileHeader.profilePlanned.text = it.data.stats.statuses.anime[0].size.toString()
                        binding.profileHeader.profileWatching.text = it.data.stats.statuses.anime[1].size.toString()
                        binding.profileHeader.profileCompleted.text = it.data.stats.statuses.anime[2].size.toString()
                        binding.profileHeader.profileOnHold.text = it.data.stats.statuses.anime[3].size.toString()
                        binding.profileHeader.profileDropped.text = it.data.stats.statuses.anime[4].size.toString()

                        var info = ""
                        it.data.commonInfo.forEachIndexed { index, s ->
                            if (index != it.data.commonInfo.size-1) info += "$s / "
                        }
                        binding.profileHeader.profileCommonInfo.text = info

                        binding.swipeRefresh.isRefreshing = false
                    } else {
                        //navigate(R.id.action_profileFragment_to_authFragment)
                        //findNavController().backQueue.removeLast()
                    }
                }
            }
        }
    }

}