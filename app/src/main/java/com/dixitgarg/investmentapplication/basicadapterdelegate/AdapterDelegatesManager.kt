package com.dixitgarg.investmentapplication.basicadapterdelegate

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

class AdapterDelegatesManager<T> {

    protected var delegates: SparseArrayCompat<AdapterDelegate<T>?> = SparseArrayCompat<AdapterDelegate<T>?>()
    protected var fallbackDelegate: AdapterDelegate<T>? = null

    fun addDelegate(
        viewType: Int,
        delegate: AdapterDelegate<T>
    ): AdapterDelegatesManager<T> {
        return addDelegate(viewType, false, delegate)
    }

    private fun addDelegate(
        viewType: Int, allowReplacingDelegate: Boolean,
        delegate: AdapterDelegate<T>
    ): AdapterDelegatesManager<T> {
        if (delegate == null) {
            throw NullPointerException("AdapterDelegate is null!")
        }
        require(viewType != FALLBACK_DELEGATE_VIEW_TYPE) {
            ("Please use another view type.")
        }
        require(!(!allowReplacingDelegate && delegates[viewType] != null)) {
            ("An AdapterDelegate is already registered for the viewType.")
        }
        delegates.put(viewType, delegate)
        return this
    }

    fun getItemViewType(items: T, position: Int): Int {
        if (items == null) {
            throw NullPointerException("Items datasource is null!")
        }
        val delegatesCount = delegates.size()
        for (i in 0 until delegatesCount) {
            val delegate = delegates.valueAt(i)
            if (delegate!!.isForViewType(items, position)) {
                return delegates.keyAt(i)
            }
        }
        if (fallbackDelegate != null) {
            return FALLBACK_DELEGATE_VIEW_TYPE
        }
        throw NullPointerException(
            "No AdapterDelegate added that matches position=$position in data source"
        )
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = getDelegateForViewType(viewType)
            ?: throw NullPointerException("No AdapterDelegate added for ViewType $viewType")
        return delegate.onCreateViewHolder(parent)
    }

    fun onBindViewHolder(items: T, position: Int, holder: RecyclerView.ViewHolder) {
        val delegate = getDelegateForViewType(holder.itemViewType)
            ?: throw NullPointerException("No delegate found for item at this position")
        delegate.onBindViewHolder(items, position, holder
        )
    }

    private fun getDelegateForViewType(viewType: Int): AdapterDelegate<T>? {
        return delegates[viewType, fallbackDelegate]
    }

    companion object {
        const val FALLBACK_DELEGATE_VIEW_TYPE = Int.MAX_VALUE - 1
    }
}