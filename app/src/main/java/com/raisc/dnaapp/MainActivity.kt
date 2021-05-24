package com.raisc.dnaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.raisc.dnaapp.completeproject.CompletedActivity
import com.raisc.dnaapp.databinding.ActivityCompleteBinding
import com.raisc.dnaapp.databinding.ActivityMainBinding
import com.raisc.dnaapp.incompleteproject.IncompleteActivity
import com.raisc.dnaapp.newproject.NewProject
import com.raisc.dnaapp.pendingproject.PendingActivity

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    fun createNewProject(view: View) {
        startActivity(Intent(this, NewProject::class.java))
    }

    fun pendingProject(view: View) {
        startActivity(Intent(this, PendingActivity::class.java))
    }

    fun incompleteProject(view: View) {
        startActivity(Intent(this, IncompleteActivity::class.java))
    }

    fun completedProject(view: View) {
        startActivity(Intent(this, CompletedActivity::class.java))
    }
}