package com.coding.expensemanager.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.coding.expensemanager.R
import com.coding.expensemanager.pojo.TransactionsTable
import com.coding.expensemanager.databinding.FragmentAddTransactionsBinding
import com.coding.expensemanager.util.Constants
import com.coding.expensemanager.util.UserSharedPreference
import com.coding.expensemanager.util.UtilClass
import com.coding.expensemanager.viewmodel.AllTransactionViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


/**
 * A Fragment Where User Can Saved Their transactions
 *
 */
class AddTransactions : Fragment() {

    private val TAG = "AddTransactions"
    var recurringStartTime = 0L
    var recurringEndTime = 0L
    var savedTime = 0L
    private lateinit var allTransactionViewModel: AllTransactionViewModel
    private var _binding: FragmentAddTransactionsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        allTransactionViewModel = activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(activity!!))
                .get(AllTransactionViewModel::class.java)
        }!!
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBarAction()
        setAutoCompleteField(view)
        setCalenders()
        setButtons()
    }

    /**
     * @throws NumberFormatException if user enter invalid date
     */
    private fun setButtons() {
        val selectDuration = arrayOf(
            Constants.ONE_DAY,
            Constants.ONE_WEEK,
            Constants.ONE_MONTH,
            Constants.TWO_MONTH,
            Constants.SIX_MONTH,
            Constants.ONE_DAY
        )
        val selectDurationName = arrayOf(
            getString(R.string.one_day),
            getString(R.string.one_week),
            getString(R.string.one_month),
            getString(R.string.two_month),
            getString(R.string.six_month),
            getString(R.string.one_year)
        )

        binding.addExpense.setOnClickListener {
            var selected = -1
            if (binding.recurringTransactionCheckBox.isChecked) {
                MaterialAlertDialogBuilder(it.context, R.style.natsu_custom)
                    .setTitle(getString(R.string.duration))
                    .setIcon(R.drawable.ic_calendar_24)
                    // Single-choice items (initialized with checked item)
                    .setSingleChoiceItems(selectDurationName, selected) { dialog, which ->
                        selected = which
                        Log.d(TAG, "selectMonth: $selected")
                    }
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                        if (selected != -1) {
                            if (fieldCheck() && recurringCheck()) {
                                while (recurringStartTime <= recurringEndTime) {
                                    val selectedDate =
                                        binding.transactionCalender.text.toString().split(
                                            "/"
                                        )
                                    val transactions: TransactionsTable = try {
                                        TransactionsTable(
                                            0,
                                            binding.transactionName.text.toString(),
                                            binding.transactionAmount.text.toString().toInt(),
                                            System.currentTimeMillis(), recurringStartTime,
                                            selectedDate[0].toInt(),
                                            selectedDate[1].toInt(),
                                            selectedDate[2].toInt(),
                                            Constants.EXPENSE,
                                            binding.transactionCategoryText.text.toString(),
                                            binding.transactionTypeText.text.toString()
                                        )
                                    } catch (e: java.lang.NumberFormatException) {
                                        Snackbar.make(it,R.string.date_exception, Snackbar.LENGTH_SHORT)
                                            .show()
                                        return@setPositiveButton
                                    }
                                    allTransactionViewModel.saveTransaction(transactions)
                                    recurringStartTime += selectDuration[selected]
                                }
                                UserSharedPreference.initializeSharedPreferencesSavedTransaction(activity!!).edit().putBoolean(Constants.SAVED,true).apply()
                                activity!!.onBackPressed()
                            } else {
                                if (recurringEndTime - recurringStartTime <= Constants.ONE_DAY) {
                                    Snackbar.make(
                                        it,
                                        R.string.please_enter_valid,
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    Snackbar.make(it, R.string.mandatory, Snackbar.LENGTH_SHORT)
                                        .show()
                                }

                            }
                        }
                    }
                    .show()
            } else {
                if (fieldCheck()) {
                    val selectedDate = binding.transactionCalender.text.toString().split("/")
                    val transactions: TransactionsTable = try {
                        TransactionsTable(
                            0,
                            binding.transactionName.text.toString(),
                            binding.transactionAmount.text.toString().toInt(),
                            System.currentTimeMillis(), savedTime,
                            selectedDate[0].toInt(),
                            selectedDate[1].toInt(),
                            selectedDate[2].toInt(),
                            Constants.EXPENSE,
                            binding.transactionCategoryText.text.toString(),
                            binding.transactionTypeText.text.toString()
                        )
                    } catch (e: java.lang.NumberFormatException) {
                        Snackbar.make(it,R.string.date_exception, Snackbar.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    allTransactionViewModel.saveTransaction(transactions)
                    UserSharedPreference.initializeSharedPreferencesSavedTransaction(activity!!).edit().putBoolean(Constants.SAVED,true).apply()
                    activity!!.onBackPressed()
                } else {
                    Snackbar.make(it, R.string.mandatory, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }


        }
        binding.addIncome.setOnClickListener {
            if (fieldCheck()) {
                val selectedDate = binding.transactionCalender.text.toString().split("/")
                val transactions: TransactionsTable = try {
                    TransactionsTable(
                        0,
                        binding.transactionName.text.toString(),
                        binding.transactionAmount.text.toString().toInt(),
                        System.currentTimeMillis(), savedTime,
                        selectedDate[0].toInt(),
                        selectedDate[1].toInt(),
                        selectedDate[2].toInt(),
                        Constants.INCOME,
                        binding.transactionCategoryText.text.toString(),
                        binding.transactionTypeText.text.toString()
                    )
                } catch (e: java.lang.NumberFormatException) {
                    Snackbar.make(it,R.string.date_exception, Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                allTransactionViewModel.saveTransaction(transactions)
                UserSharedPreference.initializeSharedPreferencesSavedTransaction(activity!!).edit().putBoolean(Constants.SAVED,true).apply()
                activity!!.onBackPressed()
            } else {
                Snackbar.make(it, R.string.mandatory, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Checking the Recurring Date There Should be Greater than One Day
     */
    private fun recurringCheck(): Boolean {
        return binding.transactionEndDate.text.toString().isNotEmpty() &&
                binding.transactionEndDate.text.toString().isNotEmpty() &&
                (recurringEndTime - recurringStartTime >= Constants.ONE_DAY)


    }

    /**
     * Checking for Field as All Field are Mandatory to add Transactions
     */
    private fun fieldCheck(): Boolean {
        return binding.transactionName.text.toString().isNotEmpty() &&
                binding.transactionAmount.text.toString().isNotEmpty() &&
                binding.transactionCalender.text.toString().isNotEmpty() &&
                binding.transactionCategoryText.text.toString().isNotEmpty() &&
                binding.transactionTypeText.text.toString().isNotEmpty()
    }

    /**
     * Setting Calender on Select Start Date ,End Date and Select Date
     */
    private fun setCalenders() {
        binding.transactionCalenderLayout.setEndIconOnClickListener {
            // Opens the date picker with today's date selected.
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(limitRange().build())
                .build()
            datePicker.addOnPositiveButtonClickListener {
                binding.transactionCalender.setText(UtilClass.convertToDate(it))
                savedTime = it
            }
            datePicker.show(activity!!.supportFragmentManager, getString(R.string.calender))
        }
        binding.transactionStartDate.setOnClickListener {

            // Opens the date picker with today's date selected.
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(limitRange().build())
                .build()
            datePicker.addOnPositiveButtonClickListener {
                recurringStartTime = it
                binding.transactionStartDate.text = UtilClass.convertToDate(it)
            }
            datePicker.show(activity!!.supportFragmentManager, getString(R.string.calender))
        }
        binding.transactionEndDate.setOnClickListener {

            // Opens the date picker with today's date selected.
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(limitRange().build())
                .build()
            datePicker.addOnPositiveButtonClickListener {
                recurringEndTime = it
                binding.transactionEndDate.text = UtilClass.convertToDate(it)
            }
            datePicker.show(activity!!.supportFragmentManager, getString(R.string.calender))
        }
    }

    /**
     * Creating Constraints from
     */
    private fun limitRange(): CalendarConstraints.Builder {
        return CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
    }

    private fun setAutoCompleteField(view: View) {
        val items = listOf("Food", "Fuel", "Rent", "Others")
        val adapter = ArrayAdapter(view.context, R.layout.list_item, items)
        (binding.transactionCategoryLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val category = listOf(Constants.CARD, Constants.UPI, Constants.OTHERS)
        val categoryAdapter = ArrayAdapter(view.context, R.layout.list_item, category)
        (binding.transactionTypeLayout.editText as? AutoCompleteTextView)?.setAdapter(
            categoryAdapter
        )
        binding.recurringTransactionCheckBox.setOnClickListener {
            if (binding.recurringTransactionCheckBox.isChecked) {
                binding.recurringLayout.visibility = View.VISIBLE
                binding.addIncome.isClickable = false
                binding.addIncome.setTextColor(ContextCompat.getColor(view.context, R.color.gray))
            } else {
                binding.recurringLayout.visibility = View.GONE
                binding.addIncome.isClickable = true
                binding.addIncome.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.income_default_color
                    )
                )

            }
        }
    }

    private fun setTopBarAction() {
        binding.transactionTopAppBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddTransactions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}