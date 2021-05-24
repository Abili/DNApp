package com.raisc.dnaapp.newproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raisc.dnaapp.data.Repository
import com.raisc.dnaapp.model.Project
import javax.inject.Inject

class NewProjectViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val mProjectSaved = MutableLiveData<Boolean>()


    fun isSaved(): LiveData<Boolean?> {
        return mProjectSaved
    }

    fun saveProject(
        projectName: String,
        clientName: String,
        imageUrl: String,
        clientPhone: String,
        clientId: String,
        projectLocation: String
    ) {
        if (projectName.isEmpty() || clientName.isEmpty() || clientPhone.isEmpty()) {
            mProjectSaved.value = false
            return
        }
        val project =
            Project(null, projectName, clientName, imageUrl, clientPhone, projectLocation, clientId)
        saveProject(project)
        mProjectSaved.value = true
    }

    fun saveProject(project: Project) {
        return repository.save(project)
    }

}