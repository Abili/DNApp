package com.raisc.dnaapp.completeproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raisc.dnaapp.data.Repository
import com.raisc.dnaapp.model.Project
import javax.inject.Inject

class CompleteViewModel
@Inject constructor
    (private var mRepository: Repository?) : ViewModel() {


    fun getCompleteProjects(): LiveData<List<Project>> {
        return mRepository!!.getCompleteProjects()
    }


//    fun setTeaImage(teaType: String?) {
//        mTeaDetail.setValue(BarsItemsViewModel(teaType))
//    }

}