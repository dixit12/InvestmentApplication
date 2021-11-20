package com.dixitgarg.investmentapplication.basicadapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BasicRecyclerViewAdapter(
    private var items: ArrayList<AdapterDelegateModuleItem>,
    private val delegates: AdapterDelegatesManager<List<AdapterDelegateModuleItem>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return delegates.getItemViewType(items, position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.onCreateViewHolder(viewGroup, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates.onBindViewHolder(items, position, holder)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun getItems(): ArrayList<AdapterDelegateModuleItem> {
        return items
    }
}