package com.yassineabou.calculator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "previous_operation")
data class PreviousOperation(

    val input: String,

    val result: String,

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
)
