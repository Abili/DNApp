package com.raisc.dnaapp.pendingproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raisc.dnaapp.adapters.ProjectsAdapter
import com.raisc.dnaapp.adapters.AutoCompleteAdapter
import com.raisc.dnaapp.databinding.ActivityPendingBinding
import com.raisc.dnaapp.model.Project

class PendingActivity : AppCompatActivity() {
    private var binding: ActivityPendingBinding? = null
    var viewModel: PendingViewModel? = null

    private var mDatabase: DatabaseReference? = null
    private var adapter: ProjectsAdapter? = null
    private val mFirebaseDatabase: FirebaseDatabase? = null
    private var mItemDecoration: DividerItemDecoration? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val viewModelFactory = PendingProjectViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PendingViewModel::class.java)


        //mDatabase = FirebaseDatabase.getInstance().reference.child("pending")
        //bars_Local_clubs = ArrayList()


//sample data // realdata will be got from webService
        binding!!.pendingRecycler.adapter = adapter
        viewModel!!.getPendingProjects().observe(this, { items ->
            items.let {
                adapter!!.submitList(items)
                binding!!.shimmerViewContainer.stopShimmer()
                binding!!.shimmerViewContainer.visibility = View.GONE
            }

            populateRecyclerView()
            setSearchView()

        })
//        binding.search_location.setOnClickListener {
//            enableSearchView()
//        }

        viewModel!!.getPendingProjects().observe(this) { items ->
            // Update the cached copy of the students in the adapter.
            items.let {
                val arr = mutableListOf<Project>()

                for (value in it) {
                    //arr.add("${value.name} (${value.location})")
                    arr.add(value)
                }
                val adapter =
                    AutoCompleteAdapter(this, android.R.layout.simple_list_item_1, arr)
                binding!!.searchTv.setAdapter(adapter)
                binding!!.searchTv.threshold = 1

                binding!!.searchTv.setOnItemClickListener { parent, view, position, id ->
                    var selectedPoi = parent.adapter.getItem(position) as Project
                    //val text = "${arr[position].name} (${arr[position].location})"
                    /// selectedPoi = arr[position].name

                    binding!!.searchTv.setText(selectedPoi.projectName)
                    fetchUserDetails(selectedPoi.projectName)
                }
            }
        }
    }


    private fun populateRecyclerView() {
        layoutManager = LinearLayoutManager(this@PendingActivity)
        binding?.pendingRecycler?.layoutManager = layoutManager
        mItemDecoration =
            DividerItemDecoration(binding?.pendingRecycler?.context, layoutManager!!.orientation)
        binding?.pendingRecycler?.addItemDecoration(mItemDecoration!!)
    }


    companion object {
        const val EXTRA_ITEM = "item"
    }

    private fun setSearchView() {
        binding?.searchLocation?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fetchUserDetails(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                fetchUserDetails(newText)
                return false
            }
        })

    }

    private fun fetchUserDetails(newText: String) {
        var eText = newText.toLowerCase()
        if (eText.length < 2) {
            return
        } else {
            eText = eText.substring(0, 1).toUpperCase() + eText.substring(1).toLowerCase()
        }
        if (eText.isEmpty()) {
            Toast.makeText(
                this,
                "Text Required",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            viewModel!!.getPendingProject(eText).observe(this) { clubs ->
                clubs.let {
                    adapter?.submitList(clubs)
                    binding?.shimmerViewContainer?.stopShimmer()
                    binding?.shimmerViewContainer?.visibility = View.GONE


                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        populateRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        binding?.shimmerViewContainer?.startShimmer()
    }

    override fun onPause() {
        binding?.shimmerViewContainer?.stopShimmer()
        super.onPause()
    }
}
