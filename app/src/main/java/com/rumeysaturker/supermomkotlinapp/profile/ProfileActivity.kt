package com.rumeysaturker.supermomkotlinapp.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hoanganhtuan95ptit.autoplayvideorecyclerview.AutoPlayVideoRecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.rumeysaturker.supermomkotlinapp.login.LoginActivity
import com.rumeysaturker.supermomkotlinapp.models.Users
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.VideoRecyclerView.CenterLayoutManager
import com.rumeysaturker.supermomkotlinapp.models.Posts
import com.rumeysaturker.supermomkotlinapp.models.UserPosts
import com.rumeysaturker.supermomkotlinapp.utils.BottomNavigationViewHelper
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import com.rumeysaturker.supermomkotlinapp.utils.ProfilePostListRecyclerAdapter
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.tek_post_recycler_item.*
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 4
    private val TAG = "ProfileActivity"

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mUser: FirebaseUser
    lateinit var mRef: DatabaseReference
    lateinit var tumGonderiler: ArrayList<UserPosts>
    var myRecyclerView: AutoPlayVideoRecyclerView? = null

    var ilkAcilis = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference

        tumGonderiler = ArrayList<UserPosts>()
        setupToolBar()


        supportFragmentManager.addOnBackStackChangedListener(object : FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {

                var backStacktekiElemanSayisi = supportFragmentManager.backStackEntryCount
                if (backStacktekiElemanSayisi == 0) {
                    //Log.e("MMM","Back stackte eleman yok")
                    tumlayout.visibility = View.VISIBLE
                    profileContainer.visibility = View.GONE
                } else {
                    tumlayout.visibility = View.GONE
                    profileContainer.visibility = View.VISIBLE
                    //Log.e("MMM","*****************************************")
                    // for(i in 0..backStacktekiElemanSayisi-1)
                    //Log.e("MMM",""+supportFragmentManager.getBackStackEntryAt(i).name)

                }

            }

        })


        takipciSayilariniGuncelle()



        kullaniciPostlariniGetir(mUser.uid)

       // imgGrid.setOnClickListener {

        //    setupRecyclerView(1)

        //  }

       // imgList.setOnClickListener {

        //    setupRecyclerView(2)
       // }

    }

    private fun kullaniciBilgileriniGetir() {

        tvProfilDuzenle.isEnabled = false
        imgProfileSettings.isEnabled = false

        mRef.child("users").child(mUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.getValue() != null) {
                    var okunanKullaniciBilgileri = p0!!.getValue(Users::class.java)


                    EventBus.getDefault().postSticky(EventBusDataEvents.KulaniciBilgileriniGonder(okunanKullaniciBilgileri))
                    tvProfilDuzenle.isEnabled = true
                    imgProfileSettings.isEnabled = true

                    tvProfilAdiToolbar.setText(okunanKullaniciBilgileri!!.user_name)
                    tvProfilGercekAdi.setText(okunanKullaniciBilgileri!!.adi_soyadi)
                    tvFollowerSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.follower)
                    tvFollowingSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.following)
                    tvPostSayisi.setText(okunanKullaniciBilgileri!!.user_detail!!.post)

                    if (ilkAcilis) {
                        ilkAcilis = false
                        var imgUrl: String = okunanKullaniciBilgileri!!.user_detail!!.profile_picture!!
                        UniversalImageLoader.setImage(imgUrl, circleProfileImage, progressBar, "")
                    }


                    if (!okunanKullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()) {
                        tvBiyografi.visibility = View.VISIBLE
                        tvBiyografi.setText(okunanKullaniciBilgileri!!.user_detail!!.biography!!)
                    } else {
                        tvBiyografi.visibility = View.GONE
                    }
                    if (!okunanKullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()) {
                        tvWebSitesi.visibility = View.VISIBLE
                        tvWebSitesi.setText(okunanKullaniciBilgileri!!.user_detail!!.web_site!!)
                    } else {
                        tvWebSitesi.visibility = View.GONE
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

    tvProfilDuzenle.setOnClickListener {

        tumlayout.visibility= View.INVISIBLE
        profileContainer.visibility=View.VISIBLE
        var transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.profileContainer,ProfileEditFragment())
        transaction.addToBackStack("editProfileFragmentEklendi")
        transaction.commit()

    }}

    private fun takipciSayilariniGuncelle() {

        progressBar9.visibility = View.VISIBLE
        tumlayout.visibility = View.INVISIBLE

        mRef = FirebaseDatabase.getInstance().reference

        mRef.child("following").child(mUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var takipEttikleriminSayisi = p0!!.childrenCount.toString()

                mRef.child("follower").child(mUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var takipEdenlerinSayisi = p0!!.childrenCount.toString()

                        mRef.child("users").child(mUser.uid).child("user_detail").child("following").setValue(takipEttikleriminSayisi)
                        mRef.child("users").child(mUser.uid).child("user_detail").child("follower").setValue(takipEdenlerinSayisi)


                        kullaniciBilgileriniGetir()
                    }

                })
            }

        })

    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)//fonksiyona companion object kullandığımız için direkt erişebiliyoruz.
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun kullaniciPostlariniGetir(kullaniciID: String) {


        mRef.child("users").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var userID = kullaniciID
                var kullaniciAdi = p0!!.getValue(Users::class.java)!!.user_name
                var kullaniciFotoURL = p0!!.getValue(Users::class.java)!!.user_detail!!.profile_picture


                mRef.child("posts").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0!!.hasChildren()) {
                            //Log.e("HATA","COCUK VAR")
                            for (ds in p0!!.children) {

                                var eklenecekUserPosts = UserPosts()
                                eklenecekUserPosts.userID = userID
                                eklenecekUserPosts.userName = kullaniciAdi
                                eklenecekUserPosts.userPhotoURL = kullaniciFotoURL
                                eklenecekUserPosts.postID = ds.getValue(Posts::class.java)!!.post_id
                                eklenecekUserPosts.postURL = ds.getValue(Posts::class.java)!!.file_url
                                eklenecekUserPosts.postAciklama = ds.getValue(Posts::class.java)!!.aciklama
                                eklenecekUserPosts.postYuklenmeTarih = ds.getValue(Posts::class.java)!!.yuklenme_tarih

                                tumGonderiler.add(eklenecekUserPosts)

                            }


                        } else {
                            progressBar9.visibility = View.GONE
                            tumlayout.visibility = View.VISIBLE
                        }

                        setupRecyclerView()

                    }

                })


            }


        })


    }

    //1 ise grid 2 ise list view şeklinde veriler gösterilir
    private fun setupRecyclerView() {

        if (tumGonderiler.size == 0) {
            //golgelik2.visibility = View.GONE

            profileRecyclerView.visibility = View.GONE
            containerGonderiYok.visibility = View.VISIBLE
        } else {
            golgelik2.visibility = View.VISIBLE

            profileRecyclerView.visibility = View.VISIBLE
            containerGonderiYok.visibility = View.GONE
        }


      /*  if (layoutCesidi == 1) {

            if (myRecyclerView != null && myRecyclerView!!.getHandingVideoHolder() != null) {
                myRecyclerView!!.getHandingVideoHolder().stopVideo();
                //Log.e("HATA","Gridlayout aktif, videoları durdur")
            }


          //  imgGrid.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
           // imgList.setColorFilter(ContextCompat.getColor(this, R.color.siyah), PorterDuff.Mode.SRC_IN)
            myRecyclerView = profileRecyclerView
            myRecyclerView!!.setHasFixedSize(true)
           // myRecyclerView!!.adapter = ProfilePostGridRecyclerAdapter(tumGonderiler, this)

            myRecyclerView!!.layoutManager = GridLayoutManager(this, 3)

            progressBar9.visibility = View.GONE
            tumlayout.visibility = View.VISIBLE

        } else if (layoutCesidi == 2) {*/

            if (myRecyclerView != null && myRecyclerView?.getHandingVideoHolder() != null) {
                myRecyclerView!!.getHandingVideoHolder().playVideo();
                //Log.e("HATA","Listview aktif, varsa bekleyen videoyu oynat")
            }
           // imgGrid.setColorFilter(ContextCompat.getColor(this, R.color.siyah), PorterDuff.Mode.SRC_IN)
           // imgList.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
            myRecyclerView = profileRecyclerView
            myRecyclerView!!.layoutManager = CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false)
         //? alt satır kontrol et!!
            myRecyclerView!!.adapter = ProfilePostListRecyclerAdapter(this,tumGonderiler)
            progressBar9.visibility = View.GONE
            tumlayout.visibility = View.VISIBLE


       // }

    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            tumlayout.visibility = View.GONE
            profileContainer.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        } else {
            tumlayout.visibility = View.VISIBLE
            profileContainer.visibility = View.INVISIBLE
            super.onBackPressed()
            overridePendingTransition(0, 0)
        }

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


    override fun onResume() {
        setupNavigationView()
        super.onResume()
        if (myRecyclerView != null && myRecyclerView?.getHandingVideoHolder() != null) {
            myRecyclerView!!.getHandingVideoHolder().playVideo();
            //Log.e("HATA","RESUME CALISIYO")
        }

    }

    override fun onPause() {
        super.onPause()
        if (myRecyclerView != null && myRecyclerView!!.getHandingVideoHolder() != null) {
            myRecyclerView!!.getHandingVideoHolder().stopVideo();
            //Log.e("HATA","PAUSE CALISIYO")
        }
    }

}