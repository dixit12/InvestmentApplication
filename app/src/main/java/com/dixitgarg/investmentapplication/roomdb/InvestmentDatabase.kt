package com.dixitgarg.investmentapplication.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dixitgarg.investmentapplication.utils.Utility.Companion.readJSONFromAsset
import com.dixitgarg.investmentapplication.model.Investment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Database(
    entities = [Investment::class],
    version = 11,
    exportSchema = false
)

abstract class InvestmentDatabase: RoomDatabase() {

    abstract fun getInvestmentDAO() : InvestmentDAO

    companion object {
        @Volatile
        var databaseInstance: InvestmentDatabase ?= null
        val DB_NAME = "investment.db"

        fun getDatabase(context: Context): InvestmentDatabase {
            return databaseInstance ?: synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, InvestmentDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(InvestorDatabaseCallback(context))
                        .build()
                    databaseInstance = instance
                    instance
                }
        }

        private class InvestorDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                val scopeIO = CoroutineScope(Job() + Dispatchers.IO)
                databaseInstance?.let { database ->
                    scopeIO.launch{
                        val jsonObj = JSONObject(readJSONFromAsset(context))
                        val responseData = jsonObj.optJSONObject("response_data") ?: JSONObject()
                        val transactionsArray = responseData.optJSONArray("Transactions") ?: JSONArray()
                        val transactionsList = ArrayList<Investment>()
                        for (i in 0 until transactionsArray.length()){
                            val investment = Investment.mapWithJson(transactionsArray.optJSONObject(i))
                            transactionsList.add(investment)
                        }
                        populateDatabase(database, transactionsList)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: InvestmentDatabase, investmentList: ArrayList<Investment>) {
            val investmentDao = database.getInvestmentDAO()
            investmentDao.insertInvestmentData(investmentList)
        }
    }
}