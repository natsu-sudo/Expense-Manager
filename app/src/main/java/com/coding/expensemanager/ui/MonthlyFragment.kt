package com.coding.expensemanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding.expensemanager.apdapter.TransactionAdapter
import com.coding.expensemanager.databinding.FragmentMonthlyBinding
import com.coding.expensemanager.util.UtilClass
import com.coding.expensemanager.viewmodel.AllTransactionListViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Where User Can See the Transaction Month Wise
 */
class MonthlyFragment : Fragment() {
    private val TAG = "MonthlyFragment"
    private var item: Int = 0
    private var _binding: FragmentMonthlyBinding? = null
    private val binding get() = _binding!!
    private lateinit var allTransactionListViewModel: AllTransactionListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allTransactionListViewModel = activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(activity!!))
                .get(AllTransactionListViewModel::class.java)
        }!!
        allTransactionListViewModel.updateMonth(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMonthlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transactionTopAppBar.title=UtilClass.getMonth(item)
        binding.transRecycleList.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        }
        allTransactionListViewModel.getMonthlyList.observe(viewLifecycleOwner, {
            binding.transRecycleList.adapter = TransactionAdapter()
            (binding.transRecycleList.adapter as TransactionAdapter).submitList(it)
        })
        allTransactionListViewModel.getMonthlyNetBalance.observe(viewLifecycleOwner, {
            Log.d(TAG, "onViewCreated: $it")
            binding.netBalance.text = it.netBalance.toFloat().toString()
            binding.amountSaved.text = it.income.toFloat().toString()
            binding.amountSpent.text = it.expense.toFloat().toString()
            with(binding.progressIndicator) {
                if (it.income > 0) {
                    progress = ((it.netBalance * 100) / it.income)
                }
            }
        })
        binding.transactionTopAppBar.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(item: Int) =
            MonthlyFragment().apply {
                this.item = item
            }
    }
}