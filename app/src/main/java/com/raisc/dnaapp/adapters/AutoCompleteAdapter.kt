package com.raisc.dnaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.raisc.dnaapp.model.Project

class AutoCompleteAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int, private val allPois: List<Project>
) :
    ArrayAdapter<Project>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<Project> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): Project {
        return mPois[p0]

    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return mPois.get(p0).id?.toLong()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView?
            ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = "${mPois[position].projectName} (${mPois[position].clientName})"
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                mPois = filterResults.values as List<Project>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.projectName!!.toLowerCase().contains(queryString) ||
                                it.clientName!!.toLowerCase().contains(queryString) ||
                                it.projectLocation!!.toLowerCase().contains(queryString)
                    }

                return filterResults
            }

        }
    }
}