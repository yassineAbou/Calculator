package com.example.calculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator.data.model.PreviousOperation

@Database(entities = [PreviousOperation::class], version = 1, exportSchema = false)
abstract class PreviousOperationDatabase : RoomDatabase() {

    abstract fun getPreviousOperationDao(): PreviousOperationDao
}
