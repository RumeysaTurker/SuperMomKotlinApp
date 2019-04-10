package com.rumeysaturker.supermomkotlinapp.gorev

import android.arch.core.util.Function
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.rumeysaturker.supermomkotlinapp.models.*

class GorevStore: Store<GorevModel>, ViewModel() {

    private val state: MutableLiveData<GorevModel> = MutableLiveData()

    private val initState = GorevModel(listOf(), Visibility.Hepsi())


    override fun dispatch(action: Action) {
        state.value = reduce(state.value, action)
    }

    private fun reduce(state: GorevModel?, action: Action): GorevModel {
        val newState = state ?: initState

        return when(action) {

            is AddTodo -> newState.apply{
               todos = newState.todos.toMutableList().apply {
                    add(Gorev(action.text, action.id))
                }
            }
            is ToggleTodo -> newState.apply {
            todos = newState.todos.map {
                if (it.id == action.id) {
                    it.copy(status = !it.status)
                } else it
            } as MutableList<Gorev>
        }
            is SetVisibility -> newState.apply {
                 visibility = action.visibility
            }
            is RemoveTodo -> newState.apply {
                  todos = newState.todos.filter {
                        it.id != action.id
                    } as MutableList<Gorev>
            }
        }
    }

    override fun subscribe(renderer: Renderer<GorevModel>, func: Function<GorevModel, GorevModel>) {
        renderer.render(Transformations.map(state, func))
    }


}