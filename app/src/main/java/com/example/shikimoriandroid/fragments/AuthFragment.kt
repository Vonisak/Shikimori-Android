package com.example.shikimoriandroid.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.shikimoriandroid.Constants
import com.example.shikimoriandroid.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.FragmentAuthBinding
import com.example.shikimoriandroid.databinding.FragmentProfileBinding
import com.example.shikimoriandroid.localBd.AuthInfo
import com.example.shikimoriandroid.localBd.Database
import com.example.shikimoriandroid.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class AuthFragment : BaseBottomNavFragment() {


    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
        ShikimoriAPI::class.java
    )
    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private var userId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAuthBinding.inflate(inflater, container, false)


        binding.auth.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.appLink)
            startActivity(intent)
        }

        binding.authButton.setOnClickListener {
            provider.getTokens(authCode = binding.authInput.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        accessToken = it.accessToken
                        refreshToken = it.refreshToken
                        Log.i("TAG", it.accessToken)
                        Log.i("TAG", it.refreshToken)
                    },
                    {
                        Log.i("TAG", it.toString())
                    }
                )
        }

        binding.showUserInfo.setOnClickListener {
            Log.i("TAG", "Bearer $accessToken")
            provider.getCurrentUser(accessToken = "Bearer $accessToken")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        userId = it.id

                        runBlocking(Dispatchers.IO) {
                            (activity as MainActivity).userDao
                                .insert(AuthInfo(userId, accessToken, refreshToken))
                        }
                        (activity as MainActivity).updateUsersInfo()
                        navigate(R.id.action_authFragment_to_profileFragment)
                        findNavController().backQueue.removeLast()
                        Log.i("TAG", it.toString())
                    },
                    {
                        Log.i("TAG", it.toString())
                    }
                )
        }

        return binding.root
    }

}