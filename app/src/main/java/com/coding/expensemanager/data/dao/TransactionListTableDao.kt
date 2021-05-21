package com.coding.expensemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.coding.expensemanager.pojo.MonthYear
import com.coding.expensemanager.pojo.TransactionsTable

@Dao
interface TransactionListTableDao {
    @Query("SELECT * FROM transaction_table ORDER By timestamp DESC")
    fun getListOfAllTransactions():LiveData<List<TransactionsTable>>

    @Query("SELECT SUM(amount) FROM transaction_table WHERE transaction_type== :transactionType")
    fun getSumFromAllTransactionTable(transactionType:Int):LiveData<Int>

    @Query("SELECT * FROM transaction_table ORDER By timestamp DESC LIMIT 4")
    fun getLimitedListFromTransactionTable():LiveData<List<TransactionsTable>>

    @Query("SELECT SUM(amount) FROM transaction_table WHERE transaction_type= :transactionType AND transaction_name== :transactionMethod")
    fun getSumOfTransactionTypeFromAll(transactionType:Int,transactionMethod:String):LiveData<Int>

    @Query("SELECT * FROM transaction_table ORDER BY month  AND timestamp")
    fun getMonthlyReport():LiveData<List<TransactionsTable>>

    @Query("SELECT * FROM transaction_table WHERE month==:month ORDER By timestamp DESC")
    fun getListByMonth(month:Int):LiveData<List<TransactionsTable>>

    @Query("SELECT * FROM transaction_table WHERE transaction_type == :transactionType ORDER BY timestamp DESC")
    fun getIncomeOrExpenseList(transactionType: Int):LiveData<List<TransactionsTable>>

    @Query("SELECT * FROM transaction_table WHERE month == :month AND year == :year ORDER BY timestamp DESC")
    fun getListByMonthAndYear(month: Int,year: Int):LiveData<List<TransactionsTable>>










}