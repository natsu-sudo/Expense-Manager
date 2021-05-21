package com.coding.expensemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.coding.expensemanager.R
import com.coding.expensemanager.apdapter.MonthlyCardAdapter
import com.coding.expensemanager.pojo.TransactionsTable

class YearAdapterCard(val map: Map<Int, List<TransactionsTable>>) : ListAdapter<Int, YearAdapterCard.YearViewHolder>(YearDiffCallBack())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view:View= LayoutInflater.from(parent.context).inflate(R.layout.year_view,parent,false)
        return YearViewHolder(view)
    }

    inner class YearViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val year: TextView =view.findViewById<TextView>(R.id.year)
        private val yearRecyclerView: RecyclerView =view.findViewById<RecyclerView>(R.id.monthly_recycler_view)

        fun onBind(item: Int) {
            year.text =item.toString()
            val monthlyMap=sendMonthlyMap(item)
            yearRecyclerView.apply {
                layoutManager= LinearLayoutManager(view.context)
                adapter= MonthlyCardAdapter(monthlyMap)
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(view.context, RecyclerView.VERTICAL))
            }
            (yearRecyclerView.adapter as MonthlyCardAdapter).submitList(monthlyMap.keys.toList().sorted())
        }

        private fun sendMonthlyMap(item: Int): Map<Int, List<TransactionsTable>> {
                return map[item]!!.groupBy {
                    it.month
                }
        }

    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}

class YearDiffCallBack: DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem==newItem
    }

}
