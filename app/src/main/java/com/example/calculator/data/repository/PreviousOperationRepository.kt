package com.example.calculator.data.repository

import com.example.calculator.data.local.PreviousOperationDao
import com.example.calculator.data.model.PreviousOperation
import javax.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged

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
