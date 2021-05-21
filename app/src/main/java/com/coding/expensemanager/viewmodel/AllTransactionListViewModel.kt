package com.coding.expensemanager.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.pojo.CurrentStatus
import com.coding.expensemanager.repository.AllTransactionListRepository
import com.coding.expensemanager.util.Constants

class AllTransactionListViewModel(context: Context) : ViewModel() {
    private val monthId = MutableLiveData(0)
    private val getListViewModel =
        AllTransactionListRepository(context as Application).allTransactionListDao

    /**
     * @param getLimitedTransactions: getLimited Transaction so that it can be reflected in Recycler View
     */
    val getLimitedTransactions: LiveData<List<TransactionsTable>> = getLimitedList()

    /**
     * @param getFullList: getFull Transaction so that it can be reflected in Recycler View
     */
    val getFullList: LiveData<List<TransactionsTable>> = getFullListFromTable()

    /**
     * @param getNetBalance: will give you the current Net Balance
     */
    val getNetBalance: LiveData<CurrentStatus> =
        Transformations.switchMap(getFullListFromTable(), ::getNetBalanceFromBD)


    private fun getLimitedList(): LiveData<List<TransactionsTable>> {
        return getListViewModel.getLimitedListFromTransactionTable()
    }

    private fun getFullListFromTable(): LiveData<List<TransactionsTable>> {
        return getListViewModel.getListOfAllTransactions()
    }

    val getShortMonthlyList: LiveData<Map<Int, List<TransactionsTable>>> = Transformations
        .switchMap(getMonthlyReport(), ::convertToMonthlyMap)

    val getYearlyList: LiveData<Map<Int, List<TransactionsTable>>> = Transformations
        .switchMap(getFullListFromTable(),::convertToYearlyMap)

    private fun convertToYearlyMap(list: List<TransactionsTable>): LiveData<Map<Int, List<TransactionsTable>>> {
        val temp = MutableLiveData<Map<Int, List<TransactionsTable>>>()
        val returnList = list.groupBy {
            it.year
        }
        temp.value = returnList
        return temp
    }


    /**
     * will return the List of transaction group by Month
     */
    private fun getMonthlyReport(): LiveData<List<TransactionsTable>> {
        return getListViewModel.getMonthlyReport()
    }

    /**
     * @param month:this method will update the month
     */
    fun updateMonth(month: Int) {
        monthId.value = month
        Log.d("TAG", "updateMonth: " + monthId.value)
    }


    /**
     * @param list:converting list to map so that we can show the data according to month
     */
    private fun convertToMonthlyMap(list: List<TransactionsTable>): LiveData<Map<Int, List<TransactionsTable>>> {
        val temp = MutableLiveData<Map<Int, List<TransactionsTable>>>()
        val returnList = list.groupBy {
            it.month
        }
        temp.value = returnList
        return temp
    }

    /**
     * @param list:Method which will return data class by transforming the list to TransactionTable
     */
    private fun getNetBalanceFromBD(list: List<TransactionsTable>): LiveData<CurrentStatus> {
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

    /**
     * @param getMonthlyList:an observer which will give the data in List if month will change
     */
    val getMonthlyList: LiveData<List<TransactionsTable>> = Transformations.switchMap(monthId) {
        Log.d("TAG", ": $monthId")
        getListViewModel.getListByMonth(it)
    }
    val getMonthlyNetBalance: LiveData<CurrentStatus> =
        Transformations.switchMap(getMonthlyList, ::getNetBalanceFromBD)


}