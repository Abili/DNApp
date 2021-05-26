package com.raisc.dnaapp.incompleteproject

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisc.dnaapp.R
import com.raisc.dnaapp.adapters.IncompleteProjectsAdapter
import com.raisc.dnaapp.databinding.ActivityIncompleteBinding
import com.raisc.dnaapp.model.Project
import com.raisc.dnaapp.newproject.NewProject

class IncompleteActivity : AppCompatActivity(), IncompleteProjectsAdapter.OnItemClick {
    private var binding: ActivityIncompleteBinding? = null
    var viewModel: IncompleteViewModel? = null
    var factory: IncompleteProjectViewModelFactory? = null
    var incompleteAdapter: IncompleteProjectsAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncompleteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        incompleteAdapter = IncompleteProjectsAdapter(this)

        factory = IncompleteProjectViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory!!).get(IncompleteViewModel::class.java)
        viewModel!!.getIncompleteProjects().observe(this, { items ->
            items.let {
                incompleteAdapter!!.submitList(items)
            }
            populateRecyclerView()
        })


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
        val project = viewModel?.getIncompleteProjects()?.value?.get(adapterPosition)
        val menuDialog = PopupMenu(this, itemView.rootView)
        menuDialog.inflate(R.menu.menu_incomplete)
        menuDialog.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.edit -> {
                        val intent = Intent(this@IncompleteActivity, NewProject::class.java)
                        intent.putExtra("project", project)
                        startActivity(intent)
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