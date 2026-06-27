package org.utl.idgs901.space_ai_mobile.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationRendererModule {
    // Singletons are automatically provided by Hilt via @Inject constructor and @Singleton
}
