package com.coding.expensemanager.util

import java.text.SimpleDateFormat
import java.text.DateFormatSymbols;
import java.util.*

object UtilClass {
    fun convertToDateAndTime(standardTimeStamp: Long): String {
        // Creating date format
        return SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US).format(standardTimeStamp)
    }

    fun convertToDate(standardTimeStamp: Long): String {
        // Creating date format
        return SimpleDateFormat("dd/MM/yyyy", Locale.US).format(standardTimeStamp)
    }

    fun convertToTime(standardTimeStamp: Long):String{
        // Creating date format
        return SimpleDateFormat("hh:mm a ", Locale.US).format(standardTimeStamp)
    }

    fun getMonth(month: Int): String? {
        return DateFormatSymbols().months[month - 1]
    }
}