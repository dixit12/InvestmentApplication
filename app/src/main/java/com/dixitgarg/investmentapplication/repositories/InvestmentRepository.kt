package com.dixitgarg.investmentapplication.repositories

import androidx.lifecycle.LiveData

import androidx.annotation.WorkerThread
import com.dixitgarg.investmentapplication.model.Investment
import com.dixitgarg.investmentapplication.roomdb.InvestmentDAO


class InvestmentRepository(private val investmentDAO: InvestmentDAO){

    fun getInvestmentList():LiveData<List<Investment>> {
        return investmentDAO.getInvestmentList
    }

}