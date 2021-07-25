package com.bo.helper.di.factory

import androidx.lifecycle.ViewModelProvider
import com.bo.helper.di.module.ActivityViewModelModule
import com.bo.helper.di.module.HomeViewModelModule
import com.bo.helper.di.module.ScanViewModelModule
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        ActivityViewModelModule::class,
        HomeViewModelModule::class,
        ScanViewModelModule::class
    ]
)
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}