package br.com.myfitt.ui.utils

import br.com.myfitt.domain.models.Exercicio

fun Exercicio.toUserFriendlyName(): String {
    val builder = StringBuilder().append(nome)
    if (tipo != null) builder.append(" ${tipo.nome}")
    return builder.toString()
}