package com.dixitgarg.investmentapplication.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dixitgarg.investmentapplication.view.adapterdelegate.InvestmentCardHeaderAdapterDelegate
import com.dixitgarg.investmentapplication.view.adapterdelegate.InvestmentCardItemAdapterDelegate
import com.dixitgarg.investmentapplication.basicadapterdelegate.AdapterDelegateModuleItem
import com.dixitgarg.investmentapplication.basicadapterdelegate.AdapterDelegatesManager
import com.dixitgarg.investmentapplication.basicadapterdelegate.BasicRecyclerViewAdapter
import com.dixitgarg.investmentapplication.utils.ViewType
import com.dixitgarg.investmentapplication.model.Investment
import com.dixitgarg.investmentapplication.viewModel.InvestmentViewModel
import com.sew.investmentapplication.R
import kotlinx.android.synthetic.main.activity_investment.*


class InvestmentActivity : AppCompatActivity() {

    private lateinit var investmentViewModel: InvestmentViewModel
    private var transactionsList = ArrayList<Investment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investment)
        this.initViewModel()
        this.setUpRecyclerView()
        investmentViewModel.getInvestmentListAsLiveData().observe(this, {
            this.transactionsList.clear()
            this.transactionsList = it as ArrayList<Investment>
            this.inflateDataToUI()
        })
    }

    private fun inflateDataToUI() {
        var totalAmount = 0
        transactionsList.sortedBy { it.time }
        transactionsList.reverse()
        val finalTransactionList = transactionsList.groupBy { it.convertedTime }
        for (key in finalTransactionList.keys) {
            finalTransactionList[key]?.forEach {
                totalAmount += it.roundedAmount
            }
        }
        roundedAmountCircle?.maxValue = 5000f
        roundedAmountCircle?.progress = totalAmount.toFloat()
        setUpAdapterDelegate(finalTransactionList)
    }

    private fun setUpAdapterDelegate(finalTransactionList: Map<String, List<Investment>>) {
        val arrayList = ArrayList<AdapterDelegateModuleItem>()
        for (key in finalTransactionList.keys) {
            arrayList.add(
                InvestmentCardHeaderAdapterDelegate.MyAdapterDelegateModule.ModuleData(
                    finalTransactionList[key]!! as ArrayList<Investment>
                )
            )
            for (i in finalTransactionList[key]?.indices!!) {
                arrayList.add(
                    InvestmentCardItemAdapterDelegate.MyAdapterDelegateModule.ModuleData(
                        finalTransactionList[key]!![i]
                    )
                )
            }
        }
        rcvInvestmentDetails?.adapter = BasicRecyclerViewAdapter(arrayList, getDelegateManagerList())

    }

    private fun setUpRecyclerView() {
        rcvInvestmentDetails?.setHasFixedSize(true)
        rcvInvestmentDetails?.layoutManager = LinearLayoutManager(this)
        rcvInvestmentDetails?.itemAnimator = DefaultItemAnimator()
    }

    private fun initViewModel() {
        investmentViewModel = ViewModelProvider(this)[InvestmentViewModel::class.java]
    }

    private fun getDelegateManagerList(): AdapterDelegatesManager<List<AdapterDelegateModuleItem>> {
        val delegatesManager = AdapterDelegatesManager<List<AdapterDelegateModuleItem>>()
        delegatesManager.addDelegate(ViewType.HEADER_TYPE, InvestmentCardHeaderAdapterDelegate())
        delegatesManager.addDelegate(ViewType.ITEM_TYPE, InvestmentCardItemAdapterDelegate())
        return delegatesManager
    }

}