package br.com.myfitt.treinos.ui.screens.seriesExercicio

data class CronometroState(
    val descansoAtivo: Boolean = false,
    val serieAtiva: Boolean = false,
    val segundosDescanso: Int = 0,
    val segundosSerie: Int = 0,
){
    val cronometroServiceAtivo = descansoAtivo && serieAtiva
}