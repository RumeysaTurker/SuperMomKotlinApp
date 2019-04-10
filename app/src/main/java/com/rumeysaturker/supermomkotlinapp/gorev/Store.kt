package com.rumeysaturker.supermomkotlinapp.gorev

import android.arch.core.util.Function
import android.opengl.GLSurfaceView
import com.algolia.instantsearch.helpers.Highlighter

import com.rumeysaturker.supermomkotlinapp.models.Action


interface Store<T> {
    fun dispatch(action: Action)

    fun subscribe(renderer: Renderer<T>, func: Function<T, T> = Function { it })
}