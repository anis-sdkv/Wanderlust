package com.wanderlust.app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wanderlust.data.repositoriesImpl.CurrentUserRepositoryImpl
import com.wanderlust.data.repositoriesImpl.PlaceRepositoryImpl
import com.wanderlust.data.repositoriesImpl.RouteRepositoryImpl
import com.wanderlust.data.repositoriesImpl.SettingsRepositoryImpl
import com.wanderlust.data.repositoriesImpl.UserRepositoryImpl
import com.wanderlust.data.services.GeocoderServiceImpl
import com.wanderlust.data.sources.local.sharedpref.AppPreferences
import com.wanderlust.domain.repositories.CurrentUserRepository
import com.wanderlust.domain.repositories.PlaceRepository
import com.wanderlust.domain.repositories.RouteRepository
import com.wanderlust.domain.repositories.SettingsRepository
import com.wanderlust.domain.repositories.UserRepository
import com.wanderlust.domain.services.GeocoderService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


//@Module
//@InstallIn(ActivityComponent::class)
//abstract class SettingsModule {
//}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindCurrentUserRepository(impl: CurrentUserRepositoryImpl): CurrentUserRepository

    @Binds
    abstract fun bindRouteRepositoryImpl(impl: RouteRepositoryImpl): RouteRepository

    @Binds
    abstract fun bindPlaceRepositoryImpl(impl: PlaceRepositoryImpl): PlaceRepository
    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}

@Module
@InstallIn(SingletonComponent::class)
class DataProvideModule {
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideAppPreferences(@ApplicationContext appContext: Context): AppPreferences = AppPreferences(appContext)

    @Provides
    fun provideGeocoderService(@ApplicationContext appContext: Context): GeocoderService =
        GeocoderServiceImpl(appContext)
}