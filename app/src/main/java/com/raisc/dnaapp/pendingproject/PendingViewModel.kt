package com.raisc.dnaapp.pendingproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.raisc.dnaapp.data.Repository
import com.raisc.dnaapp.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PendingViewModel
@Inject constructor
    (private var mRepository: Repository?) : ViewModel() {
    private var clubName = " "

    //lateinit var barsClubs: BarsClubs
    //var mBars: Bars = mRepository!!.getBarClubs()


    fun getPendingProjects(): Flow<PagingData<Project>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 60,
            enablePlaceholders = true
        ),
        pagingSourceFactory = { mRepository?.getPendingProject()!! }
    ).flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    fun getPendingProject(projectName: String): LiveData<List<Project>> {
        return mRepository!!.getPendingProject(projectName)
    }


//    fun setFavorite() {
//        mRepository!!.updateFavorite(itemName)
//    }

//    fun setTeaImage(teaType: String?) {
//        mTeaDetail.setValue(BarsItemsViewModel(teaType))
//    }

}