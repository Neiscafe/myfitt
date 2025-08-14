package br.com.myfitt.domain.models

data class TreinoExercicioComNome(
    val id: Int,
    val treinoId: Int,
    val exercicioNome: String,
    val exercicioId: Int = 0,
    val series: Int = 1,
    val posicao: Int = 0,
    val pesoKg: Float = 0f,
    val serieId: Int = 0,
    val segundosDescanso: Int = 0,
    val repeticoes: Int = 0,
    val observacao: String? = null,
    val pesoKgUltimoTreino: Float = 0f,
    val repeticoesUltimoTreino: Int = 0,
    val seriesUltimoTreino: Int = 0,
    val seriesLista: List<Serie>
){
}