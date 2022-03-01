package com.example.shikimoriandroid.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.shikimoriandroid.Constants
import com.example.shikimoriandroid.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.adapters.GlideAdapter
import com.example.shikimoriandroid.databinding.FragmentProfileBinding
import com.example.shikimoriandroid.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileFragment : BaseBottomNavFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
        ShikimoriAPI::class.java
    )
    private val glideAdapter = GlideAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        if ((activity as MainActivity).users.isNotEmpty()) {
            (activity as MainActivity).users[0].accessToken?.let { accessToken ->
                provider.getCurrentUser(accessToken = "Bearer $accessToken")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            binding.profileNickname.text = it.nickname
                            glideAdapter.loadImage(it.images.imageX148, binding.profileImage)
                        },
                        {
                            Log.i("TAG", it.toString())
                        }
                    )
            }
        } else {
//            navigate(R.id.action_profileFragment_to_authFragment)
//            findNavController().backQueue.removeLast()
        }

        return binding.root
    }

}