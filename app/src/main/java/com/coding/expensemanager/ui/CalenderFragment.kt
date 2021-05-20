package com.coding.expensemanager.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.coding.expensemanager.apdapter.CalendarAdapter
import com.coding.expensemanager.databinding.FragmentCalenderBinding
import com.coding.expensemanager.viewmodel.CalenderViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * A [fagment] which will show user Their transaction and User can See their income and Expense date wise
 *
 */
class CalenderFragment : Fragment() {


    private lateinit var selectedDate:LocalDate
    private lateinit var calenderViewModel: CalenderViewModel
    private  val TAG = "CalenderFragment"
    private var _bindind:FragmentCalenderBinding?=null
    private val binding get() = _bindind!!
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calenderViewModel = activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(activity!!))
                .get(CalenderViewModel::class.java)
        }!!
        selectedDate = LocalDate.now()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindind=FragmentCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calenderViewModel.updateMonth(selectedDate.monthValue)
        binding.topAppBarCalc.setNavigationOnClickListener {
            // Handle navigation icon press
            activity?.onBackPressed()
        }
        binding.calendarRecyclerView.apply {
            layoutManager=GridLayoutManager(activity, 7)
        }
        calenderViewModel.getMonthlyMap.observe(viewLifecycleOwner, Observer {
            binding.monthYearTv.text = monthYearFromDate(selectedDate)
            val daysInMonth = daysInMonthArray(selectedDate)
            Log.d(TAG, "onViewCreated: $it")
            binding.calendarRecyclerView.adapter = CalendarAdapter(daysInMonth, it)
        })
        binding.forwardMonth.setOnClickListener {
            nextMonthAction()
        }
        binding.backMonth.setOnClickListener {
            previousMonthAction()
        }

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CalenderFragment()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): MutableList<String> {
        val daysInMonthArray = mutableListOf<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                val day=(i - dayOfWeek).toString()
                daysInMonthArray.add(day)
            }
        }
        return daysInMonthArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        calenderViewModel.updateMonth(selectedDate.monthValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        calenderViewModel.updateMonth(selectedDate.monthValue)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindind=null
    }


}