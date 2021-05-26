package com.raisc.dnaapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "projects_table")
class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val projectName: String?,
    val clientName: String?,
    val clientPic: String?,
    val clientPhone: String?,
    val projectLocation: String?,
    val clientID: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(projectName)
        parcel.writeString(clientName)
        parcel.writeString(clientPic)
        parcel.writeString(clientPhone)
        parcel.writeString(projectLocation)
        parcel.writeString(clientID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}