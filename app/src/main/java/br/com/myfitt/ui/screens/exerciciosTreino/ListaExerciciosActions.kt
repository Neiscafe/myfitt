package br.com.myfitt.ui.screens.exerciciosTreino

import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.domain.models.Serie

sealed class ListaExerciciosActions {
    data class GetSugestoes(val query: String) : ListaExerciciosActions()
    data class GetAllExercicioTreino(val exercicio: Exercicio) : ListaExerciciosActions()
    class DeleteExercicio(val exercicio: Exercicio) : ListaExerciciosActions()
    class AddAndInsertExercicio(val name: String) : ListaExerciciosActions()
    class AddExercicioTreino(val exercicio: Exercicio) : ListaExerciciosActions()
    class DeleteExercicioTreino(val exercicioTreino: ExercicioTreino) : ListaExerciciosActions()
    class MoveUpExercicioTreino(val exercicioTreino: ExercicioTreino) : ListaExerciciosActions()
    class MoveDownExercicioTreino(val exercicioTreino: ExercicioTreino) : ListaExerciciosActions()
    class DeleteSerie(val serie: Serie) : ListaExerciciosActions()
    class AddSerie(val serie: Serie) : ListaExerciciosActions()
    class UpdateSerie(val serie: Serie) : ListaExerciciosActions()
}