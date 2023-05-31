package com.wanderlust.app.di

import com.wanderlust.data.services.UserService
import com.wanderlust.domain.repositories.UserRepository
import com.wanderlust.domain.usecases.GetUserByName
import com.wanderlust.domain.usecases.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetUserByNameUseCase(userRepository: UserRepository): GetUserByName =
        GetUserByName(userRepository)

    @Provides
    fun provideRegisterUserUseCase(userService: UserService): RegisterUserUseCase =
        RegisterUserUseCase(userService)
}