package com.rumeysaturker.supermomkotlinapp.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rumeysaturker.supermomkotlinapp.home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.models.UserDetails
import com.rumeysaturker.supermomkotlinapp.models.Users
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_kayit.*
import kotlinx.android.synthetic.main.fragment_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class KayitFragment : Fragment() {

    var telNo = ""
    var verificationID = ""
    var gelenKod = ""
    var gelenEmail = ""
    var emailIleKayitislemi = true
    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_kayit, container, false)

        progressBar = view.pbKullaniciKayit

        mAuth = FirebaseAuth.getInstance()
        /*  if (mAuth.currentUser != null) {
    mAuth.signOut()
}*/
        mRef = FirebaseDatabase.getInstance().reference

        view.tvGirisYap.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


        view.etAdSoyad.addTextChangedListener(watcher)
        view.etKullaniciAdi.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)

        view.btnGiris.setOnClickListener {

            var userNameKullanimdaMi = false
            mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0!!.getValue() != null) {
                        for (user in p0!!.children) {
                            var okunanKullanici = user.getValue(Users::class.java)
                            if (okunanKullanici!!.user_name!!.equals(view.etKullaniciAdi.text.toString())) {
                                Toast.makeText(activity, "Bu kullanıcı adı zaten kullanımda", Toast.LENGTH_SHORT).show()
                                userNameKullanimdaMi = true
                                break
                            }
                        }


                            progressBar.visibility = View.VISIBLE
                            //kullanıcı mail ile kayıt olmak istiyorsa
                            if (emailIleKayitislemi) {
                                var sifre = view.etSifre.text.toString()
                                var adSoyad = view.etAdSoyad.text.toString()
                                var userName = view.etKullaniciAdi.text.toString()

                                mAuth.createUserWithEmailAndPassword(gelenEmail, sifre)
                                        .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                                            override fun onComplete(p0: Task<AuthResult>) {
                                                if (p0!!.isSuccessful) {
                                                    Toast.makeText(activity, "Oturum email ile açıldı Uid: " + mAuth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
                                                    var userID = mAuth.currentUser!!.uid.toString()
                                                    var kaydedilecekKullaniciDetaylari = UserDetails("0", "0", "0", "", "", "")
                                                    //oturum açan kullanıcının verilerini database'e kaydet
                                                    var kaydedilecekKullanici = Users(gelenEmail, sifre, userName, adSoyad, "", "", userID, kaydedilecekKullaniciDetaylari)
                                                    mRef.child("users").child(userID).setValue(kaydedilecekKullanici).addOnCompleteListener(object : OnCompleteListener<Void> {
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if (p0!!.isSuccessful) {
                                                                Toast.makeText(activity, "Kullanıcı kaydedildi", Toast.LENGTH_SHORT).show()
                                                                var intent = Intent(activity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                                startActivity(intent)
                                                                progressBar.visibility = View.INVISIBLE
                                                            } else {
                                                                progressBar.visibility = View.INVISIBLE
                                                                mAuth.currentUser!!.delete()//kullanıcıyı sil
                                                                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                                            override fun onComplete(p0: Task<Void>) {
                                                                                if (p0!!.isSuccessful) {//silme işlemi olduysa kullanıcı kaydedilemedi
                                                                                    Toast.makeText(activity, "Kullanıcı kaydedilemedi, tekrar deneyin", Toast.LENGTH_SHORT).show()
                                                                                }
                                                                            }
                                                                        })
                                                            }
                                                        }

                                                    })
                                                } else {
                                                    progressBar.visibility = View.INVISIBLE
                                                    Toast.makeText(activity, "Oturum açılamadı: " + p0!!.exception, Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                        })
                            }
                            //kullanıcı telefon no ile kayıt olmak istiyorsa
                        else {
                            var sifre = view.etSifre.text.toString()
                            var sahteEmail = telNo + "@rumeysa.com" //"+905557930000@rumeysa.com
                            var adSoyad = view.etAdSoyad.text.toString()
                            var userName = view.etKullaniciAdi.text.toString()

                            mAuth.createUserWithEmailAndPassword(sahteEmail, sifre)
                                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                                        override fun onComplete(p0: Task<AuthResult>) {
                                            if (p0!!.isSuccessful) {
                                                Toast.makeText(activity, "Oturum tel no açıldı Uid:" + mAuth.currentUser!!.uid, Toast.LENGTH_SHORT).show()

                                                var userID = mAuth.currentUser!!.uid.toString()

                                                //oturum açan kullanıcının verilerini database'e kaydet
                                                var kaydedilecekKullaniciDetaylari=UserDetails("0","0","0","","","")
                                                var kaydedilecekKullanici = Users("", sifre, userName, adSoyad, telNo, sahteEmail, userID,kaydedilecekKullaniciDetaylari)
                                                mRef.child("users").child(userID).setValue(kaydedilecekKullanici).addOnCompleteListener(object : OnCompleteListener<Void> {
                                                    override fun onComplete(p0: Task<Void>) {
                                                       if (p0!!.isSuccessful) {//başarılı bir şekilde veri tabanına kaydedilmişse
                                                            Toast.makeText(activity, "Kullanıcı kaydedildi", Toast.LENGTH_SHORT).show()
                                                            progressBar.visibility = View.INVISIBLE
                                                            var intent = Intent(activity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                            startActivity(intent)
                                                        } else {
                                                            progressBar.visibility = View.INVISIBLE
                                                            mAuth.currentUser!!.delete()//kullanıcıyı sil
                                                                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                                        override fun onComplete(p0: Task<Void>) {
                                                                            if (p0!!.isSuccessful) {//silme işlemi olduysa kullanıcı kaydedilemedi
                                                                                Toast.makeText(activity, "Kullanıcı kaydedilemedi, tekrar deneyin", Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        }
                                                                    })
                                                        }
                                                    }
                                                })
                                            } else {
                                                progressBar.visibility = View.INVISIBLE
                                                Toast.makeText(activity, "Oturum açılamadı: " + p0!!.exception, Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    })
                        }
                    }

                }
            })

        }

        return view
    }

    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length > 5) {//sifre vs. 6 karakterden az olamayacağı için bu kontrolü yapıyoruz
                if (etAdSoyad.text.toString().length > 5 && etKullaniciAdi.text.toString().length > 5 && etSifre.text.toString().length > 5) {
                    btnGiris.isEnabled = true
                    btnGiris.setTextColor((ContextCompat.getColor(activity!!, R.color.beyaz)))
                    btnGiris.setBackgroundResource(R.drawable.register_button_aktif)

                } else {
                    btnGiris.isEnabled = false//6 karakterden azsa butonu aktifleştirme!
                    btnGiris.setTextColor((ContextCompat.getColor(activity!!, R.color.colorAccent)))
                    btnGiris.setBackgroundResource(R.drawable.register_button)

                }
            } else {
                btnGiris.isEnabled = false//6 karakterden azsa butonu aktifleştirme!
                btnGiris.setTextColor((ContextCompat.getColor(activity!!, R.color.colorAccent)))
                btnGiris.setBackgroundResource(R.drawable.register_button)
            }
        }

    }

    /***************************EVENTBUS*****************************/
    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri: EventBusDataEvents.KayitBilgileriniGonder) {
        if (kayitBilgileri.emailKayit == true) {
            emailIleKayitislemi = true
            gelenEmail = kayitBilgileri.email!!

            Toast.makeText(activity, "Gelen email: " + gelenEmail, Toast.LENGTH_SHORT).show()
            Log.e("rümeysa", "Gelen email:" + gelenEmail)
        } else {
            emailIleKayitislemi = false
            telNo = kayitBilgileri.telNo!!
            verificationID = kayitBilgileri.verificationID!!
            gelenKod = kayitBilgileri.code!!
            Toast.makeText(activity, "Gelen kod: " + gelenKod + "VerificationID: " + verificationID, Toast.LENGTH_SHORT).show()
        }

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
