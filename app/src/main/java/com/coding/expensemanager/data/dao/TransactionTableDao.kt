package com.coding.expensemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.coding.expensemanager.pojo.TransactionsTable

@Dao
interface TransactionTableDao {
    @Query("SELECT * FROM transaction_table WHERE id =:id")
    fun getListExpense(id:Int): LiveData<TransactionsTable>

    @Insert
    suspend fun insertInTransactionTable(transactionsTable: TransactionsTable):Long

    @Update
    suspend fun updateInTransactionTable(transactionsTable: TransactionsTable)

}