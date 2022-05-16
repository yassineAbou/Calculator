package com.example.calculator0.repository

import com.example.calculator0.database.PrevOperation
import com.example.calculator0.database.PrevOperationDoe
import javax.inject.Inject

class PrevOperationRepository @Inject constructor(private val
    prevOperationDoe: PrevOperationDoe) {

    val allPrevOperations = prevOperationDoe.getAllPrevOperations()


     suspend fun insert(prevOperation: PrevOperation) {
         prevOperationDoe.insert(prevOperation)
     }

    suspend fun clear() {
        prevOperationDoe.clear()
    }

}