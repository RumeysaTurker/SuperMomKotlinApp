package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hoanganhtuan95ptit.autoplayvideorecyclerview.VideoHolder
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.VideoRecyclerView.Video
import com.rumeysaturker.supermomkotlinapp.VideoRecyclerView.VideoView
import com.rumeysaturker.supermomkotlinapp.generic.CommentFragment
import com.rumeysaturker.supermomkotlinapp.home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.models.UserPosts
import com.rumeysaturker.supermomkotlinapp.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import android.text.Spannable
import android.text.SpannableString
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.rumeysaturker.supermomkotlinapp.generic.UserProfileActivity


class HomeFragmentRecyclerAdapter(var context: Context, var tumGonderiler: ArrayList<UserPosts>) : RecyclerView.Adapter<HomeFragmentRecyclerAdapter.MyViewHolder>() {


    override fun getItemCount(): Int {
        return tumGonderiler.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var viewHolder = LayoutInflater.from(context).inflate(R.layout.tek_post_recycler_item, parent, false)
      //  viewHolder.progressBar10.visibility=View.GONE
        return MyViewHolder(viewHolder, context)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var videoMu = false
        var dosyaYolu = tumGonderiler.get(position).postURL
        //https://asdasdasdasdasdasd.mp4
        var dosyaTuru = dosyaYolu!!.substring(dosyaYolu.lastIndexOf("."), dosyaYolu.lastIndexOf(".") + 4)

        if (dosyaTuru.equals(".mp4")) {
            holder.videoCameraAnim.start()
            holder.progressbar.visibility=View.GONE
            holder.videoKaranlikImage.visibility = View.VISIBLE
            videoMu = true
        }

        holder.setData(position, tumGonderiler.get(position), videoMu)
    }


    class MyViewHolder(itemView: View?, myHomeActivity: Context) : VideoHolder(itemView) {

        var tumLayout = itemView as ConstraintLayout
        var profileImage = tumLayout.imgUserProfile
        var userNameTitle = tumLayout.tvKullaniciAdiBaslik
        var gonderi = tumLayout.imgPostResim
        var framelayout = tumLayout.frameLayout
        var userNameveAciklama = tumLayout.tvKullaniciAdiveAciklama
        var gonderiKacZamanOnce = tumLayout.tvKacZamanOnce
        var yorumYap = tumLayout.imgYorum
        var gonderiBegen = tumLayout.imgBegen
        var myHomeActivity = myHomeActivity
        var mInstaLikeView = tumLayout.insta_like_view
        var begenmeSayisi = tumLayout.tvBegenmeSayisi
        var yorumlariGoster = tumLayout.tvYorumlariGoster
        var myVideo = tumLayout.videoView
        var videoCameraAnim = tumLayout.cameraAnimation
        var videoKaranlikImage = tumLayout.videoKaranlik
        var progressbar = tumLayout.progressBar10



        var olusturulanElemanVideoMu = false

        override fun getVideoLayout(): View? {

            if (olusturulanElemanVideoMu) //==true
             {
                return myVideo
                // progressbar.visibility=View.GONE
            } else return null

        }

        override fun playVideo() {
            if (olusturulanElemanVideoMu) {


                myVideo.play(object : VideoView.OnPreparedListener {
                    override fun onPrepared() {
                        progressbar.visibility=View.GONE
                        videoKaranlikImage.visibility = View.GONE
                        videoCameraAnim.stop()
                    }

                })
            }
        }

        override fun stopVideo() {
            if (olusturulanElemanVideoMu) {
                progressbar.visibility=View.GONE
                videoCameraAnim.stop()
                myVideo.stop()
            }
        }


