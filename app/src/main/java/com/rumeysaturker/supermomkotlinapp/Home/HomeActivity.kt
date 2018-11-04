package com.rumeysaturker.supermomkotlinapp.Home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.nostra13.universalimageloader.core.ImageLoader
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import com.rumeysaturker.supermomkotlinapp.utils.HomePagerAdapter
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_home.*

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    private val ACTIVITY_NUMBER = 0
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        initImageLoader()
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

    private fun initImageLoader() {
        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)//configuration dosyasını yolluyoruz.

    }


}
