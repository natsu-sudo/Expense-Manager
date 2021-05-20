package com.coding.expensemanager.ui

import android.os.Bundle
import android.util.Log
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
import com.coding.expensemanager.databinding.FragmentIncomeBinding
import com.coding.expensemanager.viewmodel.GenericViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Where User Can See the Income So far
 */
private const val TAG = "IncomeFragment"

class IncomeFragment : Fragment() {
    private var _binding: FragmentIncomeBinding? = null
    private lateinit var incomeViewModel: GenericViewModel
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        incomeViewModel = activity?.run {
            ViewModelProvider(viewModelStore,
                ViewModelFactory(activity!!)).get(GenericViewModel::class.java)
        }!!
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        binding.incomeRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        }
        incomeViewModel.getIncomeList.observe(viewLifecycleOwner, Observer {
            binding.incomeRecyclerView.adapter=TransactionAdapter()
            (binding.incomeRecyclerView.adapter as TransactionAdapter).submitList(it)
        })
        incomeViewModel.totalIncome.observe(viewLifecycleOwner, Observer {
            binding.netIncome.text=it?.toFloat()?.toString() ?: "0.0"
        })



    }

    companion object {
        @JvmStatic
        fun newInstance() = IncomeFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}