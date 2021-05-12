package com.raisc.dnaapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.raisc.dnaapp.model.Project
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class Repository @Inject constructor(
    private val projectsDao: ProjectsDao?,
    private val context: Context?,
    private val mIoExecutor: ExecutorService
) {

    private var isCachedExpired = true
    private val mUid = FirebaseAuth.getInstance().currentUser.uid

//    fun getSortedTeas(sort: String?, fileByFavorite: Boolean?): LiveData<PagedList<BarItem>> {
//        val sortBy: SortUtils.TeaSortBy = SortUtils.TeaSortBy.valueOf(sort)
//        val factory: DataSource.Factory<Int, BarItem> = mDao.getAll(SortUtils.getAllQuery(sortBy, fileByFavorite))
//        return LivePagedListBuilder<Any?, Any?>(factory, PAGE_SIZE)
//                .build()
//    }


//

    fun getPendingProjects(): LiveData<List<Project>> {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return projectsDao!!.getPendingProjects()
    }

    fun getIncompleteProjects(): LiveData<List<Project>> {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return projectsDao!!.getIncompleteProjects()
    }

    fun getCompleteProjects(): LiveData<List<Project>> {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return projectsDao!!.getcompleteProjects()
    }

    fun getNewProjectsProjects(): LiveData<List<Project>> {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return projectsDao!!.getNewProjects()
    }

//    fun refreshCache(cacheExpiryHours: Int) {
//        projectsDao!!.get()
////        if (isCachedExpired && context.isNetworkAvailable() == true) {
////
////            val response = api?.getBarClub()
////
////            return dao!!.insert(response!!.value!!)
////        } else {
////            dao!!.getClubBar()
////        }
//    }


    fun save(item: Project) {
        mIoExecutor.execute {
            //saving to locat database
            projectsDao!!.insert(item)

            //store key inside her for temp use
            val cartKey = FirebaseDatabase.getInstance().reference
                .child("user")
                .child(mUid)
                .child("projects")


            //saving to firbase
            val firebaseDatabase = FirebaseDatabase.getInstance().reference
                .child("users")
                .child(mUid)
                .child("projects")
                .child("orders")
            key = firebaseDatabase.push().key!!
            firebaseDatabase.child(key).setValue(item)
            cartKey.child("key").setValue(key)
        }

//    override fun saveCount(count: BarsClubsItem) {
//        mIoExecutor.execute {
//            dao!!.saveCount(count)
//        }
//    }

    }

    companion object {
        const val LIMIT = 10
        const val CACHE_EXPIRY_HOURS = 2 // cache expiry time in hours
        var key = ""

        val TAG = Repository::class.java.simpleName

        @Volatile
        private var sInstance: Repository? = null
        private const val PAGE_SIZE = 20
        fun getInstance(context: Context?): Repository? {
            if (sInstance == null) {
                synchronized(Repository::class.java) {
                    if (sInstance == null) {
                        val database: ProjectsDatabase = ProjectsDatabase.getInstance(context!!)
                        //val cartDatabase: CartItemsDatabase = CartItemsDatabase.getInstance(context)
                        sInstance = Repository(
                            database.projectsDao(), context,
                            Executors.newSingleThreadExecutor()
                        )
                    }
                }
            }
            return sInstance
        }
    }

    init {
        //getBarItems = mDao.getItem(barItem.itemName)
    }


}
