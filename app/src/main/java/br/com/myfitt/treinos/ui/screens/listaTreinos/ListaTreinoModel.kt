package br.com.myfitt.treinos.ui.screens.listaTreinos

import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.common.domain.Treino

data class ListaTreinoModel(val treino: Treino, val tipoExercicios: List<TipoExercicio>)