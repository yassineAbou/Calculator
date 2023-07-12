package com.yassineabou.calculator.data.repository

import com.yassineabou.calculator.data.local.PreviousOperationDao
import com.yassineabou.calculator.data.model.PreviousOperation
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class PreviousOperationRepository @Inject constructor(
    private val previousOperationDao: PreviousOperationDao,
) {

    val listPreviousOperationsFlow =
        previousOperationDao.getListPreviousOperations().distinctUntilChanged()

    suspend fun insert(previousOperation: PreviousOperation) {
        previousOperationDao.insert(previousOperation)
    }

    suspend fun clear() {
        previousOperationDao.clear()
    }
}
