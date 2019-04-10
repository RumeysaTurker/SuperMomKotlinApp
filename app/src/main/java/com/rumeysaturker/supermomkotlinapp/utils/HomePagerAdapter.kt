package com.rumeysaturker.supermomkotlinapp.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

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

    //kamera fragmenti için sadece ilgili fragmenti açık tutmak için
    fun secilenFragmentiViewPagerdanSil(viewGroup: ViewGroup, position: Int){
        var silinecekFragment=this.instantiateItem(viewGroup,position)
        this.destroyItem(viewGroup,position,silinecekFragment)//view groupta şu positionda olan silinecekFragmenti sil
    }

    fun secilenFragmentiViewPageraEkle(viewGroup: ViewGroup, position: Int){
        this.instantiateItem(viewGroup,position)
    }

}