package com.raisc.dnaapp.newproject

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raisc.dnaapp.data.Repository
import java.lang.reflect.InvocationTargetException
import javax.inject.Inject


/**
 * Factory for creating a ViewModel
 */
class NewProjectViewModelFactory @Inject constructor(instance: Repository) : ViewModelProvider.Factory {
    private val mRepository: Repository = instance
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.getConstructor(Repository::class.java)
                    .newInstance(mRepository)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }

    companion object {
        fun createFactory(activity: Activity): NewProjectViewModelFactory {
            val context = activity.applicationContext
                    ?: throw IllegalStateException("Not yet attached to Application")
            return NewProjectViewModelFactory(Repository.getInstance(context)!!)
        }
    }

}