package br.com.myfitt.common.domain

import java.time.LocalDateTime

data class Treino(val treinoId: Int, val dhCriado: LocalDateTime = LocalDateTime.now())