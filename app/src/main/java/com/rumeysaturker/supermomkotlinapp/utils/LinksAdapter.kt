package com.rumeysaturker.supermomkotlinapp.utils

import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rumeysaturker.supermomkotlinapp.generic.CommentFragment
import com.rumeysaturker.supermomkotlinapp.home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.models.UserPosts
import com.rumeysaturker.supermomkotlinapp.profile.ProfileActivity
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.VideoRecyclerView.Video
import com.rumeysaturker.supermomkotlinapp.VideoRecyclerView.VideoView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hoanganhtuan95ptit.autoplayvideorecyclerview.VideoHolder
import com.rumeysaturker.supermomkotlinapp.game.Links
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.Comparator



    class LinksAdapter(var context: Context, var tumGonderiler: ArrayList<Links>) : RecyclerView.Adapter<LinksAdapter.MyViewHolder>() {

        init {
            Collections.sort(tumGonderiler, object : Comparator<Links> {
                override fun compare(o1: Links?, o2: Links?): Int {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


            })

        }

        override fun getItemCount(): Int {
            return tumGonderiler.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var viewHolder = LayoutInflater.from(context).inflate(R.layout.tek_post_recycler_item, parent, false)

            return MyViewHolder(viewHolder, context)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        }


        class MyViewHolder(itemView: View?, myProfileActivity: Context) : VideoHolder(itemView) {

            var olusturulanElemanVideoMu = false

            override fun getVideoLayout(): View? {

                if (olusturulanElemanVideoMu) {
                    return myVideo
                } else return null

            }

            override fun playVideo() {
                if (olusturulanElemanVideoMu) {


                    myVideo.play(object : VideoView.OnPreparedListener {
                        override fun onPrepared() {
                            videoKaranlikImage.visibility = View.GONE
                            videoCameraAnim.stop()
                        }

                    })
                }
            }

            override fun stopVideo() {
                if (olusturulanElemanVideoMu) {
                    videoCameraAnim.stop()
                    myVideo.stop()
                }
            }

            var tumLayout = itemView as ConstraintLayout
            var profileImage = tumLayout.imgUserProfile
            var userNameTitle = tumLayout.tvKullaniciAdiBaslik
            var gonderi = tumLayout.imgPostResim
            var userNameveAciklama = tumLayout.tvKullaniciAdiveAciklama
            var gonderiKacZamanOnce = tumLayout.tvKacZamanOnce
            var yorumYap = tumLayout.imgYorum
            var gonderiBegen = tumLayout.imgBegen
            var myProfileActivity = myProfileActivity
            var mInstaLikeView = tumLayout.insta_like_view
            var begenmeSayisi = tumLayout.tvBegenmeSayisi
            var yorumlariGoster = tumLayout.tvYorumlariGoster
            var myVideo = tumLayout.videoView
            var videoCameraAnim = tumLayout.cameraAnimation
            var videoKaranlikImage = tumLayout.videoKaranlik


            fun setData(position: Int, oankiGonderi: UserPosts, videoMu: Boolean) {

                olusturulanElemanVideoMu = videoMu
                if (olusturulanElemanVideoMu) {
                    myVideo.visibility = View.VISIBLE
                    gonderi.visibility = View.GONE
                    myVideo.setVideo(Video(oankiGonderi.postURL, 0))
                } else {
                    myVideo.visibility = View.GONE
                    gonderi.visibility = View.VISIBLE
                    UniversalImageLoader.setImage(oankiGonderi.postURL!!, gonderi, null, "")
                }

                userNameTitle.setText(oankiGonderi.userName)

                /*
                         var userNameveAciklamaText="<font color=#000>"+oankiGonderi.userName.toString()+"</font>"+" "+oankiGonderi.postAciklama
                         var sonuc:Spanned?=null
                         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                             sonuc= Html.fromHtml(userNameveAciklamaText,Html.FROM_HTML_MODE_LEGACY)
                         }else {
                             sonuc=Html.fromHtml(userNameveAciklamaText)
                         }
                         */
                userNameveAciklama.setText(oankiGonderi.userName.toString() + " " + oankiGonderi.postAciklama.toString())

                UniversalImageLoader.setImage(oankiGonderi.userPhotoURL!!, profileImage, null, "")
                gonderiKacZamanOnce.setText(TimeAgo.getTimeAgo(oankiGonderi.postYuklenmeTarih!!))

                begeniKontrol(oankiGonderi)
                yorumlariGoruntule(position, oankiGonderi)


                yorumYap.setOnClickListener {

                    yorumlarFragmentiniBaslat(oankiGonderi)
                }

                yorumlariGoster.setOnClickListener {
                    yorumlarFragmentiniBaslat(oankiGonderi)
                }

                gonderiBegen.setOnClickListener {

                    var mRef = FirebaseDatabase.getInstance().reference
                    var userID = FirebaseAuth.getInstance().currentUser!!.uid
                    mRef.child("likes").child(oankiGonderi.postID!!).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0!!.hasChild(userID)) {

                                mRef.child("likes").child(oankiGonderi.postID!!).child(userID).removeValue()
                                gonderiBegen.setImageResource(R.drawable.ic_like)
                                //Bildirimler.bildirimKaydet(oankiGonderi.userID!!, Bildirimler.GONDERI_BEGENISI_GERI_CEK, oankiGonderi!!.postID!!)

                            } else {

                                mRef.child("likes").child(oankiGonderi.postID!!).child(userID).setValue(userID)

                                if (!oankiGonderi.userID!!.equals(userID))
                              //      Bildirimler.bildirimKaydet(oankiGonderi.userID!!, Bildirimler.GONDERI_BEGENILDI, oankiGonderi!!.postID!!)
                                gonderiBegen.setImageResource(R.drawable.ic_star_sari)
                                mInstaLikeView.start()
                                begenmeSayisi.visibility = View.VISIBLE
                                begenmeSayisi.setText("" + p0!!.childrenCount!!.toString() + " beğenme")
                            }
                        }


                    })


                }

                var ilkTiklama: Long = 0
                var sonTiklama: Long = 0

                gonderi.setOnClickListener {

                    ilkTiklama = sonTiklama
                    sonTiklama = System.currentTimeMillis()

                    if (sonTiklama - ilkTiklama < 300) {
                        mInstaLikeView.start()

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

                (myProfileActivity as AppCompatActivity).profileContainer.visibility = View.VISIBLE
                (myProfileActivity as AppCompatActivity).profileRoot.visibility = View.INVISIBLE

                var transaction = (myProfileActivity as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.profileContainer, CommentFragment())
                transaction.addToBackStack("commentFragmentEklendi")
                transaction.commit()
            }

            fun yorumlariGoruntule(position: Int, oankiGonderi: UserPosts) {

                var mRef = FirebaseDatabase.getInstance().reference
                mRef.child("comments").child(oankiGonderi.postID!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

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

            fun begeniKontrol(oankiGonderi: UserPosts) {

                var mRef = FirebaseDatabase.getInstance().reference
                var userID = FirebaseAuth.getInstance().currentUser!!.uid
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