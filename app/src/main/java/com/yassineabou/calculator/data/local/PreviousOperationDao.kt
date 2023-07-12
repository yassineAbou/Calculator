package com.yassineabou.calculator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yassineabou.calculator.data.model.PreviousOperation
import kotlinx.coroutines.flow.Flow

@Dao
interface PreviousOperationDao {

    @Insert
    suspend fun insert(previousOperation: PreviousOperation)

    @Query("DELETE FROM previous_operation")
    suspend fun clear()

    @Query("SELECT * FROM previous_operation ORDER BY id DESC")
    fun getListPreviousOperations(): Flow<List<PreviousOperation>>
}
