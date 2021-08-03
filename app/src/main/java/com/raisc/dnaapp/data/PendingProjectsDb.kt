package com.raisc.dnaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raisc.dnaapp.model.Project
import com.raisc.dnaapp.utils.SingletonHolder

@Database(entities = [Project::class], version = 1)

abstract class PendingProjectsDatabase : RoomDatabase() {

    abstract val peindingProjectsDao: PendingProjectsDao


    companion object : SingletonHolder<PendingProjectsDatabase, Context>(creator = {
        Room.databaseBuilder(
            it.applicationContext,
            PendingProjectsDatabase::class.java,
            "pending_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    })
}