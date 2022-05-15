package com.example.shikimoriandroid

import android.app.Application
import androidx.room.Room
import com.example.shikimoriandroid.data.datasource.localBd.Database

class DatabaseApplication: Application() {

    lateinit var database: Database

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, Database::class.java, "database-name")
            .fallbackToDestructiveMigration().build()
    }
}