package com.coding.expensemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding.expensemanager.apdapter.TransactionAdapter
import com.coding.expensemanager.databinding.FragmentTransactionBinding
import com.coding.expensemanager.viewmodel.AllTransactionListViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory


class TransactionFragment : Fragment() {

    private lateinit var allTransactionListViewModel: AllTransactionListViewModel
    private var _binding: FragmentTransactionBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allTransactionListViewModel= activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(activity!!))
                .get(AllTransactionListViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentTransactionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transRecycleList.apply {
            layoutManager= LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        }
        allTransactionListViewModel.getFullList.observe(viewLifecycleOwner, {
               binding.transRecycleList.adapter= TransactionAdapter()
            (binding.transRecycleList.adapter as TransactionAdapter).submitList(it)
        })
        allTransactionListViewModel.getNetBalance.observe(viewLifecycleOwner, {
            binding.netBalance.text=it.netBalance.toFloat().toString()
            binding.amountSaved.text=it.income.toFloat().toString()
            binding.amountSpent.text=it.expense.toFloat().toString()
            with(binding.progressIndicator){
                progress= ((it.netBalance*100)/it.income)
            }
        })
        binding.transactionTopAppBar.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TransactionFragment()
    }
}

