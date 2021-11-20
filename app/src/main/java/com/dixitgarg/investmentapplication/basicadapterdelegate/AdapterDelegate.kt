package com.dixitgarg.investmentapplication.basicadapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterDelegate<T> {
    /**
     * Called to determine whether this AdapterDelegate is the responsible for the given data
     * element.
     */
    abstract fun isForViewType(items: T, position: Int): Boolean

    /**
     * Creates the  [RecyclerView.ViewHolder] for the given data source item
     */
    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    /**
     * Called to bind the [RecyclerView.ViewHolder] to the item of the datas source set
     */
    abstract fun onBindViewHolder(
        items: T, position: Int,
        holder: RecyclerView.ViewHolder
    )

    /**
     * Called when a view created by this adapter has been detached from its window.
     */
    open fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}
}

