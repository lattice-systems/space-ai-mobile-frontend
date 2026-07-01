package org.utl.idgs901.space_ai_mobile.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.utl.idgs901.space_ai_mobile.data.repository.MockAuthRepositoryImpl
import org.utl.idgs901.space_ai_mobile.data.settings.repository.SettingsRepositoryImpl
import org.utl.idgs901.space_ai_mobile.data.repository.UserRepositoryImpl
import org.utl.idgs901.space_ai_mobile.domain.repository.AuthRepository
import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import org.utl.idgs901.space_ai_mobile.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        mockAuthRepositoryImpl: MockAuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
