package br.com.myfitt.treinos.domain.model

import br.com.myfitt.common.domain.TipoExercicio

data class TipoExercicioTreino(val treinoId: Int, val tipoExercicios: List<TipoExercicio>)