        fun setData(position: Int, oankiGonderi: UserPosts, videoMu: Boolean) {

            olusturulanElemanVideoMu = videoMu
            if (olusturulanElemanVideoMu) {
                progressbar.visibility=View.GONE
                myVideo.visibility = View.VISIBLE
                gonderi.visibility = View.GONE
                myVideo.setVideo(Video(oankiGonderi.postURL, 0))
            } else {
               // progressbar.visibility=View.GONE
                myVideo.visibility = View.GONE
                gonderi.visibility = View.VISIBLE
                UniversalImageLoader.setImage(oankiGonderi.postURL!!, gonderi, progressbar, "")
            }




            userNameTitle.setText(oankiGonderi.userName)

            userNameTitle.setOnClickListener {

                var tiklanilanUserID = oankiGonderi.userID

                if (!tiklanilanUserID!!.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                  var intent = Intent(myHomeActivity, UserProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                   intent.putExtra("secilenUserID", oankiGonderi.userID)
                   myHomeActivity.startActivity(intent)
                } else {

                    var intent = Intent(myHomeActivity, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    myHomeActivity.startActivity(intent)
                }


            }

            val spannable = SpannableString(oankiGonderi.userName.toString() + " " + oankiGonderi.postAciklama.toString())
            spannable.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    0, oankiGonderi.userName.toString().length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


            userNameveAciklama.setText(spannable)

            UniversalImageLoader.setImage(oankiGonderi.userPhotoURL!!, profileImage, null, "")
            gonderiKacZamanOnce.setText(TimeAgo.getTimeAgo(oankiGonderi.postYuklenmeTarih!!))

            begeniKontrol(oankiGonderi)
            yorumlariGoruntule(position, oankiGonderi)


            yorumYap.setOnClickListener {

                if (myVideo.visibility == View.VISIBLE) {
                    myVideo.stop()
                }
                yorumlarFragmentiniBaslat(oankiGonderi)
            }

            yorumlariGoster.setOnClickListener {
                if (myVideo.visibility == View.VISIBLE) {
                    myVideo.stop()
                }
                yorumlarFragmentiniBaslat(oankiGonderi)
            }

            gonderiBegen.setOnClickListener {

                var mRef = FirebaseDatabase.getInstance().reference
                var userID = FirebaseAuth.getInstance().currentUser!!.uid
                mRef.child("likes").child(oankiGonderi.postID!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0!!.hasChild(userID)) {

                            mRef.child("likes").child(oankiGonderi.postID!!).child(userID).removeValue()
                            //  Bildirimler.bildirimKaydet(oankiGonderi.userID!!,Bildirimler.GONDERI_BEGENISI_GERI_CEK,oankiGonderi!!.postID!!)
                            //Log.e("VVV","HA BEGENME gerı cekme BILDIRIM:"+oankiGonderi!!.postID)
                            gonderiBegen.setImageResource(R.drawable.ic_star)

                        } else {
                            //firebasete likes diye bir çocuk oluştur ve kullanıcı idyi ata
                            mRef.child("likes").child(oankiGonderi.postID!!).child(userID).setValue(userID)


                            if (!oankiGonderi.userID!!.equals(userID))
                            //   Bildirimler.bildirimKaydet(oankiGonderi.userID!!,Bildirimler.GONDERI_BEGENILDI,oankiGonderi!!.postID!!)

                            //Log.e("VVV","HA BEGENME BILDIRIM:"+oankiGonderi!!.postID)
                                gonderiBegen.setImageResource(R.drawable.ic_star_sari)
                            mInstaLikeView.start()
                            begenmeSayisi.visibility = View.VISIBLE
                            begenmeSayisi.setText("" + p0!!.childrenCount!!.toString() + " beğenme")
                        }
                    }


                })


            }
//çift tıkladığında beğenme
            var ilkTiklama: Long = 0
            var sonTiklama: Long = 0

            gonderi.setOnClickListener {

                ilkTiklama = sonTiklama
                sonTiklama = System.currentTimeMillis()

                if (sonTiklama - ilkTiklama < 300) {
                    mInstaLikeView.start()
//veritabanına kaydet
                    FirebaseDatabase.getInstance().getReference().child("likes").child(oankiGonderi.postID!!)
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(FirebaseAuth.getInstance().currentUser!!.uid)


                    sonTiklama = 0
                }


            }

            myVideo.setOnClickListener {

                ilkTiklama = sonTiklama
                sonTiklama = System.currentTimeMillis()

                if (sonTiklama - ilkTiklama < 300) {
                    mInstaLikeView.start()

                    FirebaseDatabase.getInstance().getReference().child("likes").child(oankiGonderi.postID!!)
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(FirebaseAuth.getInstance().currentUser!!.uid)


                    sonTiklama = 0
                }


            }


        }

        fun yorumlarFragmentiniBaslat(oankiGonderi: UserPosts) {
            EventBus.getDefault().postSticky(EventBusDataEvents.YorumYapilacakGonderininIDsiniGonder(oankiGonderi!!.postID))

            (myHomeActivity as HomeActivity).homeViewPager.visibility = View.INVISIBLE
            (myHomeActivity as HomeActivity).homeFragmentContainer.visibility = View.VISIBLE


            var transaction = (myHomeActivity as HomeActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.homeFragmentContainer, CommentFragment())
            transaction.addToBackStack("commentFragmentEklendi")
            transaction.commit()
        }

        fun yorumlariGoruntule(position: Int, oankiGonderi: UserPosts) {

            var mRef = FirebaseDatabase.getInstance().reference
            mRef.child("comments").child(oankiGonderi.postID!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {

                    var yorumSayisi = 0

                    for (ds in p0!!.children) {
                        if (!ds!!.key.toString().equals(oankiGonderi!!.postID)) {
                            yorumSayisi++
                        }
                    }


                    if (yorumSayisi >= 1) {
                        yorumlariGoster.visibility = View.VISIBLE
                        yorumlariGoster.setText(yorumSayisi.toString() + " yorumun tümünü gör")
                    } else {
                        yorumlariGoster.visibility = View.GONE
                    }

                }


            })

        }
//uygulamayı tekrar başlattığında beğendiysek sarı yıldız olsun
        fun begeniKontrol(oankiGonderi: UserPosts) {

            var mRef = FirebaseDatabase.getInstance().reference
            var userID = FirebaseAuth.getInstance().currentUser!!.uid
    //addValueEventListener-> anlık olarak değişiklikleri algılaması için
            mRef.child("likes").child(oankiGonderi.postID!!).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0!!.getValue() != null) {
                        begenmeSayisi.visibility = View.VISIBLE
                        begenmeSayisi.setText("" + p0!!.childrenCount!!.toString() + " beğenme")
                    } else {
                        begenmeSayisi.visibility = View.GONE
                    }

                    if (p0!!.hasChild(userID)) {
                        gonderiBegen.setImageResource(R.drawable.ic_star_sari)
                    } else {
                        gonderiBegen.setImageResource(R.drawable.ic_star)
                    }
                }


            })


        }


    }
}