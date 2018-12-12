package com.rumeysaturker.supermomkotlinapp.Profile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rumeysaturker.supermomkotlinapp.Login.LoginActivity
import com.rumeysaturker.supermomkotlinapp.Models.Users
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NUMBER = 4
    private val TAG = "ProfileActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        mUser = mAuth.currentUser!!
        setupToolBar()
        setupNavigationView()
        kullaniciBilgileriniGetir()


    }

    private fun kullaniciBilgileriniGetir() {

        mRef.child("users").child(mUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.getValue() != null) {
                    var okunanKullaniciBilgileri = p0!!.getValue(Users::class.java)
                   EventBus.getDefault().postSticky(EventBusDataEvents.KulaniciBilgileriniGonder(okunanKullaniciBilgileri))
                    tvProfilAdiToolbar.setText(okunanKullaniciBilgileri!!.user_name!!)
                    tvProfilGercekAd.setText(okunanKullaniciBilgileri!!.adi_soyadi!!)
                    tvFollowerSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.follower!!)
                    tvFollowingSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.following!!)
                    tvGonderiSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.post!!)
                     var imgURL:String?=okunanKullaniciBilgileri!!.user_detail!!.profile_picture!!
                    UniversalImageLoader.setImage(imgURL!!, circleProfileImage, progressBar, "")
                    if(!okunanKullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()){
                        tvBiyografi.visibility=View.VISIBLE
                        tvBiyografi.setText(okunanKullaniciBilgileri!!.user_detail!!.biography!!)
                    }
                    if(!okunanKullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()){
                       tvWebSitesi.visibility=View.VISIBLE
                        tvWebSitesi.setText(okunanKullaniciBilgileri!!.user_detail!!.web_site!!)

                    }


                }
            }
        })
    }




    //aktivite başlatma, imgProfileSettings ikonuna tıklanıldığında ProfileSettingsActivity'i başlat
    private fun setupToolBar() {
        imgProfileSettings.setOnClickListener {
            var intent = Intent(this, ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)//fonksiyona companion object kullandığımız için direkt erişebiliyoruz.
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NUMBER)
        menuItem.setChecked(true)
    }

    override fun onBackPressed() {
        profileRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    //kullanıcının oturum açıp açmadığıyla ilgili verileri tutan listener
    private fun setupAuthListener() {

        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                if (user == null) {
                    Log.e("HATA", "Kullanıcı oturum açmamış,ProfileActivtydesin")
                    //kimse yoksa ProfileActivity'de LoginActivity'e yönlendir
                    var intent = Intent(this@ProfileActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()//home activitye gittikten sonra geri butonuna bastığında uygulamadan çıkıcak(ilk girişte)(back-stack durumunu ortadan kaldırır.)
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
