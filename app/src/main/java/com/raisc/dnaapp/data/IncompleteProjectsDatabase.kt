package com.raisc.dnaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raisc.dnaapp.model.Project
import com.raisc.dnaapp.utils.SingletonHolder

@Database(entities = [Project::class], version = 1)

abstract class IncompleteProjectsDatabase : RoomDatabase() {

    abstract val inCompleteProjectsDao: InCompleteProjectsDao


    companion object : SingletonHolder<IncompleteProjectsDatabase, Context>(creator = {
        Room.databaseBuilder(
            it.applicationContext,
            IncompleteProjectsDatabase::class.java,
            "incompleter_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    })
}