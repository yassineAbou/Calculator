package com.example.calculator.repository

import com.example.calculator.database.PrevOperation
import com.example.calculator.database.PrevOperationDoe
import javax.inject.Inject

class PrevOperationRepository @Inject constructor(private val
    prevOperationDoe: PrevOperationDoe) {

    val listPrevOperations = prevOperationDoe.getListPrevOperations()


     suspend fun insert(prevOperation: PrevOperation) {
         prevOperationDoe.insert(prevOperation)
     }

    suspend fun clear() {
        prevOperationDoe.clear()
    }

}