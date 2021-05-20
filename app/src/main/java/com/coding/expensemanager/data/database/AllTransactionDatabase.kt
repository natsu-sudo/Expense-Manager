package com.coding.expensemanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.data.dao.TransactionListTableDao
import com.coding.expensemanager.data.dao.TransactionTableDao

@Database(entities = [TransactionsTable::class],version = 1)
abstract class AllTransactionDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionTableDao
    abstract fun transactionListDao():TransactionListTableDao
    companion object{
        @Volatile
        private var instance: AllTransactionDatabase?=null
        fun getDatabase(context: Context)=
            instance
                ?: synchronized(this){
                    Room.databaseBuilder(context.applicationContext,
                        AllTransactionDatabase::class.java,"all_transaction_database").build().also {
                        instance =it
                    }
                }
    }
}