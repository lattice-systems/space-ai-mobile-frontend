package org.utl.idgs901.space_ai_mobile.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.utl.idgs901.space_ai_mobile.data.map.repository.MapRepositoryImpl
import org.utl.idgs901.space_ai_mobile.domain.map.repository.MapRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapModule {

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mapRepositoryImpl: MapRepositoryImpl
    ): MapRepository
}
