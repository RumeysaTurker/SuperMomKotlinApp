package com.rumeysaturker.supermomkotlinapp.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
class SharePagerAdapter(fragmentManager: FragmentManager, tabAdlari:ArrayList<String>) : FragmentPagerAdapter(fragmentManager) {

    private var mFragmentList:ArrayList<Fragment> = ArrayList()
    private var mTabAdlari:ArrayList<String> = tabAdlari

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment){
        mFragmentList.add(fragment)
    }
//1 Fotoğraf 2 video şeklinde tab adları çağıracak
    override fun getPageTitle(position: Int): CharSequence? {
        return mTabAdlari.get(position)
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