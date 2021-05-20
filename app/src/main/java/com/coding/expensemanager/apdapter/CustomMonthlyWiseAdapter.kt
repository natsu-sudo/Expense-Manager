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

class CustomMonthlyWiseAdapter : ListAdapter<TransactionsTable,CustomMonthlyWiseAdapter.ViewHolder>(DiffCallBackShort()){
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {

        private val transactionsName: TextView =view.findViewById(R.id.trans_name)
        private val amount: TextView =view.findViewById(R.id.trans_amount)
        fun onBind(item: TransactionsTable) {
            transactionsName.text=item.transactionName
            if (item.transactionType == Constants.INCOME){
                amount.setTextColor(ContextCompat.getColor(view.context, R.color.income_default_color))
                amount.text= "+ ${item.amount}"
            }else{
                amount.setTextColor(ContextCompat.getColor(view.context, R.color.expense_default_color))
                amount.text= "- ${item.amount}"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.short_month,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }


}

class DiffCallBackShort():DiffUtil.ItemCallback<TransactionsTable>() {
    override fun areItemsTheSame(oldItem: TransactionsTable, newItem: TransactionsTable): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: TransactionsTable, newItem: TransactionsTable): Boolean {
        return oldItem==newItem
    }
}

