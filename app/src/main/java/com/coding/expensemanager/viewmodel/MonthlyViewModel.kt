package com.coding.expensemanager.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.coding.expensemanager.pojo.CurrentStatus
import com.coding.expensemanager.pojo.MonthYear
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.repository.AllTransactionListRepository
import com.coding.expensemanager.util.Constants

class MonthlyViewModel(context: Context) : ViewModel() {
    private val getListViewModel =
        AllTransactionListRepository(context as Application).allTransactionListDao
    private val monthAndYearId = MutableLiveData<MonthYear>()
    private val yearId = MutableLiveData(0)
    fun updateMonth(id: MonthYear) {
        id.also { monthAndYearId.value = it }
    }

    fun updateYear(year:Int){
        yearId.value = year
    }

    val getMonthlyList: LiveData<List<TransactionsTable>> = Transformations.switchMap(monthAndYearId,::getList)

    val getNetBalance: LiveData<CurrentStatus> =Transformations.switchMap(getMonthlyList,::getStatus)

    private fun getStatus(list: List<TransactionsTable>): LiveData<CurrentStatus> {
        var otherInCome = 0
        var cardIncome = 0
        var upiIncome = 0
        var expense = 0
        var income = 0
        var othersExpense = 0
        var cardExpense = 0
        var upiExpense = 0
        val currentStatus = MutableLiveData<CurrentStatus>()
        list.forEach {
            if (it.transactionType == Constants.EXPENSE) {
                expense += it.amount
                when (it.transactionMethod) {
                    Constants.CARD -> {
                        cardExpense += it.amount
                    }
                    Constants.UPI -> {
                        upiExpense += it.amount
                    }
                    else -> {
                        othersExpense += it.amount
                    }
                }
            } else {
                income += it.amount
                when (it.transactionMethod) {
                    Constants.CARD -> {
                        cardIncome += it.amount
                    }
                    Constants.UPI -> {
                        upiIncome += it.amount
                    }
                    else -> {
                        otherInCome += it.amount
                    }
                }
            }
        }
        currentStatus.value = CurrentStatus(
            income - expense,
            otherInCome - othersExpense,
            cardIncome - cardExpense,
            upiIncome - upiExpense,
            income,
            expense
        )
        return currentStatus
    }

    private fun getList(monthYear: MonthYear): LiveData<List<TransactionsTable>> {
        return  getListViewModel.getListByMonthAndYear(monthYear.month,monthYear.year)
    }
}