package com.wanderlust.app.di

import com.wanderlust.data.repositoriesImpl.UserRepositoryImpl
import com.wanderlust.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    fun provideUserRepository(): UserRepository =
        UserRepositoryImpl()
}