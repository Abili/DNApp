package com.raisc.dnaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisc.dnaapp.databinding.ProjectModelBinding
import com.raisc.dnaapp.model.Project
import com.raisc.dnaapp.ui.ProgressActivity


class ProjectsAdapter : ListAdapter<Project, ProjectsAdapter.BarsViewHolder>(BarsComparator()) {

    interface OnItemClickListener {
        fun onItemClicked(adapterPosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarsViewHolder {
        return BarsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BarsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)

    }

    class BarsViewHolder(private val binding: ProjectModelBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Project) {
            binding.projectName.text = item.projectName
            binding.clientName.text = item.clientName
            Glide.with(context).load(item.clientPic).into(binding.clientImageView)
            itemView.setOnClickListener {
                val intent = Intent(context, ProgressActivity::class.java)
                intent.putExtra("pendingProject", item.projectName)
                context.startActivity(intent)
            }

        }

        companion object {
            fun create(parent: ViewGroup): BarsViewHolder {
                val binding = ProjectModelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BarsViewHolder(binding, parent.context)
            }
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