package br.com.myfitt.ui.components

sealed class Loadable<T>{
    data class Loaded<T>(val data: T): Loadable<T>()
    data object Loading: Loadable<Nothing>()
}