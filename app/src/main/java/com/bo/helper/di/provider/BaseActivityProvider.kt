package com.bo.helper.di.provider

import com.bo.helper.presentation.base.dialog.ProgressDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BaseActivityProvider {

    @ContributesAndroidInjector
    abstract fun provideProgressDialog(): ProgressDialog
}