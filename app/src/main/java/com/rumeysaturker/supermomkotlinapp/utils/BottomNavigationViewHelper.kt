package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.rumeysaturker.supermomkotlinapp.Home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.News.NewsActivity
import com.rumeysaturker.supermomkotlinapp.Profile.ProfileActivity
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.Search.SearchActivity
import com.rumeysaturker.supermomkotlinapp.Share.ShareActivity

class BottomNavigationViewHelper {
    companion object {
        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx) {
            bottomNavigationViewEx.enableAnimation(false)
            bottomNavigationViewEx.enableItemShiftingMode(false)
            bottomNavigationViewEx.enableShiftingMode(false)
            bottomNavigationViewEx.setTextVisibility(false)//bottom_navigation_menu.xml içindeki title'lar boş bile olsa yer kaplıyor.Bu yüzden false yaptım.
        }

        fun setupNavigation(context: Context, bottomNavigationViewEx: BottomNavigationViewEx) {
            bottomNavigationViewEx.onNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.ic_home -> {

                       val intent=Intent(context, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)//bottomnavigation geçişlerindeki zıplama efektini kaldırmak için addFlags koydum
                        context.startActivity(intent)
                            return true
                        }
                        R.id.ic_search -> {

                            val intent=Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.ic_news -> {
                            val intent=Intent(context,NewsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.ic_profile -> {

                            val intent=Intent(context, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.ic_share -> {
                            val intent=Intent(context, ShareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true

                        }
                    }
                    return false
                }

            }
        }
    }
}