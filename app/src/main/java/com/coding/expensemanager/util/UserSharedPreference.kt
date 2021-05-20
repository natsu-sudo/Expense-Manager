package com.coding.expensemanager.util

import android.content.Context
import android.content.SharedPreferences

object UserSharedPreference  {
    fun initializeSharedPreferencesForUser(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.USER_NAME, Context.MODE_PRIVATE)
    }

    fun initializeSharedPreferencesFirstTime(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.FIRST_TIME, Context.MODE_PRIVATE)
    }

    fun initializeSharedPreferencesSavedTransaction(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SAVED, Context.MODE_PRIVATE)
    }


}
