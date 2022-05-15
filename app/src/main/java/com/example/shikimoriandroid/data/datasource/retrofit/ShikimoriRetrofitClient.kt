package com.example.shikimoriandroid.data.datasource.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ShikimoriRetrofitClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://shikimori.one/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}