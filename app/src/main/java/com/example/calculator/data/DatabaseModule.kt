package com.example.calculator.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providePreviousOperationDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        PreviousOperationDatabase::class.java,
        "calculator_history_database"
    ).createFromAsset("database/previous_operation.db").fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun providePreviousOperationDao(previousOperationDatabase: PreviousOperationDatabase):
            PreviousOperationDao {
        return previousOperationDatabase.getPreviousOperationDao()
    }


}