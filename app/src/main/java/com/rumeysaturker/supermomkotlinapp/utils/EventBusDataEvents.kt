package com.rumeysaturker.supermomkotlinapp.utils


import com.rumeysaturker.supermomkotlinapp.models.Gorevler
import com.rumeysaturker.supermomkotlinapp.models.Users

class EventBusDataEvents {

    internal class KayitBilgileriniGonder(var telNo:String?, var email:String?, var verificationID: String?, var code:String?,var emailKayit:Boolean)
     internal class KulaniciBilgileriniGonder(var kullanici: Users?)
    internal class GorevBilgileriniGonder(var gorev: Gorevler?)

    internal class PaylasilacakResmiGonder(var dosyaYolu:String?, var dosyaTuruResimMi:Boolean?)

    internal class GalerySecilenDosyaYolunuGonder(var dosyaYolu:String?)

    internal class KameraIzinBilgisiGonder(var kameraIzniVerildiMi: Boolean?)

    internal class YorumYapilacakGonderininIDsiniGonder(var gonderiID:String?)
    internal class GorevIDsiniGonder(var gorevID:String?)


}
