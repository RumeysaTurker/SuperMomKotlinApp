package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.rumeysaturker.supermomkotlinapp.home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.profile.ProfileActivity
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.game.GameActivity
import com.rumeysaturker.supermomkotlinapp.gorev.GorevActivity
import com.rumeysaturker.supermomkotlinapp.search.SearchActivity
import com.rumeysaturker.supermomkotlinapp.share.ShareActivity

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
                        R.id.ic_game -> {
                            val intent=Intent(context, GameActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true

                        }
                        R.id.ic_gorev -> {
                            val intent=Intent(context, GorevActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                            return true

                        }
                        R.id.ic_profile -> {

                            val intent=Intent(context, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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