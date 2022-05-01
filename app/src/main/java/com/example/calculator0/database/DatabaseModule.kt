package com.example.calculator0.database

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
    fun providePrevOperationDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        PrevOperationDatabase::class.java,
        "calculator_history_database"
    ).build()

    @Singleton
    @Provides
    fun providePrevOperationDoe(prevOperationDatabase: PrevOperationDatabase):
            PrevOperationDoe {
        return prevOperationDatabase.getPrevOperationDoe()
    }


}