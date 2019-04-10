package com.rumeysaturker.supermomkotlinapp.models

class Gorevler {
    var user_id:String? = null
    var gorev_id:String? = null
    var yuklenme_tarih:Long? = null
    var gorev:String? = null
    var yapildi:String?=null
    constructor() {

    }

    constructor(user_id: String?, gorev_id: String?, gorev: String?,yapildi:String?) {
        this.user_id = user_id
        this.gorev_id = gorev_id
      //  this.yuklenme_tarih = yuklenme_tarih
        this.gorev = gorev
        this.yapildi=yapildi
    }

    override fun toString(): String {
        return "Gorevler(user_id=$user_id, gorev_id=$gorev_id, yuklenme_tarih=$yuklenme_tarih, gorev=$gorev,yapildi=$yapildi)"
    }
}