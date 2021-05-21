package com.coding.expensemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.coding.expensemanager.apdapter.MonthlyCardAdapter
import com.coding.expensemanager.databinding.ActivityMainBinding
import com.coding.expensemanager.ui.AllCategoryFragment
import com.coding.expensemanager.ui.ExpenseManagerFragment
import com.coding.expensemanager.ui.FirstFragment
import com.coding.expensemanager.ui.MonthlyFragment
import com.coding.expensemanager.util.Constants
import com.coding.expensemanager.util.UserSharedPreference

class MainActivity : AppCompatActivity(),AllCategoryFragment.ChangeFragment,MonthlyCardAdapter.ChangeFragment {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userRegistered =
            UserSharedPreference.initializeSharedPreferencesForUser(this@MainActivity)
                .getString(Constants.USER_NAME, null)
        if (userRegistered != null) {
            with(supportFragmentManager.beginTransaction()) {
                add(R.id.main_container, ExpenseManagerFragment.newInstance())
                commit()
            }
        } else {
            with(supportFragmentManager.beginTransaction()) {
                add(R.id.main_container, FirstFragment.newInstance())
                commit()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.main_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun clickListener(item: Int, year: Int) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.main_container, MonthlyFragment.newInstance(item,year))
            addToBackStack(null)
            commit()
        }
    }


}