package com.coding.expensemanager.apdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.coding.expensemanager.R
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.util.Constants.Companion.INCOME
import com.coding.expensemanager.util.UtilClass
import com.google.android.material.button.MaterialButton

private const val TAG = "MonthlyCardAdapter"
class MonthlyCardAdapter(val map: Map<Int, List<TransactionsTable>>) :ListAdapter<Int,MonthlyCardAdapter.MonthlyViewHolder>(MonthlyDiffCallBack()) {
    private var listener:ChangeFragment?=null
    inner class MonthlyViewHolder(val view:View):RecyclerView.ViewHolder(view) {
        private val monthName: TextView =view.findViewById(R.id.month_name)
        private val indicator: TextView =view.findViewById(R.id.indicator)
        private val budgetExceed: TextView =view.findViewById(R.id.budget_exceed_)
        private val monthlyRecyclerView: RecyclerView =view.findViewById(R.id.monthly_list_recler_view)
        private val monthlyViewAll: MaterialButton =view.findViewById(R.id.monthly_view_all)
        fun onBind(item: Int) {
            monthName.text=UtilClass.getMonth(item)
            val monthlyIndicatorExceed= map[item]?.let { checkForExceed(it) }
            val year: Int = map[item]?.let { getYear(it) }!!
            if (monthlyIndicatorExceed == true){
                budgetExceed.visibility=View.INVISIBLE
                indicator.setBackgroundColor(ContextCompat.getColor(view.context, R.color.income_default_color))
            }else{
                indicator.setBackgroundColor(ContextCompat.getColor(view.context, R.color.expense_default_color))
                budgetExceed.visibility=View.VISIBLE
            }
            monthlyRecyclerView.apply {
                layoutManager=LinearLayoutManager(view.context)
                adapter=CustomMonthlyWiseAdapter()
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(view.context, RecyclerView.VERTICAL))
            }
            monthlyViewAll.setOnClickListener {
                listener?.clickListener(item,year)

            }
            val list= mutableListOf<TransactionsTable>()
            val sortedList=map[item]?.let { list ->
                list.sortedByDescending {
                    it.timeStamp
                }
            }
            sortedList!!.forEach {
                Log.d(TAG, "onBind: $it")
            }
            if (sortedList.isNotEmpty()){
                var counter=0
                while (counter< sortedList.size && counter<=2 ){
                    list.add(sortedList[counter])
                    counter+=1
                }
            }
            (monthlyRecyclerView.adapter as CustomMonthlyWiseAdapter).submitList(list)
        }

        private fun getYear(it: List<TransactionsTable>):Int {
            return it[0].year
        }

        private fun checkForExceed(list: List<TransactionsTable>): Boolean {
            var expense=0
            var income=0
            list.forEach {
                if(it.transactionType== INCOME){
                    income+=it.amount
                }else{
                    expense+=it.amount
                }
            }
            return income>expense
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.monthly_view,parent,false)
        listener=view.context as ChangeFragment
        return MonthlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthlyViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
    interface ChangeFragment{
        fun clickListener(item: Int, year: Int)
    }

}

class MonthlyDiffCallBack: DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
      return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem==newItem
    }

}
