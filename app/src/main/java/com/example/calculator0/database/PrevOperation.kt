package com.example.calculator0.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "previous_operation_table")
data class PrevOperation(

      @ColumnInfo(name = "previous_input")
      val previousInput: String,

      @ColumnInfo(name = "previous_output")
      val previousOutput: String,

      @PrimaryKey(autoGenerate = true) var id: Int = 0)






