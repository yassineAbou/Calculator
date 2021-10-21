package com.example.calculator0.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PrevOperation::class], version = 1, exportSchema = false)
abstract class PrevOperationDatabase : RoomDatabase() {

    abstract val prevOperationDoe: PrevOperationDoe

    companion object {

        @Volatile
        private var INSTANCE: PrevOperationDatabase? = null
        fun getInstance(context: Context): PrevOperationDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PrevOperationDatabase::class.java,
                        "calculator_history_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}








