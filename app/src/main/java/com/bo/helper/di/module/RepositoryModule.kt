package com.bo.helper.di.module

import com.bo.helper.data.repository.DiscountRepositoryImpl
import com.bo.helper.domain.repository.IDiscountRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDiscountRepository(repository: DiscountRepositoryImpl): IDiscountRepository

}