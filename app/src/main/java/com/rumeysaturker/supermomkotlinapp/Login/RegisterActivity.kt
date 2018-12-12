package com.rumeysaturker.supermomkotlinapp.Login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rumeysaturker.supermomkotlinapp.Models.Users
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus


class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    lateinit var manager: FragmentManager
    lateinit var mRef: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //  setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        // mAuth.signOut()//çıkış yap
        mRef = FirebaseDatabase.getInstance().reference

        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)


        init()

    }
        /*  var btnLoginFacebook = findViewById<Button>(R.id.btnLoginFacebook)
          btnLoginFacebook.setOnClickListener {
              View.OnClickListener {
                  //Login
                  callbackManager = CallbackManager.Factory.create()
                  LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
                  LoginManager.getInstance().registerCallback(callbackManager,
                          object : FacebookCallback<LoginResult> {
                              override fun onSuccess(loginResult: LoginResult) {
                                  Log.d("RegisterActiviy", "Facebook token" + loginResult.accessToken.token)
                                  startActivity(Intent(applicationContext, HomeActivity::class.java))
                              }

                              override fun onCancel() {
                                  Log.d("RegisterActivity", "Facebook onCancel")
                              }

                              override fun onError(exception: FacebookException) {
                                  Log.d("RegisterActivity", "Facebook onError")
                              }
                          })

              }

          }*/


    private fun init() {
        tvGirisYap.setOnClickListener {
            var intent = Intent(this@RegisterActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()//geri gel butonunun çalışması için
        }

        tvEposta.setOnClickListener {
            viewTelefon.visibility = View.INVISIBLE
            viewEposta.visibility = View.VISIBLE
            etGirisSablon.setText("")
            etGirisSablon.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            ccp.visibility = View.GONE
            etGirisSablon.setHint("E-Posta")

            btnIleri.isEnabled = false
            btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.colorAccent))
            btnIleri.setBackgroundResource(R.drawable.register_button)
        }
        tvTelefon.setOnClickListener {
            ccp.visibility = View.VISIBLE
            etGirisSablon.width = 200
            viewTelefon.visibility = View.VISIBLE
            viewEposta.visibility = View.INVISIBLE
            etGirisSablon.setText("")
            etGirisSablon.inputType = InputType.TYPE_CLASS_NUMBER
            etGirisSablon.setHint("Telefon")

            btnIleri.isEnabled = false
            btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.colorAccent))
            btnIleri.setBackgroundResource(R.drawable.register_button)
        }

        btnIleri.setOnClickListener {

            if (etGirisSablon.hint.toString().equals("Telefon")) {

                var cepTelefonuKullanimdaMi = false
                if (isValidTelefon(etGirisSablon.text.toString())) {
                    mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0!!.getValue() != null) { //veritabanında veri var mı
                                for (user in p0!!.children) {//p0'ın çocuklarını gez hepsini user'a ata
                                    var okunanKullanici = user.getValue(Users::class.java)//okunanKullanici'ya user'da okuduğum verileri Users modelindeki gibi al
                                    if (okunanKullanici!!.phone_number!!.equals(etGirisSablon.text.toString())) {
                                        Toast.makeText(this@RegisterActivity, "Bu telefon numarası zaten kullanımda", Toast.LENGTH_SHORT).show()
                                        cepTelefonuKullanimdaMi = true
                                        break
                                    }
                                }
                                if(cepTelefonuKullanimdaMi==false){ //yani telefon ilk defa kaydediliyorsa
                                    loginRoot.visibility = View.GONE
                                    loginContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                                    transaction.addToBackStack("telefonKoduGirFragmentEklendi")
                                    transaction.commit()
                                    EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(etGirisSablon.text.toString(), null, null, null, false))

                                }
                            }
                            else{//bu else veri tabanında hiç eleman yokken null'ken geçiş yapmak için yazıldı
                                loginRoot.visibility = View.GONE
                                loginContainer.visibility = View.VISIBLE
                                var transaction = supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                                transaction.addToBackStack("telefonKoduGirFragmentEklendi")
                                transaction.commit()

                            }
                        }


                    })

                } else {
                    Toast.makeText(this, "Lütfen geçerli bir telefon numarası giriniz", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (isValidEmail(etGirisSablon.text.toString())) {

                     var emailKullanimdaMi = false
                    mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(this@RegisterActivity, "Database Hatası", Toast.LENGTH_SHORT).show()

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0!!.getValue() != null) {
                                for (user in p0!!.children) {
                                    var okunanKullanici = user.getValue(Users::class.java)
                                    if (okunanKullanici!!.email!!.equals((etGirisSablon.text.toString()))) {
                                        Toast.makeText(this@RegisterActivity, "Bu email hesabı zaten kullanımda", Toast.LENGTH_SHORT).show()
                                        emailKullanimdaMi = true
                                        break
                                    }

                                }
                                if(emailKullanimdaMi==false) {

                                    loginRoot.visibility = View.GONE
                                    loginContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer, KayitFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                                    transaction.addToBackStack("emailileGirisFragmentEklendi")
                                    transaction.commit()
                                    EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(null, etGirisSablon.text.toString(), null, null, true))

                                }
                            }
                            else{
                                loginRoot.visibility = View.GONE
                                loginContainer.visibility = View.VISIBLE
                                var transaction = supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                                transaction.addToBackStack("telefonKoduGirFragmentEklendi")
                                transaction.commit()

                            }

                        }})
                } else {
                    Toast.makeText(this, "Lütfen geçerli bir email giriniz", Toast.LENGTH_SHORT).show()
                }

            }
        }
        etGirisSablon.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length >= 10) {
                    btnIleri.isEnabled = true
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.beyaz))
                    btnIleri.setBackgroundResource(R.drawable.register_button_aktif)
                } else {
                    btnIleri.isEnabled = false
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.colorAccent))
                    btnIleri.setBackgroundResource(R.drawable.register_button)
                }
            }

        })

    }

    override fun onBackStackChanged() {

        val elemanSayisi = manager.backStackEntryCount
        if (elemanSayisi == 0) {
            loginRoot.visibility = View.VISIBLE
        }
    }

    fun isValidEmail(kontrolEdilecekMail: String): Boolean {
        if (kontrolEdilecekMail == null) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()
    }

    fun isValidTelefon(kontrolEdilecekTelefon: String): Boolean {
        if (kontrolEdilecekTelefon == null || kontrolEdilecekTelefon.length > 14) {
            return false
        }
        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()

    }

    override fun onStart() {
        super.onStart()
//        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        //listenerla direk kullanıcıyı ilgili activity'e gönderiyoruz
        //  if (mAuthListener != null)
        //   mAuth.removeAuthStateListener(mAuthListener)
    }


}
