package com.example.shikimoriandroid.presentation.entity

sealed class State<T> {
    data class Success<T>(var data: T) : State<T>()
    data class Fail<T>(var error: Throwable?) : State<T>()
    class Pending<T> : State<T>()
}