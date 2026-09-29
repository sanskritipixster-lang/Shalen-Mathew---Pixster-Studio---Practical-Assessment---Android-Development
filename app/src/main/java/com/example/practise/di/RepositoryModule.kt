package com.example.practise.di

import com.example.practise.data.repository.CurrencyConversionRepositoryImpl
import com.example.practise.domain.repository.CurrencyConversionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCurrencyConversionRepository(
        implementation: CurrencyConversionRepositoryImpl
    ): CurrencyConversionRepository
}
