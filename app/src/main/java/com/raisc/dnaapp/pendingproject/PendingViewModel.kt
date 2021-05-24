package com.raisc.dnaapp.pendingproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raisc.dnaapp.data.Repository
import com.raisc.dnaapp.model.Project
import javax.inject.Inject

class PendingViewModel
@Inject constructor
    (private var mRepository: Repository?) : ViewModel() {
    private var clubName = " "

    //lateinit var barsClubs: BarsClubs
    //var mBars: Bars = mRepository!!.getBarClubs()


    fun getPendingProjects(): LiveData<List<Project>> {
        return mRepository!!.getPendingProjects()
    }

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