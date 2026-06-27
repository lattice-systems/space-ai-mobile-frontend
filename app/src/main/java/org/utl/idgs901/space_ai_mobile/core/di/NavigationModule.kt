package org.utl.idgs901.space_ai_mobile.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.utl.idgs901.space_ai_mobile.data.navigation.repository.NavigationRepositoryImpl
import org.utl.idgs901.space_ai_mobile.domain.navigation.repository.NavigationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigationRepository(
        navigationRepositoryImpl: NavigationRepositoryImpl
    ): NavigationRepository
}
