package com.example.calculator0.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrevOperationDoe {

    @Insert
    suspend fun insert(history: PrevOperation)

    @Query("DELETE FROM previous_operation_table")
    suspend fun clear()

    @Query("SELECT * FROM previous_operation_table ORDER BY id DESC")
    fun getAllPrevOperations(): Flow<List<PrevOperation>>


}