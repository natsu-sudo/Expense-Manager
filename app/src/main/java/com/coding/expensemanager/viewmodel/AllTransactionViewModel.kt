package com.coding.expensemanager.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.repository.AllTransactionRepository
import kotlinx.coroutines.launch

class AllTransactionViewModel(context: Context):ViewModel() {
    private val getAllTransactionRepo=AllTransactionRepository(context as Application).allTransactionDao

    fun saveTransaction(transaction: TransactionsTable){
        viewModelScope.launch {
            getAllTransactionRepo.insertInTransactionTable(transaction)
        }
    }

}