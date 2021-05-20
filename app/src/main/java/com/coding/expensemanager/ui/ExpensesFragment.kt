package com.coding.expensemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding.expensemanager.apdapter.TransactionAdapter
import com.coding.expensemanager.databinding.FragmentExpensesBinding
import com.coding.expensemanager.viewmodel.GenericViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Where User Can See the Expenses So far
 */
private const val TAG = "ExpensesFragment"
class ExpensesFragment : Fragment() {
   private var _binding:FragmentExpensesBinding?=null
    private lateinit var expenseViewModel: GenericViewModel
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expenseViewModel= activity?.run {
            ViewModelProvider(viewModelStore,
                ViewModelFactory(activity!!)
            ).get(GenericViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentExpensesBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        binding.expenseRecyclerView.apply {
            layoutManager= LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        }
        expenseViewModel.getExpenseList.observe(viewLifecycleOwner, Observer {
            binding.expenseRecyclerView.adapter= TransactionAdapter()
            (binding.expenseRecyclerView.adapter as TransactionAdapter).submitList(it)
        })
        expenseViewModel.totalExpense.observe(viewLifecycleOwner, Observer {

            binding.netExpense.text= it?.toFloat()?.toString() ?: "0.0"
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExpensesFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}