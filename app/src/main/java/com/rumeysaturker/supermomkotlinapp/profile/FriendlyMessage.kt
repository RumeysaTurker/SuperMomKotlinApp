package com.rumeysaturker.supermomkotlinapp.profile

class FriendlyMessage (
    val text:String?,
    val name:String?,
    val photoUrl:String?
)
{
    constructor(): this(null,null,null)
}