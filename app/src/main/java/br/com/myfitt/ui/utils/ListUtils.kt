package br.com.myfitt.ui.utils

fun <T> List<T>.toNullableSpinnerList(): List<T?>{
    return this.toMutableList<T?>().also {
        it.add(0, null)
    }
}