package com.example.shikimoriandroid.localBd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert
    fun insert(userInfo: AuthInfo)

    @Query("SELECT * FROM authInfo")
    fun getAll(): List<AuthInfo>

    @Delete
    fun delete(userInfo: AuthInfo)

    @Query("DELETE FROM authInfo")
    fun cleanUsers()
}