package com.bo.helper.di

import android.app.Application
import android.content.Context
import com.bo.helper.HelperApp
import com.bo.helper.di.module.ActivityModule
import com.bo.helper.di.module.ApplicationModule
import com.bo.helper.di.module.NetworkModule
import com.bo.helper.di.module.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        NetworkModule::class,
        ActivityModule::class,
        RepositoryModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<HelperApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun context(): Context
}