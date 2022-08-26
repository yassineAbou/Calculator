package com.example.calculator.repository

import com.example.calculator.data.PreviousOperation
import com.example.calculator.data.PreviousOperationDao
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class PreviousOperationRepository @Inject constructor(
    private val previousOperationDao: PreviousOperationDao
) {

    val listPreviousOperationsFlow = previousOperationDao.getListPreviousOperations().distinctUntilChanged()

     suspend fun insert(previousOperation: PreviousOperation) {
         previousOperationDao.insert(previousOperation)
     }

    suspend fun clear() {
        previousOperationDao.clear()
    }

}