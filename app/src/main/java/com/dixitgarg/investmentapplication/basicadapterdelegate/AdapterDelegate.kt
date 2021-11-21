package com.dixitgarg.investmentapplication.basicadapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterDelegate<T> {

    abstract fun isForViewType(items: T, position: Int): Boolean

    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(items: T, position: Int, holder: RecyclerView.ViewHolder)

    open fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}
}

