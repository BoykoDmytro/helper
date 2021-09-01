package com.bo.helper.di.module

import androidx.lifecycle.ViewModel
import com.bo.helper.di.key.ViewModelKey
import com.bo.helper.presentation.home.fragment.DiscountListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DiscountListViewModel::class)
    abstract fun bindDiscountListViewModel(viewModel: DiscountListViewModel): ViewModel

}