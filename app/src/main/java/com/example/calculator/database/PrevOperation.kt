package com.example.calculator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "previous_operation_table")
data class PrevOperation(

      @ColumnInfo(name = "input")
      val input: String,

      @ColumnInfo(name = "result")
      val result: String,

      @PrimaryKey(autoGenerate = true) var id: Int = 0)






