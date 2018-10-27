package com.rumeysaturker.supermomkotlinapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rumeysaturker.supermomkotlinapp.Home.GorevFragment
import com.rumeysaturker.supermomkotlinapp.Home.HomeFragment
import com.rumeysaturker.supermomkotlinapp.Home.MessagesFragment
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import com.rumeysaturker.supermomkotlinapp.utils.HomePagerAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val ACTIVITY_NUMBER = 0
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupNavigationView()
        setupHomeViewPager()
    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)//fonksiyona companion object kullandığımız için direkt erişebiliyoruz.
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NUMBER)
        menuItem.setChecked(true)
    }

    private fun setupHomeViewPager() {
        var homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.addFragment(GorevFragment())//id=0
        homePagerAdapter.addFragment(HomeFragment())//id=1
        homePagerAdapter.addFragment(MessagesFragment())//id=2

        //activity main de bulunan viewpagera oluşturulan adaptörü atama
        homeViewPager.adapter = homePagerAdapter
        //viewpager, homefragment'iyle başlasın
        homeViewPager.setCurrentItem(1)

    }


}
