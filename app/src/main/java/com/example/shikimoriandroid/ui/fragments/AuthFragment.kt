package com.example.shikimoriandroid.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shikimoriandroid.domain.utils.Constants
import com.example.shikimoriandroid.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class AuthFragment : BaseBottomNavFragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
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

//        binding.authButton.setOnClickListener {
//            provider.getTokens(authCode = binding.authInput.text.toString())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    {
//                        accessToken = it.accessToken
//                        refreshToken = it.refreshToken
//                        Log.i("TAG", it.accessToken)
//                        Log.i("TAG", it.refreshToken)
//                    },
//                    {
//                        Log.i("TAG", it.toString())
//                    }
//                )
//        }
//
//        binding.showUserInfo.setOnClickListener {
//            Log.i("TAG", "Bearer $accessToken")
//            provider.getCurrentUser(accessToken = "Bearer $accessToken")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    {
//                        userId = it.id
//
//                        runBlocking(Dispatchers.IO) {
//                            (activity as MainActivity).userDao
//                                .insert(AuthInfo(userId, accessToken, refreshToken))
//                        }
//                        (activity as MainActivity).updateUsersInfo()
//                        navigate(R.id.action_authFragment_to_profileFragment)
//                        findNavController().backQueue.removeLast()
//                        Log.i("TAG", it.toString())
//                    },
//                    {
//                        Log.i("TAG", it.toString())
//                    }
//                )
//        }

        return binding.root
    }

}