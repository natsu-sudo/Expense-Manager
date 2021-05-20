package com.coding.expensemanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coding.expensemanager.R
import com.coding.expensemanager.apdapter.ViewPagerAdapter
import com.coding.expensemanager.databinding.FragmentExpenseBinding
import com.coding.expensemanager.util.Constants
import com.coding.expensemanager.util.Constants.Companion.USER_NAME
import com.coding.expensemanager.util.UserSharedPreference
import com.google.android.material.snackbar.Snackbar

private const val TAG = "ExpenseManagerFragment"

/**
 * Manager Fragment Which are holding the Other Fragments
 *
 */

class ExpenseManagerFragment : Fragment(){

    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        binding.topAppBar.title = UserSharedPreference.initializeSharedPreferencesForUser(context!!)
            .getString(USER_NAME, "natsu")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        if (UserSharedPreference.initializeSharedPreferencesSavedTransaction(activity!!).getBoolean(Constants.SAVED,false)){
            UserSharedPreference.initializeSharedPreferencesSavedTransaction(activity!!).edit().putBoolean(Constants.SAVED,false).apply()
            Snackbar.make(view, R.string.saved, Snackbar.LENGTH_SHORT)
                .show()
        }
        viewPagerAndFragment()
        binding.topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            activity?.onBackPressed()

        }
        binding.topAppBar.setOnMenuItemClickListener {
            goToCalender()
            true
        }
        binding.addTransactions.setOnClickListener {
            val fr = fragmentManager?.beginTransaction()
            Log.d(TAG, "selectMonth: $fr")
            fr?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            fr?.replace(R.id.main_container, AddTransactions.newInstance())
            fr?.addToBackStack(null)
            fr?.commit()
        }

    }

    private fun goToCalender() {
        val fr = fragmentManager?.beginTransaction()
        Log.d(TAG, "selectMonth: $fr")
        fr?.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_bottom, R.anim.enter_from_pop_up, R.anim.pop_exit_from_bottom);
        fr?.replace(R.id.main_container, CalenderFragment.newInstance())
        fr?.addToBackStack(null)
        fr?.commit()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ExpenseManagerFragment()
    }

    /**
     * setting view pager
     */
    private fun viewPagerAndFragment() {
        binding.expenseTabLayout.setupWithViewPager(binding.tabLayoutViewPager)
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, 0)
        viewPagerAdapter.addFragment(AllCategoryFragment.newInstance(), getString(R.string.all))
        viewPagerAdapter.addFragment(IncomeFragment.newInstance(), getString(R.string.income))
        viewPagerAdapter.addFragment(ExpensesFragment.newInstance(), getString(R.string.expense))
        binding.tabLayoutViewPager.adapter = viewPagerAdapter
        binding.expenseTabLayout.getTabAt(0)?.setIcon(R.drawable.ic__all)
        binding.expenseTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_income)
        binding.expenseTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_expense)
    }

//    private fun showMenu(v: View, @MenuRes menuRes: Int) {
//        val popup = PopupMenu(context!!, v)
//        popup.menuInflater.inflate(menuRes, popup.menu)
//
//        popup.setOnMenuItemClickListener {
//            makeText(activity,"jyf", LENGTH_SHORT).show()
//            return@setOnMenuItemClickListener true
//        }
//
//        // Show the popup menu.
//        popup.show()
//    }


}






