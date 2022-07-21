package com.example.calculator.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PrevOperation::class], version = 1, exportSchema = false)
abstract class PrevOperationDatabase : RoomDatabase() {

    abstract fun getPrevOperationDoe(): PrevOperationDoe

}








