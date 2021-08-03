package com.raisc.dnaapp.incompleteproject

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
import kotlinx.coroutines.launch
import javax.inject.Inject

class IncompleteViewModel
@Inject constructor
    (private var mRepository: Repository?) : ViewModel() {
    private var clubName = " "


    fun getIncompleteProjects(): Flow<PagingData<Project>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 60,
            enablePlaceholders = true
        ),
        pagingSourceFactory = { mRepository?.getIncompleteProjects()!! }
    ).flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)


    fun delete(project: Project) {
        viewModelScope.launch {

        }

        return mRepository!!.deleteProject(project)
    }

    fun markComplete(project: Project) {
        return mRepository!!.makeComplete(project)
    }


//    fun setTeaImage(teaType: String?) {
//        mTeaDetail.setValue(BarsItemsViewModel(teaType))
//    }

}