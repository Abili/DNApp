package com.raisc.dnaapp.incompleteproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raisc.dnaapp.data.Repository
import com.raisc.dnaapp.model.Project
import javax.inject.Inject

class IncompleteViewModel
@Inject constructor
    (private var mRepository: Repository?) : ViewModel() {
    private var clubName = " "


    fun getIncompleteProjects(): LiveData<List<Project>> {
        return mRepository!!.getIncompleteProjects()
    }


//    fun setTeaImage(teaType: String?) {
//        mTeaDetail.setValue(BarsItemsViewModel(teaType))
//    }

}