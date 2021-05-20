package com.coding.expensemanager.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.repository.AllTransactionListRepository
import com.coding.expensemanager.util.Constants

class GenericViewModel(context: Context) : ViewModel()  {
    private val getListViewModel =
        AllTransactionListRepository(context as Application).allTransactionListDao

    val getIncomeList: LiveData<List<TransactionsTable>> = getIncomeListFromDB(Constants.INCOME)
    val getExpenseList: LiveData<List<TransactionsTable>> = getIncomeListFromDB(Constants.EXPENSE)
    val totalIncome:LiveData<Int> = getTotalIncome(Constants.INCOME)
    val totalExpense:LiveData<Int> = getTotalIncome(Constants.EXPENSE)

    private fun getTotalIncome(income: Int): LiveData<Int> {
        return getListViewModel.getSumFromAllTransactionTable(income)
    }

    private fun getIncomeListFromDB(income: Int): LiveData<List<TransactionsTable>> {
        return getListViewModel.getIncomeOrExpenseList(income)
    }



}