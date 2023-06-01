package com.wanderlust.app.di

import com.wanderlust.data.services.UserService
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.RouteRepository
import com.wanderlust.domain.repositories.UserRepository
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.GetRoutesByIdListUseCase
import com.wanderlust.domain.usecases.GetUserByName
import com.wanderlust.domain.usecases.LoginUseCase
import com.wanderlust.domain.usecases.RegisterUseCase
import com.wanderlust.domain.usecases.SetCurrentUserUseCase
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
    fun provideRegisterUseCase(userService: UserService): RegisterUseCase =
        RegisterUseCase(userService)
    @Provides
    fun provideLoginUseCase(userService: UserService): LoginUseCase =
        LoginUseCase(userService)

    @Provides
    fun provideGetRoutesByIdListUseCase(routeRepository: RouteRepository): GetRoutesByIdListUseCase =
        GetRoutesByIdListUseCase(routeRepository)

    @Provides
    fun provideGetCurrentUserUseCase(currentUserRepository: CurrentUserRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(currentUserRepository)

    @Provides
    fun provideSetCurrentUserUseCase(currentUserRepository: CurrentUserRepository): SetCurrentUserUseCase =
        SetCurrentUserUseCase(currentUserRepository)

}