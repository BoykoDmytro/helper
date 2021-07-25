package com.bo.helper.di.module

import android.app.Application
import android.content.Context
import com.bo.helper.common.navigation.Router
import com.bo.helper.data.storage.UserSharedPreferences
import com.bo.helper.di.factory.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideUserSharedPreferences(context: Context): UserSharedPreferences =
        UserSharedPreferences(context)


    @Provides
    @Singleton
    fun provideRouter(context: Context): Router = Router(context)
}