package br.com.myfitt.data.database

import androidx.room.DeleteTable
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

@RenameTable(fromTableName = "rotina", toTableName = "divisao")
@DeleteTable("rotina_exercicio")
class Specs_4_5: AutoMigrationSpec