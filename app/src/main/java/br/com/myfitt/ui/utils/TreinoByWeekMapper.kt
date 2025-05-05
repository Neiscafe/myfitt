package br.com.myfitt.ui.utils

import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.utils.DateUtil
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class TreinoByWeekMapper(val treinos: List<Treino>) {
    fun getList(): List<Pair<LocalDate, List<Treino>>> {
        val weekFields = WeekFields.of(Locale.getDefault())
        return treinos.groupBy {
            val date = DateUtil.fromDbNotation(it.data)
            date.with(
                weekFields.dayOfWeek(), DayOfWeek.MONDAY.value.toLong()
            )
        }.toList()
    }
}