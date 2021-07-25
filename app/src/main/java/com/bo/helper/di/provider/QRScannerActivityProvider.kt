package com.bo.helper.di.provider

import com.bo.helper.presentation.home.fragment.DiscountCreationFragment
import com.bo.helper.presentation.scanner.ScannerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class QRScannerActivityProvider : BaseActivityProvider() {

    @ContributesAndroidInjector
    abstract fun provideScannerFragment(): ScannerFragment

    @ContributesAndroidInjector
    abstract fun provideDiscountCreationFragment(): DiscountCreationFragment
}