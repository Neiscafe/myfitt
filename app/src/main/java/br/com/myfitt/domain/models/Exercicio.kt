package br.com.myfitt.domain.models

data class Exercicio(
    val nome: String,
    val id: Int = 0,
    val posicao: Int = 0,
    val tipo: TipoExercicio? = null,
    val habilitado: Boolean = true,
    val dataDesabilitado: String? = null
) {
    override fun toString(): String {
        return nome.lowercase().replaceFirstChar { it.uppercase() }
    }
}