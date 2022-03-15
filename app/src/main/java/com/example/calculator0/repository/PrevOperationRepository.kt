package com.example.calculator0.repository

import androidx.lifecycle.LiveData
import com.example.calculator0.database.PrevOperation
import com.example.calculator0.database.PrevOperationDoe

class PrevOperationRepository(private val prevOperationDoe: PrevOperationDoe) {

    val allPrevOperations = prevOperationDoe.getAllPrevOperations()


     suspend fun insert(prevOperation: PrevOperation) {
         prevOperationDoe.insert(prevOperation)
     }

    suspend fun clear() {
        prevOperationDoe.clear()
    }

}