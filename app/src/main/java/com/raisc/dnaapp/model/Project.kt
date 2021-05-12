package com.raisc.dnaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "projects_table")
class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val projectName: String,
    val clientName: String,
    val clientPic: String,
    val clientPhone: String,
    val projectLocation:String,
    val clientID:String
)