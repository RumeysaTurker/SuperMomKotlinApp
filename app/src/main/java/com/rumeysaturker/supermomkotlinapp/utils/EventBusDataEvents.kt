package com.rumeysaturker.supermomkotlinapp.utils

import com.rumeysaturker.supermomkotlinapp.Models.Users

class EventBusDataEvents {

    internal class KayitBilgileriniGonder(var telNo:String?, var email:String?, var verificationID: String?, var code:String?,var emailKayit:Boolean)
     internal class KulaniciBilgileriniGonder(var kullanici: Users?)
}
