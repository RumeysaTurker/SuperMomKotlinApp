package com.rumeysaturker.supermomkotlinapp.game

import android.graphics.drawable.Drawable

class Links {
   var link: String?=null
    var image: String?=null
    var id: String?=null
    var baslik:String?=null

    constructor(){

}

    constructor(link: String?, image: String?, id: String?, baslik: String?) {
        this.link = link
        this.image = image
        this.id = id
        this.baslik = baslik
    }


}