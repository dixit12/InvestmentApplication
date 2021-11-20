package com.dixitgarg.investmentapplication.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dixitgarg.investmentapplication.model.Investment

@Dao
interface InvestmentDAO {

    @get:Query("SELECT * FROM InvestmentData")
    val getInvestmentList: LiveData<List<Investment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestmentData(investmentList:ArrayList<Investment>)

}