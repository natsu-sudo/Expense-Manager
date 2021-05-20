package com.coding.expensemanager.apdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coding.expensemanager.R
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.util.Constants
import com.coding.expensemanager.util.UtilClass

class TransactionAdapter :ListAdapter<TransactionsTable,TransactionAdapter.ViewHolder>(DiffCallBack()){
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        private val purchaseName: TextView =view.findViewById(R.id.transaction_name)
        private val timestamp: TextView =view.findViewById(R.id.transaction_time)
        private val amount: TextView =view.findViewById(R.id.transaction_amount)
        fun onBind(item: TransactionsTable) {
            purchaseName.text=item.transactionName
//            timestamp.text= "${UtilClass.convertToDateAndTime(item.savedDate)}\n${item.transactionMethod}"
            timestamp.text= "${UtilClass.convertToTime(item.timeStamp)} ${UtilClass.convertToDate(item.savedDate)}\n${item.transactionMethod}"
            if (item.transactionType == Constants.INCOME){
                amount.setTextColor(ContextCompat.getColor(view.context, R.color.income_default_color))
                amount.text= "+ ${item.amount}"
            }else{
                amount.setTextColor(ContextCompat.getColor(view.context, R.color.expense_default_color))
                amount.text= "- ${item.amount}"
            }

        }

    }
    private class DiffCallBack:DiffUtil.ItemCallback<TransactionsTable>() {
        override fun areItemsTheSame(oldItem: TransactionsTable, newItem: TransactionsTable): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: TransactionsTable, newItem: TransactionsTable): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.transaction_recycler_view_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}





