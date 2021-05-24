package com.raisc.dnaapp.incompleteproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.raisc.dnaapp.R
import com.raisc.dnaapp.databinding.ActivityCompleteBinding
import com.raisc.dnaapp.databinding.ActivityIncompleteBinding

class IncompleteActivity : AppCompatActivity() {
    var binding: ActivityIncompleteBinding? = null
    var viewModel: IncompleteViewModel? = null
    var factory: IncompleteProjectViewModelFactory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncompleteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        factory = IncompleteProjectViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory!!).get(IncompleteViewModel::class.java)
        viewModel!!.getIncompleteProjects().observe(this, { items->
            items.let {

            }
        })

    }
}