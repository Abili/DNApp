package com.raisc.dnaapp.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.raisc.dnaapp.model.Project
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Project::class], version = 1)
abstract class ProjectsDatabase : RoomDatabase() {
    abstract fun projectsDao(): ProjectsDao?

    companion object {
        private const val NUMBER_OF_THREADS = 4

        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        private var mContext: Context? = null

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: ProjectsDatabase? = null

        /**
         * Returns an instance of Room Database
         *
         * @param context application context
         * @return The singleton TeaDatabase
         */
        @Synchronized
        fun getInstance(context: Context?): ProjectsDatabase {
            mContext = context
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context!!.applicationContext,
                    ProjectsDatabase::class.java, "projects_database")
                    .addCallback(sRoomDatabaseCallback)
                    .build()
            }
            return sInstance!!
        }

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {

                }
            }
        }


        /**
         * Load demo data into database.
         *
         * @param context to get raw data.
         */
//        @WorkerThread
//        fun fillWithStartingData(context: Context?) {
//            val dao: ProjectsDao? = getInstance(context).hotelsDbDao()
//            val projects: JSONArray? = loadJsonArray(context)
//            try {
//                for (i in 0 until projects!!.length()) {
//                    val project = projects.getJSONObject(i)
//                    dao!!.insert(Project(
//                        project.getString("projectName"),
//                        project.getString("clientName"),
//                        project.getString("imageUrl"),
//                        project.getString("phone")))
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }

//        private fun loadJsonArray(context: Context?): JSONArray? {
//            val builder = StringBuilder()
//            val `in` = context!!.resources.openRawResource(R.raw.sample_restuarants)
//            val reader = BufferedReader(InputStreamReader(`in`))
//            var line: String?
//            try {
//                while (reader.readLine().also { line = it } != null) {
//                    builder.append(line)
//                }
//                val json = JSONObject(builder.toString())
//                return json.getJSONArray("hotels")
//            } catch (exception: IOException) {
//                exception.printStackTrace()
//            } catch (exception: JSONException) {
//                exception.printStackTrace()
//            }
//            return null
//        }

    }
}