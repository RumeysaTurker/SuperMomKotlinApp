package com.rumeysaturker.supermomkotlinapp.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.rumeysaturker.supermomkotlinapp.home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.models.Users
import com.rumeysaturker.supermomkotlinapp.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var mRef: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupAuthListener()

        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        init()
    }


    fun init() {
        etEmailTelorUserName.addTextChangedListener(watcher)
        etSifre.addTextChangedListener(watcher)

        btnGirisYap.setOnClickListener {
            if(etEmailTelorUserName.text.toString().trim().length >= 6 && etSifre.text.toString().trim().length >= 6){

                oturumAcacakKullaniciyiDenetle(etEmailTelorUserName.text.toString(), etSifre.text.toString())

            }else{
                Toast.makeText(this,"Kullanıcı adı veya şifre en az 6 karakter olmalıdır",Toast.LENGTH_SHORT).show()

            }}
        tvGirisYap.setOnClickListener {
            //loginActivity'den RegisterActivity'e git
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }

    private fun oturumAcacakKullaniciyiDenetle(emailPhoneNumberUserName: String, sifre: String) {
        var kullaniciBulundu = false
        mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.getValue() != null ){
                    for (ds in p0!!.children) {

                        var okunanKullanici = ds.getValue(Users::class.java)

                        if (!okunanKullanici!!.email!!.isNullOrEmpty() && okunanKullanici!!.email!!.toString().equals(emailPhoneNumberUserName)) {

                            oturumAc(okunanKullanici, sifre, false)
                            kullaniciBulundu=true
                            break

                        } else if (!okunanKullanici!!.user_name!!.isNullOrEmpty() && okunanKullanici!!.user_name!!.toString().equals(emailPhoneNumberUserName)) {
                            oturumAc(okunanKullanici, sifre, false)
                            kullaniciBulundu=true
                            break
                        } else if (!okunanKullanici!!.phone_number!!.isNullOrEmpty() && okunanKullanici!!.phone_number!!.toString().equals(emailPhoneNumberUserName)) {

                            oturumAc(okunanKullanici, sifre, true)
                            kullaniciBulundu=true
                            break
                        }

                    }

                    if(kullaniciBulundu==false){

                        Toast.makeText(this@LoginActivity,"Kullanıcı Bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun oturumAc(okunanKullanici: Users, sifre: String, telefonIleGiris: Boolean) {
        var girisYapacakEmail = ""
        if (telefonIleGiris == true) {
            girisYapacakEmail = okunanKullanici.email_phone_number.toString()
        } else {
            if(!okunanKullanici.email.toString().trim().isNullOrEmpty() && okunanKullanici.email_phone_number.toString().trim().isNullOrEmpty()){
                girisYapacakEmail = okunanKullanici.email.toString()
            }else {
                girisYapacakEmail = okunanKullanici.email_phone_number.toString()
            }
        }
        mAuth.signInWithEmailAndPassword(girisYapacakEmail, sifre)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0!!.isSuccessful) {
                            fcmTokenKaydet()
                            Toast.makeText(this@LoginActivity, "Oturum açıldı :" + mAuth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Kullanıcı Adı/Şifre Hatalı :", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
    }
    private fun fcmTokenKaydet() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            var token=it.token
            yeniTokenVeritabaninaKaydet(token)
        }
    }
    private fun yeniTokenVeritabaninaKaydet(yeniToken: String) {
        if(FirebaseAuth.getInstance().currentUser != null ){
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("fcm_token").setValue(yeniToken)
        }
    }
    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etEmailTelorUserName.text.toString().length >= 6 && etSifre.text.toString().length >= 6) {
                btnGirisYap.isEnabled = true
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.beyaz))
                btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)
            } else {
                btnGirisYap.isEnabled = false
                btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.colorAccent))
                btnGirisYap.setBackgroundResource(R.drawable.register_button)
            }
        }

    }

    //kullanıcının oturum açıp açmadığıyla ilgili verileri tutan listener
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    var intent = Intent(this@LoginActivity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)//çıkış yapıldığında geri butonuna basınca homeactivityden başlatmaya çalışmasın
                    startActivity(intent)
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
