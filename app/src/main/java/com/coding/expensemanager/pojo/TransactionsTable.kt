package com.coding.expensemanager.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coding.expensemanager.util.Constants


@Entity(tableName = "transaction_table")
data class TransactionsTable(@PrimaryKey(autoGenerate = true)val id:Long,
                             @ColumnInfo(name = "transaction_name")val transactionName:String,
                             @ColumnInfo(name = "amount")val amount:Int,
                             @ColumnInfo(name = "timestamp")val timeStamp:Long,
                             @ColumnInfo(name="save_date")val savedDate:Long,
                             @ColumnInfo(name = "day")val day:Int,
                             @ColumnInfo(name="month")val month:Int,
                             @ColumnInfo(name = "year")val year: Int,
                             @ColumnInfo(name = "transaction_type")val transactionType:Int,
                             @ColumnInfo(name = "category")val category: String,
                             @ColumnInfo(name="transition_method")val transactionMethod:String=Constants.OTHERS)