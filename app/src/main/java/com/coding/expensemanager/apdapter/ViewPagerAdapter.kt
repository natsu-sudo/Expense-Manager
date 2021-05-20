package com.coding.expensemanager.apdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(childFragmentManager: FragmentManager, behavior: Int): FragmentPagerAdapter(childFragmentManager, behavior) {
    private val fragmentList:MutableList<Fragment> = mutableListOf()
    private val fragmentTitle:MutableList<String> = mutableListOf()
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    fun addFragment(fragment: Fragment, title:String){
        fragmentList.add(fragment)
        fragmentTitle.add(title)
    }

}
