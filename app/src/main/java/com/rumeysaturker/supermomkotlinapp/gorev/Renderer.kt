package com.rumeysaturker.supermomkotlinapp.gorev
import android.arch.lifecycle.LiveData
interface Renderer<T> {
    fun render(model: LiveData<T>)
}