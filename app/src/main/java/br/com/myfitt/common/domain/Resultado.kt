@file:Suppress("UNCHECKED_CAST")

package br.com.myfitt.common.domain

sealed class Resultado<out T> {
    data class Sucesso<T>(val data: T) : Resultado<T>()
    data class Erro<T>(val erro: String?) : Resultado<T>()

    val sucesso = this is Sucesso
}

fun <T, R> Resultado<T>.map(block: (T) -> R = { it as R }): Resultado<R> {
    return (this as? Resultado.Sucesso)?.data?.let {
        Resultado.Sucesso(block(it))
    } ?: run {
        Resultado.Erro((this as? Resultado.Erro)?.erro)
    }
}