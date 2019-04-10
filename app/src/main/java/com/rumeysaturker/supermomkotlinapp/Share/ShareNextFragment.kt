package com.rumeysaturker.supermomkotlinapp.share



import com.rumeysaturker.supermomkotlinapp.R

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import  com.rumeysaturker.supermomkotlinapp.home.HomeActivity
import  com.rumeysaturker.supermomkotlinapp.models.Posts

import  com.rumeysaturker.supermomkotlinapp.utils.FileOperations
import  com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_compressand_loading.*
import kotlinx.android.synthetic.main.fragment_share_next.*
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception


class ShareNextFragment : Fragment() {

    var secilenDosyaYolu: String? = null
    var dosyaTuruResimMi: Boolean? = null

    lateinit var mAuth: FirebaseAuth
    lateinit var mUser: FirebaseUser
    lateinit var mRef: DatabaseReference
    lateinit var mStorageReference: StorageReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_next, container, false)

        UniversalImageLoader.setImage(secilenDosyaYolu!!, view!!.imgSecilenResim, null, "file:/")

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference
        mStorageReference = FirebaseStorage.getInstance().reference


        view.tvIleriButton.setOnClickListener {

            //resim dosyasını sıkıstır
            if (dosyaTuruResimMi == true) {

                FileOperations.compressResimDosya(this, secilenDosyaYolu)

            }
            //video dosyasını sıkıstır
            else if (dosyaTuruResimMi == false) {

                FileOperations.compressVideoDosya(this,secilenDosyaYolu!!)

            }


        }

        view.imgClose.setOnClickListener {
//shareActivitydeki onBackPressed metodunu çağırıyoruz
            this.activity!!.onBackPressed()

        }

        return view
    }

    private fun veritabaninaBilgileriYaz(yuklenenFileUrl: String) {

        var postID = mRef.child("posts").child(mUser.uid).push().key
        var yuklenenPost = Posts(mUser.uid, postID, 0, etPostAciklama.text.toString(), yuklenenFileUrl)


        mRef.child("posts").child(mUser.uid).child(postID!!).setValue(yuklenenPost)
        mRef.child("posts").child(mUser.uid).child(postID).child("yuklenme_tarih").setValue(ServerValue.TIMESTAMP) //2424564564

        //gönderi açıklamasını yorum düğümüne ekleyelim firebasede
        if(!etPostAciklama.text.toString().isNullOrEmpty()){

            //var yorumKey=mRef.child("comments").child(postID).push().key
            mRef.child("comments").child(postID).child(postID).child("user_id").setValue(mUser.uid)
            mRef.child("comments").child(postID).child(postID).child("yorum_tarih").setValue(ServerValue.TIMESTAMP)
            mRef.child("comments").child(postID).child(postID).child("yorum").setValue(etPostAciklama.text.toString())
            mRef.child("comments").child(postID).child(postID).child("yorum_begeni").setValue("0")

        }

        postSayisiniGuncelle()

//paylaş butonuna bastıktan sonra homeActiviye git

        var intent=Intent(activity,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

    }

    private fun postSayisiniGuncelle() {
        mRef.child("users").child(mUser.uid).child("user_detail").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var oankiGonderiSayisi=p0!!.child("post").getValue().toString().toInt()
                oankiGonderiSayisi++
                mRef.child("users").child(mUser.uid).child("user_detail").child("post").setValue(oankiGonderiSayisi.toString())
            }


        })
    }


    //////////////////////////// EVENTBUS /////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onSecilenDosyaEvent(secilenResim: EventBusDataEvents.PaylasilacakResmiGonder) {
        secilenDosyaYolu = secilenResim!!.dosyaYolu!!
        dosyaTuruResimMi = secilenResim!!.dosyaTuruResimMi
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    fun uploadStorage(filePath: String?) {


        kullaniciProfilResminiGuncelle(filePath)
    }

    private fun kullaniciProfilResminiGuncelle(filePath: String?) {
        var fileUri = Uri.parse("file://"+filePath)
        val file =fileUri
        val mStorageRef = FirebaseStorage.getInstance().reference
        val profiliGuncellenenUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val profilePicReference = mStorageReference.child("users").child(mUser.uid).child(file.lastPathSegment!!)
        var dialogYukleniyor = CompressandLoadingFragment()
        dialogYukleniyor.show(activity!!.supportFragmentManager, "compressLoadingFragmenti")
        dialogYukleniyor.isCancelable = false
        val uploadTask = profilePicReference.putFile(file)
        uploadTask.addOnFailureListener() {
              Toast.makeText(activity, "prof resmi yuklenirken problem oldu", Toast.LENGTH_SHORT).show()
            dialogYukleniyor.dismiss()


        }.addOnSuccessListener { it ->
            var urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                profilePicReference.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    dialogYukleniyor.dismiss()
                    fileUri = null


                    var postID = mRef.child("posts").child(mUser.uid).push().key
                    var yuklenenPost = Posts(mUser.uid, postID, 0, etPostAciklama.text.toString()!!, it.result.toString())


                    mRef.child("posts").child(mUser.uid).child(postID!!).setValue(yuklenenPost)
                    mRef.child("posts").child(mUser.uid).child(postID).child("yuklenme_tarih").setValue(ServerValue.TIMESTAMP) //2424564564 gibi bir sayı
if(activity!=null) {
    //gönderi açıklamasını yorum düğümüne ekleyelim
    if (!etPostAciklama.text.toString().isNullOrEmpty()) {

        //var yorumKey=mRef.child("comments").child(postID).push().key
        mRef.child("comments").child(postID).child(postID).child("user_id").setValue(mUser.uid)
        mRef.child("comments").child(postID).child(postID).child("yorum_tarih").setValue(ServerValue.TIMESTAMP)
        mRef.child("comments").child(postID).child(postID).child("yorum").setValue(etPostAciklama.text.toString())
        mRef.child("comments").child(postID).child(postID).child("yorum_begeni").setValue("0")

    }

    postSayisiniGuncelle()


    var intent = Intent(activity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
    activity!!.finish()

}
                    dialogYukleniyor.dismiss()

                    //mDatabaseRef.child("users").child(profiliGuncellenenUserID).child("profile_picture").setValue(it.result.toString())

                }
            }
//dialog yükleniyora yüzde ekleme
        }.addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
            override fun onProgress(p0: UploadTask.TaskSnapshot?) {
                var progress = 100.0 * p0!!.bytesTransferred / p0!!.totalByteCount
                //Log.e("HATA", "ILERLEME : " + progress)
                dialogYukleniyor.tvBilgi.text = "%" + progress.toInt().toString() + " yüklendi.."

            }


        })

    }

}