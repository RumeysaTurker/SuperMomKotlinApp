package com.rumeysaturker.supermomkotlinapp.Login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.rumeysaturker.supermomkotlinapp.Home.HomeActivity
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import java.util.*


class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    lateinit var manager: FragmentManager

    private var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)
        setContentView(R.layout.activity_register)

        init()

        var btnLoginFacebook = findViewById<Button>(R.id.btnLoginFacebook)
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

        }
    }

    private fun init() {
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

        btnIleri.setOnClickListener {

            if (etGirisSablon.hint.toString().equals("Telefon")) {

                if (isValidTelefon(etGirisSablon.text.toString())) {

                    loginRoot.visibility = View.GONE
                    loginContainer.visibility = View.VISIBLE
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                    transaction.addToBackStack("telefonKoduGirFragmentEklendi")
                    transaction.commit()
                    EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(etGirisSablon.text.toString(), null, null, null, false))

                } else {
                    Toast.makeText(this, "Lütfen geçerli bir telefon numarası giriniz", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (isValidEmail(etGirisSablon.text.toString())) {
                    loginRoot.visibility = View.GONE
                    loginContainer.visibility = View.VISIBLE
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.loginContainer, KayitFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                    transaction.addToBackStack("emailileGirisFragmentEklendi")
                    transaction.commit()
                    EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(null, etGirisSablon.text.toString(), null, null, true))

                } else {
                    Toast.makeText(this, "Lütfen geçerli bir email giriniz", Toast.LENGTH_SHORT).show()
                }

            }
        }
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
        if (kontrolEdilecekTelefon == null || kontrolEdilecekTelefon.length >= 14) {
            return false
        }
        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()

    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
