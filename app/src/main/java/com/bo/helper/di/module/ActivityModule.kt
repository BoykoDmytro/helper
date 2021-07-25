package com.bo.helper.di.module

import com.bo.helper.di.provider.HomeActivityProviders
import com.bo.helper.presentation.home.HomeActivity
import com.bo.helper.presentation.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [HomeActivityProviders::class])
    abstract fun bindHomeActivity(): HomeActivity

}