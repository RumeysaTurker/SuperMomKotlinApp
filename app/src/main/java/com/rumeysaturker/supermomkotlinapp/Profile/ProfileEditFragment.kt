package com.rumeysaturker.supermomkotlinapp.Profile


import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rumeysaturker.supermomkotlinapp.Models.Users
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import com.rumeysaturker.supermomkotlinapp.utils.UniversalImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import java.lang.Exception


class ProfileEditFragment : Fragment() {

    lateinit var circleProfileImageFragment: CircleImageView
    lateinit var gelenkullaniciBilgileri: Users
    lateinit var mDatabaseRef: DatabaseReference
    lateinit var mStorageRef: StorageReference
    var profilePhotoUri: Uri? = null
    val RESIM_SEC = 100
    var dialogYukleniyor=YukleniyorFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference


        setupKullaniciBilgileri(view)

        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }

        view.tvChangePhoto.setOnClickListener {
            var intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC)
        }

        view.imgBtnDegisiklikKaydet.setOnClickListener {
            if(profilePhotoUri!= null){

             //   var dialogYukleniyor=YukleniyorFragment()
               // dialogYukleniyor.show(activity!!.supportFragmentManager,"yukleniyorFragmenti")
               // dialogYukleniyor.isCancelable=false

                kullaniciProfilResminiGuncelle(view)




          //  dialogYukleniyor.show(activity!!.supportFragmentManager,"yukleniyorFragmenti")
            //kullaniciProfilResminiGuncelle(dialogYukleniyor, UserID)


//var uploadResimProgress=ProgressDialog(activity) progressdiaolog depricate old. için yukleniyorfragmenti oluşturuldu



                /* var uploadTask = mStorageRef.child("users").child(gelenkullaniciBilgileri!!.user_id!!).child(profilePhotoUri!!.lastPathSegment).putFile(profilePhotoUri!!)
                        .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                            override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                                if (p0!!.isSuccessful) {
                                    val downloadUri = p0.getResult()
                                    val url = p0.downloadUrl.toString()
                                    val friendlyMessage = mStorageRef.child("users").child(gelenkullaniciBilgileri.user_name!!.downloadUri.toString()))
                                    mDatabaseRef.push().setValue(friendlyMessage)
                                    // Toast.makeText(activity,"Resim yüklendi"+p0!!.TaskSnapshot.downloadUrl.!!.toString(),Toast.LENGTH_SHORT).show()

                                    Toast.makeText(activity, "Resim yüklendi" + mStorageRef.downloadUrl!!.toString(), Toast.LENGTH_SHORT).show()
                                    mDatabaseRef.child("users").child(gelenkullaniciBilgileri!!.user_id!!).child("user_detail").child("profile_picture")
                                            .setValue(mStorageRef.downloadUrl!!.toString())
                                   */
              //  dialogYukleniyor.dismiss()//işlem başarılı olduğunda kapat
                //kullaniciAdiniGuncelle(view,true)
                  //}else {
                //kullaniciAdiniGuncelle(view,null)
               /*}

           }
           .addOnFailureListener(object : OnFailureListener {
               override fun onFailure(p0: Exception) {
                   Log.e("HATA", p0!!.message)
               }

           })
}*/



                Toast.makeText(activity, "Kullancı güncellendi", Toast.LENGTH_SHORT).show()

            }
        }
        return view
        var intent = Intent(activity, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

    }


        //profilresmi degisti
        //true : basarılı bir şekilde resim storage yüklenmiş ve veritabanına yazılmıstır
        //false : resim yüklenirken hata olusmustur
        //null : kullanıcı resmini değiştirmek istememiştir
        private fun kullaniciAdiniGuncelle(view: View, profilResmiDegisti: Boolean?) {

            if(!gelenkullaniciBilgileri!!.user_name!!.equals(view.etKullaniciAdi.text.toString())){
                if(view.etKullaniciAdi.text.toString().trim().length>5){
                    mDatabaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(object : ValueEventListener{

                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            var userNameKullanimdaMi=false

                            for(ds in p0!!.children){

                                var okunanKullaniciAdi=ds!!.getValue(Users::class.java)!!.user_name

                                if(okunanKullaniciAdi!!.equals(view.etKullaniciAdi.text.toString())){
                                    userNameKullanimdaMi=true
                                    profilBilgileriniGuncelle(view, profilResmiDegisti,false)
                                    break
                                }


                            }

                            if(userNameKullanimdaMi==false){
                                mDatabaseRef.child("users").child(gelenkullaniciBilgileri!!.user_id!!).child("user_name").setValue(view.etKullaniciAdi.text.toString())
                                profilBilgileriniGuncelle(view, profilResmiDegisti,true)

                            }

                        }


                    })
                }else {
                    Toast.makeText(activity,"Kullanıcı adı en az 6 karakter olmalıdır",Toast.LENGTH_SHORT).show()
                }





            }else {
                kullaniciProfilResminiGuncelle(view)
                profilBilgileriniGuncelle(view, profilResmiDegisti,null)
            }

        }

        private fun profilBilgileriniGuncelle(view: View, profilResmiDegisti: Boolean?, userNameDegisti: Boolean?) {

            var profilGuncellendiMi:Boolean?=null

            if (!gelenkullaniciBilgileri!!.adi_soyadi!!.equals(view.etProfileName.text.toString())) {
                if(!view.etProfileName.text.toString().trim().isNullOrEmpty()){
                    mDatabaseRef.child("users").child(gelenkullaniciBilgileri!!.user_id!!).child("adi_soyadi").setValue(view.etProfileName.text.toString())
                    profilGuncellendiMi=true
                }else{
                    Toast.makeText(activity,"Ad Soyad boş olamaz",Toast.LENGTH_SHORT).show()
                }

            }

            if(!gelenkullaniciBilgileri!!.user_detail!!.biography!!.equals(view.etBiyografi.text.toString())){
                mDatabaseRef.child("users").child(gelenkullaniciBilgileri!!.user_id!!).child("user_detail").child("biography").setValue(view.etBiyografi.text.toString())
                profilGuncellendiMi=true
            }

            if(!gelenkullaniciBilgileri!!.user_detail!!.web_site!!.equals(view.etWebSitesi.text.toString())){
                mDatabaseRef.child("users").child(gelenkullaniciBilgileri!!.user_id!!).child("user_detail").child("web_site").setValue(view.etWebSitesi.text.toString())
                profilGuncellendiMi=true
            }

            if(profilResmiDegisti == null && userNameDegisti == null && profilGuncellendiMi == null){
                Toast.makeText(activity,"Değişiklik Yok",Toast.LENGTH_SHORT).show()
            }
            else if(userNameDegisti== false && (profilGuncellendiMi==true || profilResmiDegisti==true)){
                Toast.makeText(activity,"Bilgiler güncelledi ama kullanıcı adı KULLANIMDA",Toast.LENGTH_SHORT).show()
            }else {

                Toast.makeText(activity,"Kullanıcı Güncellendi",Toast.LENGTH_SHORT).show()
                activity!!.onBackPressed()

            }







        }

    //profil fotoğrafı seçme
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESIM_SEC && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {
            profilePhotoUri = data!!.data!!

            circleProfileImage.setImageURI(profilePhotoUri)
            Toast.makeText(activity, "prof seç bloguna girdi", Toast.LENGTH_SHORT).show()

        }
    }

    private fun setupKullaniciBilgileri(view: View?) {
        view!!.etProfileName.setText(gelenkullaniciBilgileri!!.adi_soyadi)
        view!!.etKullaniciAdi.setText(gelenkullaniciBilgileri!!.user_name)
        if (!gelenkullaniciBilgileri!!.user_detail!!.biography!!.isNullOrEmpty()) {
            view!!.etBiyografi.setText(gelenkullaniciBilgileri!!.user_detail!!.biography)
        }
        if (!gelenkullaniciBilgileri!!.user_detail!!.web_site!!.isNullOrEmpty()) {
            view!!.etWebSitesi.setText(gelenkullaniciBilgileri!!.user_detail!!.web_site)
        }
        //setupProfilePicture

        var imgURL = gelenkullaniciBilgileri!!.user_detail!!.profile_picture
        UniversalImageLoader.setImage(imgURL!!, view!!.circleProfileImage, view!!.progressBar, "")
        Toast.makeText(activity, "Fotograf seçti", Toast.LENGTH_SHORT).show()

    }

    private fun kullaniciProfilResminiGuncelle(view: View) {
        val file = profilePhotoUri
        val mStorageRef = FirebaseStorage.getInstance().reference
        val profiliGuncellenenUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val profilePicReference = mStorageRef.child("images/users/" + profiliGuncellenenUserID + "/profile_picture/" + file!!.lastPathSegment)
        var dialogYukleniyor=YukleniyorFragment()
       // dialogYukleniyor.show(activity!!.supportFragmentManager,"yukleniyorFragmenti")
        dialogYukleniyor.isCancelable=false

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
        val uploadTask = profilePicReference.putFile(file)
        uploadTask.addOnFailureListener() {
            Toast.makeText(activity, "prof shekli yuklenende problem oldu", Toast.LENGTH_SHORT).show()
        //   dialogYukleniyor.dismiss()
            kullaniciAdiniGuncelle(view,true)
            //updateUserName(view!!,false,userID)
        }.addOnSuccessListener { it ->
            var urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                profilePicReference.downloadUrl
            }.addOnCompleteListener {
               if (it.isSuccessful) {
                    if (activity != null) {
                        Toast.makeText(activity, "prof resmi yüklendi", Toast.LENGTH_SHORT).show()

                     //    dialogYukleniyor.dismiss()
                        //updateUserName(view!!,true,userID)
                    }
                   profilePhotoUri = null
                   mDatabaseRef.child("users").child(profiliGuncellenenUserID).child("user_detail").child("profile_picture").setValue(it.result.toString())

                   //mDatabaseRef.child("users").child(profiliGuncellenenUserID).child("profile_picture").setValue(it.result.toString())

               }
            }.addOnFailureListener(object:OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Log.e("HATA", p0!!.message)
                    kullaniciAdiniGuncelle(view,false)
                }

            })
        }}else
        kullaniciAdiniGuncelle(view,null)
    }


    /***************************EVENTBUS*****************************/
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullanıcıBilgileri: EventBusDataEvents.KulaniciBilgileriniGonder) {

        gelenkullaniciBilgileri = kullanıcıBilgileri!!.kullanici!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }


}
