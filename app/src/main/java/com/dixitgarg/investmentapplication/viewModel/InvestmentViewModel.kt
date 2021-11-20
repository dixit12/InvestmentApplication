package com.dixitgarg.investmentapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dixitgarg.investmentapplication.roomdb.InvestmentDatabase
import com.dixitgarg.investmentapplication.model.Investment
import com.dixitgarg.investmentapplication.repositories.InvestmentRepository

class InvestmentViewModel(application:Application) : AndroidViewModel(application) {

    private val investmentRepository: InvestmentRepository

    init {
        val investmentDAO = InvestmentDatabase.getDatabase(application).getInvestmentDAO()
        investmentRepository = InvestmentRepository(investmentDAO)
    }

    fun getInvestmentListAsLiveData(): LiveData<ArrayList<Investment>>{
        return investmentRepository.getInvestmentList() as LiveData<ArrayList<Investment>>
    }
}