package br.com.myfitt.common.domain

data class Exercicio(
    val exercicioId: Int,
    val nome: String,
    val observacao: String? = null,
    val tipoExercicioId: Int? = null,
    val tipoExercicioDescr: String? = null
) {
    override fun toString(): String {
        return this.nome
    }
}