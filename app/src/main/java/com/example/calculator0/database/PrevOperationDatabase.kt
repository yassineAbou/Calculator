package com.example.calculator0.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PrevOperation::class], version = 1, exportSchema = false)
abstract class PrevOperationDatabase : RoomDatabase() {

    abstract fun getPrevOperationDoe(): PrevOperationDoe

}








