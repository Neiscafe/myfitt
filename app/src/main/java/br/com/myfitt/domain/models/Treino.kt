package br.com.myfitt.domain.models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Treino(
    val nome: String,
    val id: Int = 0,
    val data: String = LocalDate.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    ),
)