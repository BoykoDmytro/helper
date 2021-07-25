package com.bo.helper.di.provider

import com.bo.helper.presentation.home.fragment.DiscountListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeActivityProviders : BaseActivityProvider() {

    @ContributesAndroidInjector
    abstract fun provideDiscountListFragment(): DiscountListFragment
}