package com.raisc.dnaapp.data

import android.content.Context
import androidx.paging.PagingSource
import com.google.firebase.database.FirebaseDatabase
import com.raisc.dnaapp.model.Project
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class Repository @Inject constructor(
    private val pendingProjectsDao: PendingProjectsDao?,
    private val inCompleteDao: InCompleteProjectsDao?,
    private val completeDao: CompleteProjectsDao?,
    private val context: Context?,
    private val mIoExecutor: ExecutorService
) {

    private var isCachedExpired = true
//    private val mUid = FirebaseAuth.getInstance().currentUser.uid

//    fun getSortedTeas(sort: String?, fileByFavorite: Boolean?): LiveData<PagedList<BarItem>> {
//        val sortBy: SortUtils.TeaSortBy = SortUtils.TeaSortBy.valueOf(sort)
//        val factory: DataSource.Factory<Int, BarItem> = mDao.getAll(SortUtils.getAllQuery(sortBy, fileByFavorite))
//        return LivePagedListBuilder<Any?, Any?>(factory, PAGE_SIZE)
//                .build()
//    }


//


    fun deleteProject(project: Project) {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return inCompleteDao!!.delete(project)
    }

    fun getPendingProjects(): PagingSource<Int, Project> {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return pendingProjectsDao!!.getPendingProjects()
    }

    fun getPendingProject(projectName: String): Project {
        //refreshCache(CACHE_EXPIRY_HOURS)
        return pendingProjectsDao!!.getPendingProject(projectName)
    }

    fun getIncompleteProjects(): PagingSource<Int,Project> {
        return inCompleteDao!!.getIncompleteProjects()
    }


    fun getCompleteProjects(): PagingSource<Int, Project> {
        //refreshCache(CACHE_EXPIRY_HOURS)

        return completeDao!!.getCompleteProjects()
    }

//    fun getNewProjectsProjects(): PagingSource<Int, Project> {
//        //refreshCache(CACHE_EXPIRY_HOURS)
//        return pendingProjectsDao!!.getNewProjects()
//    }

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
            inCompleteDao!!.insert(item)

            //store key inside her for temp use
            cartKey
                .child("user")
                //.child(mUid)
                .child("projects")


            //saving to firbase
            firebaseDatabase
                .child("user")
                //.child(mUid)
                .child("projects")
                .child("incomplete")
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


    fun makeComplete(item: Project) {
        mIoExecutor.execute {
            //saving to locat database
            inCompleteDao!!.insert(item)

            //store key inside her for temp use
            cartKey
                .child("user")
                //.child(mUid)
                .child("projects")


            //saving to firbase
            firebaseDatabase
                .child("user")
                //.child(mUid)
                .child("projects")
                .child("complete")
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
        val firebaseDatabase = FirebaseDatabase.getInstance().reference
        val cartKey = FirebaseDatabase.getInstance().reference

        @Volatile
        private var sInstance: Repository? = null
        private const val PAGE_SIZE = 10
        fun getInstance(context: Context): Repository? {
            if (sInstance == null) {
                synchronized(Repository::class.java) {
                    if (sInstance == null) {
                        val pendingProjectsDb = PendingProjectsDatabase.getInstance(context)
                        val incompleteDb = IncompleteProjectsDatabase.getInstance(context)
                        val completeDb = CompleteProjectsDatabase.getInstance(context)
                        sInstance = Repository(
                            pendingProjectsDb.peindingProjectsDao,
                            incompleteDb.inCompleteProjectsDao!!,
                            completeDb.completeDao,
                            context,
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
