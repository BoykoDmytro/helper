package com.bo.helper.di.module

import android.app.Application
import androidx.room.Room
import com.bo.helper.data.entity.database.AppDB
import com.bo.helper.data.entity.database.dao.DiscountDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    companion object {
        private const val DATABASE_FILE = "helper.db"
    }

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDB {
        return Room.databaseBuilder(application, AppDB::class.java, DATABASE_FILE).build()
    }

    @Singleton
    @Provides
    fun provideSessionsDao(database: AppDB): DiscountDao = database.discountDao()
}