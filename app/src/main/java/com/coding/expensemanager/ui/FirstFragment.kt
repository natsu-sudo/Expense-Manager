package com.coding.expensemanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coding.expensemanager.R
import com.coding.expensemanager.databinding.FragmentFirstBinding
import com.coding.expensemanager.util.Constants
import com.coding.expensemanager.util.Constants.Companion.USER_NAME
import com.coding.expensemanager.util.UserSharedPreference
import com.google.android.material.snackbar.Snackbar


class FirstFragment : Fragment() {
    private  val TAG = "FirstFragment"
    private var _binding: FragmentFirstBinding?=null
    private val binding get()= _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: ")
        binding.continueButton.setOnClickListener {
            val username=binding.userNameText.text.toString()
           if (username.isNotEmpty()){
               UserSharedPreference.initializeSharedPreferencesForUser(it.context).edit().putString(USER_NAME,username.trim()).apply()
               UserSharedPreference.initializeSharedPreferencesFirstTime(it.context).edit().putLong(Constants.FIRST_TIME,System.currentTimeMillis()).apply()
               val fr = fragmentManager?.beginTransaction()
               fr?.replace(R.id.main_container, ExpenseManagerFragment.newInstance())
               fr?.commit()
           }else{
               Snackbar.make(view,R.string.mandatory,Snackbar.LENGTH_SHORT)
                   .show()
           }
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = FirstFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}