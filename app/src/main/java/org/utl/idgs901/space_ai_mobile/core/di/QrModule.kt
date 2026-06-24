package org.utl.idgs901.space_ai_mobile.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.utl.idgs901.space_ai_mobile.data.mock.MockQrRepositoryImpl
import org.utl.idgs901.space_ai_mobile.domain.repository.QrRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QrModule {

    @Binds
    @Singleton
    abstract fun bindQrRepository(
        mockQrRepositoryImpl: MockQrRepositoryImpl
    ): QrRepository
}
