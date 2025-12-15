@file:Suppress("UNCHECKED_CAST")

package br.com.myfitt.common.domain

import br.com.myfitt.log.LogTool

sealed class Resultado<out T> {
    data class Sucesso<T>(val data: T) : Resultado<T>()
    data class Erro(val erro: String?) : Resultado<Nothing>()

    val sucesso = this is Sucesso
    val erroOrNull get() = (this as? Erro)?.erro
    val dataOrNull get() = (this as? Sucesso)?.data
}

suspend fun <T> wrapSuspend(
    labelNullPointer: String? = null,
    block: suspend () -> T
): Resultado<T> {
    val result = runCatching {
        block()
    }
    return if(result.isSuccess){
         Resultado.Sucesso(result.getOrNull()) as Resultado<T>
    }else{
        LogTool.log(result.exceptionOrNull()?.stackTraceToString() ?: "Exception sem stacktrace!")
        Resultado.Erro(result.exceptionOrNull()?.message)
    }
}

inline fun <T> wrap(block: () -> T): Resultado<T> {
    val result = runCatching {
        block()
    }
    return if(result.isSuccess){
        Resultado.Sucesso(result.getOrNull()) as Resultado<T>
    }else{
        LogTool.log(result.exceptionOrNull()?.stackTraceToString() ?: "Exception sem stacktrace!")
        Resultado.Erro(result.exceptionOrNull()?.message)
    }
}

inline fun <T> Resultado<T>.onErro(block: (String) -> Unit): Resultado<T> {
    if (!sucesso) {
        block(this.erroOrNull ?: "Erro desconhecido!")
    }
    return this
}

inline fun <T> Resultado<T>.onSucesso(block: (T) -> Unit): Resultado<T> {
    if (sucesso) {
        block(this.dataOrNull as T)
    }
    return this
}

fun <T, R> Resultado<T>.map(block: (T) -> R = { it as R }): Resultado<R> {
    return (this as? Resultado.Sucesso)?.data?.let {
        Resultado.Sucesso(block(it))
    } ?: run {
        Resultado.Erro((this as? Resultado.Erro)?.erro)
    }
}