package com.raisc.dnaapp.incompleteproject

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisc.dnaapp.R
import com.raisc.dnaapp.databinding.ActivityIncompleteBinding
import com.raisc.dnaapp.newproject.NewProject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class IncompleteActivity : AppCompatActivity(), IncompleteProjectsAdapter.OnItemClick {
    private var binding: ActivityIncompleteBinding? = null
    var viewModel: IncompleteViewModel? = null
    var factory: IncompleteProjectViewModelFactory? = null
    private var incompleteAdapter: IncompleteProjectsAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private val project : Project?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncompleteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        incompleteAdapter = IncompleteProjectsAdapter(this)

        factory = IncompleteProjectViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory!!).get(IncompleteViewModel::class.java)

        lifecycleScope.launch {
            getIncompleteProjects()
        }

    }

    private suspend fun getIncompleteProjects() {
        viewModel!!.getIncompleteProjects().collectLatest { items ->
            items.let {
                incompleteAdapter!!.submitData(items)
            }
            populateRecyclerView()
        }

    }

    private fun populateRecyclerView() {
        binding?.incompleteRecycler?.adapter = incompleteAdapter
        layoutManager = LinearLayoutManager(this@IncompleteActivity)
        binding?.incompleteRecycler?.layoutManager = layoutManager
        val mItemDecoration =
            DividerItemDecoration(binding?.incompleteRecycler?.context, layoutManager!!.orientation)
        binding?.incompleteRecycler?.addItemDecoration(mItemDecoration)
    }

    override fun onItemClicked(adapterPosition: Int, itemView: View) {
        val project = get()
        )
        val menuDialog = PopupMenu(this, itemView.rootView)
        menuDialog.inflate(R.menu.menu_incomplete)
        menuDialog.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.edit -> {
                        val intent = Intent(this@IncompleteActivity, NewProject::class.java)
//                        intent.putExtra("project", project)
//                        startActivity(intent)
                    }
                    R.id.delete -> {
                        viewModel?.delete(project!!)
                    }
                    R.id.mark -> {
                        viewModel!!.markComplete(project!!)
                    }
                }
                return true
            }
        })
        menuDialog.show()

    }
}