package com.coding.expensemanager.repository

import android.content.Context
import com.coding.expensemanager.data.database.AllTransactionDatabase

/**
 * @property ; A reposition uusing which you can saved/edit in data
 */

class AllTransactionRepository(context: Context) {
    val allTransactionDao=
        AllTransactionDatabase.getDatabase(context.applicationContext).transactionDao()


}