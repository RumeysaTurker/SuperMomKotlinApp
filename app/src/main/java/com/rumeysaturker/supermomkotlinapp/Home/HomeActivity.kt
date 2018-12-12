package com.rumeysaturker.supermomkotlinapp.Home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nostra13.universalimageloader.core.ImageLoader
import com.rumeysaturker.supermomkotlinapp.Login.LoginActivity
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import com.rumeysaturker.supermomkotlinapp.utils.HomePagerAdapter
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_home.*

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    private val ACTIVITY_NUMBER = 0
    private val TAG = "HomeActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initImageLoader()
        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()

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

    //kullanıcının oturum açıp açmadığıyla ilgili verileri tutan listener
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {
                    var intent = Intent(this@HomeActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)//çıkış yapıldığında geri butonuna basınca homeactivityden başlatmaya çalışmasın
                    finish()//home activitye gittikten sonra geri butonuna bastığında uygulamadan çıkıcak(ilk girişte)
                } else {

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener)
    }
}
