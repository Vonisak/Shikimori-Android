package com.example.shikimoriandroid.localBd

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AuthInfo::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun authDao(): AuthDao
}