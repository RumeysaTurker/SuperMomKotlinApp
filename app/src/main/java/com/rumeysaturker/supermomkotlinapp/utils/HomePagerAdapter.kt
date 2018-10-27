package com.rumeysaturker.supermomkotlinapp.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class HomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var myFragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
    return  myFragmentList.get(position)
    }

    override fun getCount(): Int {
        return myFragmentList.size
    }

    fun addFragment(fragment:Fragment){
        myFragmentList.add(fragment)
    }
}