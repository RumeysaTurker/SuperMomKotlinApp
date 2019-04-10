package com.rumeysaturker.supermomkotlinapp.models


sealed class Action

var counter = 0L
data class AddTodo(val text: String, val id : Long = counter++) : Action()
data class ToggleTodo(val id: Long) : Action()
data class SetVisibility(val visibility: Visibility) : Action()
data class RemoveTodo(val id: Long) : Action()

sealed class Visibility{
    class Hepsi : Visibility()
    class Yapılacaklar : Visibility()
    class Tamamlanmış: Visibility()
}
