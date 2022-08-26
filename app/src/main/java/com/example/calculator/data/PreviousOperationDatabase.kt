package com.example.calculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PreviousOperation::class], version = 2, exportSchema = false)
abstract class PreviousOperationDatabase : RoomDatabase() {

    abstract fun getPreviousOperationDao(): PreviousOperationDao

}








