package com.raisc.dnaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisc.dnaapp.R
import com.raisc.dnaapp.databinding.IncompleteProjectModelBinding
import com.raisc.dnaapp.model.Project
import com.raisc.dnaapp.ui.ProgressActivity


class IncompleteProjectsAdapter(private val onItemClicklistener: OnItemClick) :
    ListAdapter<Project, IncompleteProjectsAdapter.BarsViewHolder>(BarsComparator()) {

    interface OnItemClick {
        fun onItemClicked(adapterPosition: Int, itemView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarsViewHolder {
        return BarsViewHolder.create(parent, onItemClicklistener)
    }

    override fun onBindViewHolder(holder: BarsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)

    }

    class BarsViewHolder(
        private val binding: IncompleteProjectModelBinding,
        private val context: Context, onClickItem: OnItemClick
    ) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {
        private var onItemClicklistener: OnItemClick = onClickItem
        fun bind(item: Project) {
            binding.projectName.text = item.projectName
            binding.clientName.text = item.clientName
            Glide.with(context).load(item.clientPic).into(binding.clientImageView)
            itemView.setOnClickListener {
                val intent = Intent(context, ProgressActivity::class.java)
                intent.putExtra("pendingProject", item.projectName)
                context.startActivity(intent)
            }
            binding.incompleteSettings.setOnClickListener {
                val settingsDialog = PopupMenu(context, binding.incompleteSettings)
                settingsDialog.inflate(R.menu.menu_incomplete)
                settingsDialog.show()
                settingsDialog.setOnMenuItemClickListener(this)

            }
        }

        companion object {

            fun create(parent: ViewGroup, onClickItem: OnItemClick): BarsViewHolder {
                val binding = IncompleteProjectModelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return BarsViewHolder(binding, parent.context, onClickItem)
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            onItemClicklistener.onItemClicked(bindingAdapterPosition, itemView)
            return true
        }
    }

    class BarsComparator : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            if (oldItem.projectName != newItem.projectName ||
                oldItem.projectLocation != newItem.projectLocation ||
                oldItem.clientPic != newItem.clientPic
            )
                return false

            return oldItem.projectName == newItem.projectName ||
                    oldItem.projectLocation == newItem.projectLocation ||
                    oldItem.clientPic == (newItem.clientPic)
        }
    }
}