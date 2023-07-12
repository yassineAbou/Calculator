package com.yassineabou.calculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yassineabou.calculator.data.model.PreviousOperation

@Database(entities = [PreviousOperation::class], version = 1, exportSchema = false)
abstract class PreviousOperationDatabase : RoomDatabase() {

    abstract fun getPreviousOperationDao(): PreviousOperationDao
}
