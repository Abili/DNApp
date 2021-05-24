package com.raisc.dnaapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raisc.dnaapp.databinding.ActivityMainBinding
import com.raisc.dnaapp.databinding.ActivityProgressBinding

class ProgressActivity : AppCompatActivity() {
    var binding: ActivityProgressBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}
