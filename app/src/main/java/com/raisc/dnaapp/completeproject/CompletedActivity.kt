package com.raisc.dnaapp.completeproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raisc.dnaapp.databinding.ActivityCompleteBinding

class CompletedActivity : AppCompatActivity() {
    var binding: ActivityCompleteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}