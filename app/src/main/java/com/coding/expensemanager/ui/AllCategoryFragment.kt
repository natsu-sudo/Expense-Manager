package com.coding.expensemanager.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding.expensemanager.R
import com.coding.expensemanager.apdapter.MonthlyCardAdapter
import com.coding.expensemanager.apdapter.TransactionAdapter
import com.coding.expensemanager.databinding.FragmentAllCataegoryBinding
import com.coding.expensemanager.util.Constants
import com.coding.expensemanager.viewmodel.AllTransactionListViewModel
import com.coding.expensemanager.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.eazegraph.lib.models.PieModel


/**
 * A simple [Fragment] subclass.
 * Which will show Summary of your Transactions
 * create an instance of this fragment.
 */
private const val TAG = "AllCategoryFragment"

class AllCategoryFragment : Fragment() {
    private var _binding: FragmentAllCataegoryBinding? = null
    private val binding get() = _binding!!
    private var listener:ChangeFragment?=null
    private lateinit var allTransactionListViewModel: AllTransactionListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        allTransactionListViewModel = activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(activity!!))
                .get(AllTransactionListViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllCataegoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPieChart()
        setTransactionRecyclerView()
        setMonthlyTransaction()
        binding.viewAllTransaction.setOnClickListener {
            listener?.onClick(TransactionFragment.newInstance())
        }
        binding.remindMe.setOnClickListener {
            MaterialAlertDialogBuilder(it.context, R.style.natsu_custom)
                .setTitle(getString(R.string.under_development))
                .setMessage(R.string.not_learned)
                .setPositiveButton(R.string.ok) {dialog, which ->

                }
                .show()
        }

    }

    private fun setMonthlyTransaction() {
        binding.monthlyRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
        allTransactionListViewModel.getShortMonthlyList.observe(viewLifecycleOwner, Observer {
            binding.monthlyRecyclerView.adapter = MonthlyCardAdapter(it)
            (binding.monthlyRecyclerView.adapter as MonthlyCardAdapter).submitList((it.keys).toList())
        })

    }

    private fun setTransactionRecyclerView() {
        binding.transactionRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        }
        allTransactionListViewModel.getLimitedTransactions.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.upcomingTran.visibility = View.GONE
            } else {
                binding.upcomingTran.visibility = View.VISIBLE
                binding.transactionRecyclerView.adapter = TransactionAdapter()
                (binding.transactionRecyclerView.adapter as TransactionAdapter).submitList(it)
            }
        })
    }


    @SuppressLint("ResourceType")
    private fun setPieChart() {
        // Set the data and color to the pie chart
        allTransactionListViewModel.getNetBalance.observe(viewLifecycleOwner, Observer {
            if (it.netBalance != 0) {
                Log.d(
                    TAG,
                    "setPieChart: " + (it.card * 100 / it.netBalance).toFloat() + " " + it.card + " " + it.netBalance
                )
                Log.d(
                    TAG,
                    "setPieChart: " + (it.upi * 100 / it.netBalance).toFloat() + " " + it.upi + " " + it.netBalance
                )
                Log.d(
                    TAG,
                    "setPieChart: " + (it.others * 100 / it.netBalance).toFloat() + " " + it.others + " " + it.netBalance
                )

                binding.piechart.addPieSlice(
                    PieModel(
                        Constants.CARD,
                        (it.card * 100 / it.netBalance).toFloat() * 100,
                        Color.parseColor("#AEEA00")//light green
                    )
                )
                binding.piechart.addPieSlice(
                    PieModel(
                        Constants.UPI,
                        (it.upi * 100 / it.netBalance).toFloat(),
                        Color.parseColor("#5E039BE5")//light blue
                    )
                )
                binding.piechart.addPieSlice(
                    PieModel(
                        Constants.OTHERS,
                        (it.others * 100 / it.netBalance).toFloat(),
                        Color.parseColor("#28D50000")//transparent red
                    )
                )
                binding.netBalance.text = it.netBalance.toFloat().toString()
                binding.cardNetAmount.text = getString(R.string.card_, it.card.toFloat().toString())
                binding.upiNetAmount.text = getString(R.string.upi_, it.upi.toFloat().toString())
                binding.othersNetAmount.text = getString(
                    R.string.others_,
                    it.others.toFloat().toString()
                )
                binding.piechart.startAnimation()

            } else {
                binding.netBalance.text = it.netBalance.toString()
                binding.cardNetAmount.text = getString(R.string.card_, it.card.toFloat().toString())
                binding.upiNetAmount.text = getString(R.string.upi_, it.card.toFloat().toString())
                binding.othersNetAmount.text = getString(
                    R.string.others_,
                    it.others.toFloat().toString()
                )
            }
        })


    }

    companion object {
        @JvmStatic
        fun newInstance() = AllCategoryFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ChangeFragment

        if (listener==null){
            throw Exception("No Listener was found in Activity")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener=null

    }
    interface ChangeFragment{
        fun onClick(fragment: Fragment)
    }
}