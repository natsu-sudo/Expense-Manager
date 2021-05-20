package com.coding.expensemanager.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.coding.expensemanager.data.database.AllTransactionDatabase
import com.coding.expensemanager.pojo.TransactionsTable

/**
 * @property A repsitory to aceess data from database
 */

class AllTransactionListRepository(context: Context) {
    val allTransactionListDao= AllTransactionDatabase.getDatabase(context.applicationContext)
        .transactionListDao()

}