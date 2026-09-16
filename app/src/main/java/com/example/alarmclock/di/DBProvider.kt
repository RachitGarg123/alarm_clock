package com.example.alarmclock.di

import android.content.Context
import androidx.room.Room
import com.example.alarmclock.alarmset.data.db.AlarmDao
import com.example.alarmclock.alarmset.data.db.AlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBProvider {

    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext appContext: Context
    ): AlarmDatabase {
        return Room.databaseBuilder(
            appContext,
            AlarmDatabase::class.java,
            "alarm_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesDao(database: AlarmDatabase): AlarmDao {
        return database.alarmDao()
    }

}