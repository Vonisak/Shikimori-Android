package com.example.shikimoriandroid.domain.utils

class StringMod {
    fun profileMod(str: String): String {
        var flag = true
        var start = 0
        var end = 0
        while (flag) {
            for ((index, char) in str.withIndex()) {
                if (char == '<') start = index
                if (char == '>') {
                    end = index
                    flag = true
                    break
                }
                flag = false
            }
            str.removeRange(start, end)
        }
        return str
    }
}