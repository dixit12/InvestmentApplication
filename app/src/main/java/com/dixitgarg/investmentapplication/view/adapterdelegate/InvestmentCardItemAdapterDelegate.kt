package com.dixitgarg.investmentapplication.view.adapterdelegate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dixitgarg.investmentapplication.basicadapterdelegate.AdapterDelegate
import com.dixitgarg.investmentapplication.basicadapterdelegate.AdapterDelegateModule
import com.dixitgarg.investmentapplication.basicadapterdelegate.AdapterDelegateModuleItem
import com.dixitgarg.investmentapplication.model.Investment
import com.sew.investmentapplication.R

class InvestmentCardItemAdapterDelegate() : AdapterDelegate<List<AdapterDelegateModuleItem>>() {
    private val module by lazy {
        MyAdapterDelegateModule()
    }

    override fun isForViewType(items: List<AdapterDelegateModuleItem>, position: Int): Boolean {
        return items[position] is MyAdapterDelegateModule.ModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ModuleViewHolder(module.init(LayoutInflater.from(parent.context), parent), module)
    }


    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ModuleViewHolder).cleanUpUI()
    }

    override fun onBindViewHolder(
        items: List<AdapterDelegateModuleItem>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        (holder as ModuleViewHolder).bindData(
            items[position] as MyAdapterDelegateModule.ModuleData
        )
    }


    class ModuleViewHolder(
        itemView: View,
        private val module: MyAdapterDelegateModule
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindData(
            data: MyAdapterDelegateModule.ModuleData
        ) {
            module.bindData(itemView, data)
        }

        fun cleanUpUI() {
            module.cleanUp()
        }
    }

    class MyAdapterDelegateModule : AdapterDelegateModule() {

        private var inflatedView: View? = null
        private var tvCompanyName: TextView? = null
        private var tvSpent: TextView? = null
        private var tvCurrent: TextView? = null

        private fun bindViews(itemView: View) {
            this.inflatedView = itemView
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName)
            tvSpent = itemView.findViewById(R.id.tvSpent)
            tvCurrent = itemView.findViewById(R.id.tvCurrent)
        }

        override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
            return layoutInflater.inflate(R.layout.layout_card_item_view, parent, false)
        }

        override fun cleanUp() {

        }

        @SuppressLint("SetTextI18n")
        fun bindData(
            itemView: View,
            data: ModuleData
        ) {
            this.bindViews(itemView)
            val dataItems = data.data
            tvCompanyName?.text =
                if (dataItems.place.isNotEmpty()) dataItems.place else "Test Owner"
            tvSpent?.text = "Spent " + "₹" + dataItems.spentAmount.toString()
            tvCurrent?.text = "₹" + dataItems.roundedAmount.toString()
        }

        data class ModuleData(var data: Investment) : AdapterDelegateModuleItem
    }
}