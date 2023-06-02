package com.wanderlust.app.di

import com.wanderlust.data.services.UserServiceImpl
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.PlaceRepository
import com.wanderlust.domain.repositories.RouteRepository
import com.wanderlust.domain.repositories.SettingsRepository
import com.wanderlust.domain.repositories.UserRepository
import com.wanderlust.domain.services.GeocoderService
import com.wanderlust.domain.usecases.CreatePlaceUseCase
import com.wanderlust.domain.usecases.CreateRouteUseCase
import com.wanderlust.domain.usecases.GetCurrentUserIdUseCase
import com.wanderlust.domain.usecases.GetCurrentUserUseCase
import com.wanderlust.domain.usecases.GetLocationByCoordinatesUseCase
import com.wanderlust.domain.usecases.GetRoutesByIdListUseCase
import com.wanderlust.domain.usecases.GetUserByName
import com.wanderlust.domain.usecases.LoginUseCase
import com.wanderlust.domain.usecases.RegisterUseCase
import com.wanderlust.domain.usecases.SetCurrentUserUseCase
import com.wanderlust.domain.usecases.SignOutUseCase
import com.wanderlust.domain.usecases.UpdateUserUseCase
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
    fun provideRegisterUseCase(userService: UserServiceImpl): RegisterUseCase =
        RegisterUseCase(userService)

    @Provides
    fun provideLoginUseCase(userService: UserServiceImpl): LoginUseCase =
        LoginUseCase(userService)

    @Provides
    fun provideGetRoutesByIdListUseCase(routeRepository: RouteRepository): GetRoutesByIdListUseCase =
        GetRoutesByIdListUseCase(routeRepository)

    @Provides
    fun provideGetCurrentUserUseCase(currentUserRepository: CurrentUserRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(currentUserRepository)

    @Provides
    fun provideGetCurrentUserIdUseCase(currentUserRepository: CurrentUserRepository): GetCurrentUserIdUseCase =
        GetCurrentUserIdUseCase(currentUserRepository)

    @Provides
    fun provideSetCurrentUserUseCase(currentUserRepository: CurrentUserRepository): SetCurrentUserUseCase =
        SetCurrentUserUseCase(currentUserRepository)

    @Provides
    fun provideSignOutUseCase(currentUserRepository: CurrentUserRepository): SignOutUseCase =
        SignOutUseCase(currentUserRepository)

    @Provides
    fun provideUpdateUserUseCase(
        userRepository: UserRepository,
        currentUserRepository: CurrentUserRepository
    ): UpdateUserUseCase =
        UpdateUserUseCase(userRepository, currentUserRepository)

    @Provides
    fun provideCreatePlaceUseCase(
        placeRepository: PlaceRepository,
        currentUserRepository: CurrentUserRepository,
        getLocationByCoordinatesUseCase: GetLocationByCoordinatesUseCase
    ) =
        CreatePlaceUseCase(
            placeRepository,
            currentUserRepository,
            getLocationByCoordinatesUseCase
        )


    @Provides
    fun provideCreateRouteUseCase(
        routeRepository: RouteRepository,
        currentUserRepository: CurrentUserRepository,
        getLocationByCoordinatesUseCase: GetLocationByCoordinatesUseCase
    ) =
        CreateRouteUseCase(
            routeRepository,
            currentUserRepository,
            getLocationByCoordinatesUseCase
        )

    @Provides
    fun provideGetLocationByCoordinatesUseCase(
        geocoderService: GeocoderService,
        settingsRepository: SettingsRepository
    ): GetLocationByCoordinatesUseCase = GetLocationByCoordinatesUseCase(geocoderService, settingsRepository)
}