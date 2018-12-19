package com.rumeysaturker.supermomkotlinapp.Login


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import com.rumeysaturker.supermomkotlinapp.R
import com.rumeysaturker.supermomkotlinapp.utils.EventBusDataEvents
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.*
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit


class TelefonKoduGirFragment : Fragment() {
    var gelenTelNo = ""
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks//lateinit sonradan atama yapılacağı için
    var verificationID = ""
    var gelenKod = ""
    lateinit var ccp: CountryCodePicker
    lateinit var fbAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_telefon_kodu_gir, container, false)
        val view2 = inflater.inflate(R.layout.activity_register, container, false)
        view.etOnayKodu.addTextChangedListener(watcher)
        ccp = view2.findViewById<CountryCodePicker>(R.id.ccp)
        ccp!!.registerCarrierNumberEditText(view2.findViewById<EditText>(R.id.etGirisSablon));
        fbAuth = FirebaseAuth.getInstance();

        view.tvKullaniciTelNo.setText(gelenTelNo)
        progressBar=view.pbOnayKodu

        setupCallBack()

        view.btnTelKodIleri.setOnClickListener {

            if (gelenKod.equals(view.etOnayKodu.text.toString())) {
                EventBus.getDefault().postSticky(EventBusDataEvents.KayitBilgileriniGonder(gelenTelNo, null, verificationID, gelenKod, false))
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, KayitFragment())//loginContainer'a TelefonKoduGirFragment'ini yerleştir
                transaction.addToBackStack("kayitFragmentEklendi")
                transaction.commit()

            } else {
                Toast.makeText(activity, "Kod Hatalı", Toast.LENGTH_SHORT).show()
            }
        }

        Log.e("rümeysa", "Uzun tel no:" + ccp.fullNumberWithPlus + gelenTelNo)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ccp.fullNumberWithPlus.toString() + gelenTelNo.toString(),      // Phone number to verify
                60,               // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this.activity!!,             // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks
        return view
    }

    private fun setupCallBack() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if (!credential.smsCode.isNullOrEmpty()) {
                    gelenKod = credential.smsCode!!
                    progressBar.visibility=View.INVISIBLE
                    Log.e("HATA", "on verification completed sms gelmiş" + gelenKod)
                } else {
                    Log.e("HATA", "on verification completed sms gelmeyecek")
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Log.e("Hata", "Hata Çıktı" + e.message)
                progressBar.visibility=View.INVISIBLE
            }

            override fun onCodeSent(
                    verificationId: String?,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                verificationID = verificationId!!
                progressBar.visibility=View.VISIBLE
                Log.e("HATA", "oncodesent çalıştı")

            }
        }
    }

    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length > 5) {//sifre vs. 6 karakterden az olamayacağı için bu kontrolü yapıyoruz
                if (etOnayKodu.text.toString().length > 5) {
                    btnTelKodIleri.isEnabled = true
                    btnTelKodIleri.setTextColor((ContextCompat.getColor(activity!!, R.color.beyaz)))
                    btnTelKodIleri.setBackgroundResource(R.drawable.register_button_aktif)

                } else {
                    btnTelKodIleri.isEnabled = false//6 karakterden azsa butonu aktifleştirme!
                    btnTelKodIleri.setTextColor((ContextCompat.getColor(activity!!, R.color.colorAccent)))
                    btnTelKodIleri.setBackgroundResource(R.drawable.register_button)

                }
            } else {
                btnTelKodIleri.isEnabled = false//6 karakterden azsa butonu aktifleştirme!
                btnTelKodIleri.setTextColor((ContextCompat.getColor(activity!!, R.color.colorAccent)))
                btnTelKodIleri.setBackgroundResource(R.drawable.register_button)
            }
        }

    }


    @Subscribe(sticky = true)
    internal fun onTelefonNoEvent(kayitBilgileri: EventBusDataEvents.KayitBilgileriniGonder) {

        gelenTelNo = kayitBilgileri.telNo!!
        Log.e("rümeysa", "Gelen tel no:" + gelenTelNo)
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
