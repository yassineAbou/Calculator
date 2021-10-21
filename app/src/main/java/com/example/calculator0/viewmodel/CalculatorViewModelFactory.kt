package com.example.calculator0.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator0.repository.PrevOperationRepository

class CalculatorViewModelFactory(
    private val repository: PrevOperationRepository,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}