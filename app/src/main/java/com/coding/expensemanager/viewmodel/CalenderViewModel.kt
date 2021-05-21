package com.coding.expensemanager.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.coding.expensemanager.pojo.MonthYear
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.repository.AllTransactionListRepository

class CalenderViewModel(context: Context) : ViewModel() {
    private val getListViewModel =
        AllTransactionListRepository(context as Application).allTransactionListDao
    private val monthAndYearId = MutableLiveData<MonthYear>()
    fun updateMonthAndYear(id: MonthYear) {
        monthAndYearId.value=id
    }

    private val getMonthlyList: LiveData<List<TransactionsTable>> = Transformations.switchMap(monthAndYearId){
        getListViewModel.getListByMonthAndYear(it.month,it.year)
    }

    val getMonthlyMap: LiveData<Map<Int,List<TransactionsTable>>> = Transformations.switchMap(getMonthlyList,::getMonthlyReportByDay)

    private fun getMonthlyReportByDay(list: List<TransactionsTable>?): LiveData<Map<Int, List<TransactionsTable>>> {
        val mappedReport = MutableLiveData<Map<Int, List<TransactionsTable>>>()
        if (list != null) {
            mappedReport.value= list.groupBy {
                it.day
            }
        }
        return mappedReport

    }


}