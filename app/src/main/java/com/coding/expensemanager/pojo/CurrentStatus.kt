package com.coding.expensemanager.pojo

data class CurrentStatus(
    val netBalance: Int,
    val others: Int,
    val card: Int,
    val upi: Int,
    val income: Int,
    val expense: Int
)
