package com.rumeysaturker.supermomkotlinapp.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*


class ProfileSettingsActivity : AppCompatActivity() {

    private val ACTIVITY_NUMBER = 4
    private val TAG = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        setupToolBar()
        fragmentNavigation()

    }
    /*fragment geçişleri*/
    private fun fragmentNavigation() {
        tvProfilDuzenleHesapAyarlari.setOnClickListener {
            profileSettingsRoot.visibility= View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer, ProfileEditFragment())//id'si profileSettingsContainer olan fragmente ProfileEditFragment()ten nesne oluşturup yerleştir
           transaction.addToBackStack("editProfileFragmentEklendi")//geri butonuna basıldığında bir önceki activity yüklenir fakat burada bir önceki fragmenti açmak istiyorum
            transaction.commit()//bu transaction'ı onayla
        }
        tvCikisYap.setOnClickListener {

            var dialog= SignOutFragment()
            dialog.show(supportFragmentManager,"cikisYapDialogGoster")
            Log.e("HATA","Çıkış yap butonuna basıldı,ProfileSettingsActivtydesin")

        }
    }

    private fun setupToolBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
    }
//her geri butonuna tıklanıldığında profileSettingsRoot'u visible yap
    override fun onBackPressed() {
        profileSettingsRoot.visibility= View.VISIBLE
        super.onBackPressed()
    }
    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)//fonksiyona companion object kullandığımız için direkt erişebiliyoruz.
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NUMBER)
        menuItem.setChecked(true)
    }
}
