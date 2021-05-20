package com.coding.expensemanager.apdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coding.expensemanager.R
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.util.Constants

class CalendarAdapter(
    private val daysInMonth: MutableList<String>,
    private val map: Map<Int, List<TransactionsTable>>
) : RecyclerView.Adapter<CalendarAdapter.MonthlyCalenderViewHolder>() {
    inner class MonthlyCalenderViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        private val date:TextView=view.findViewById(R.id.cell_date)
        private val incomeAmount:TextView=view.findViewById(R.id.income_cell)
        private val expenseAmount:TextView=view.findViewById(R.id.expense_cell)

        fun onBind(item: String) {
            if (item.isNotEmpty()){
                date.text= item
                if(map[item.toInt()]!=null){
                    val income=sumOfInCome(map[item.toInt()]!!,Constants.INCOME)
                    val expense=sumOfInCome(map[item.toInt()]!!,Constants.EXPENSE)
                    if (income!=0){
                        incomeAmount.visibility=View.VISIBLE
                        incomeAmount.text=format(income.toString())
                    }
                    if (expense!=0){
                        expenseAmount.visibility=View.VISIBLE
                        expenseAmount.text=format(expense.toString())
                    }
                }

            }
        }

        private fun format(returnAmount: String): String {
            if (returnAmount.length>5){
                return returnAmount.substring(0,5)+".."
            }
            return returnAmount
        }

        private fun sumOfInCome(list: List<TransactionsTable>, type:Int): Int {
          var amount=0
            list.forEach {
                if (it.transactionType==type){
                    amount+=it.amount
                }
            }
            return amount
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyCalenderViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
        val viewGroup:ViewGroup.LayoutParams=view.layoutParams
        viewGroup.height= (parent.height*0.16666666).toInt()
        return MonthlyCalenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthlyCalenderViewHolder, position: Int) {
        return holder.onBind(daysInMonth[position])
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyCalenderViewHolder {
//        val view =LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
//        val viewGroup:ViewGroup.LayoutParams=view.layoutParams
//        viewGroup.height= (parent.height*0.16666666).toInt()
//        return MonthlyCalenderViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MonthlyCalenderViewHolder, position: Int) {
//        return holder.onBind(getItem(position))
//    }


}

