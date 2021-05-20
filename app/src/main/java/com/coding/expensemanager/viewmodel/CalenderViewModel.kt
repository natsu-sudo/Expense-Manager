package com.coding.expensemanager.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.repository.AllTransactionListRepository

class CalenderViewModel(context: Context) : ViewModel() {
    private val getListViewModel =
        AllTransactionListRepository(context as Application).allTransactionListDao
    private val monthId = MutableLiveData(0)
    fun updateMonth(month: Int) {
        monthId.value = month
    }

    private val getMonthlyList: LiveData<List<TransactionsTable>> = Transformations.switchMap(monthId){
        getListViewModel.getListByMonth(it)
    }

    val getMonthlyMap: LiveData<Map<Int,List<TransactionsTable>>> = Transformations.switchMap(getMonthlyList,::getMonthlyReportByDay)

    private fun getMonthlyReportByDay(list: List<TransactionsTable>?): LiveData<Map<Int, List<TransactionsTable>>> {
        val mappedReport = MutableLiveData<Map<Int, List<TransactionsTable>>>()

        if (list != null) {
            mappedReport.value= list.groupBy {
                it.day
            }
        }
        Log.d("TAG", "getMonthlyReportByDay: $mappedReport")
        return mappedReport

    }


}