package org.utl.idgs901.space_ai_mobile.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.utl.idgs901.space_ai_mobile.core.datastore.PreferencesSerializer
import org.utl.idgs901.space_ai_mobile.core.datastore.UserPreferencesProto
import org.utl.idgs901.space_ai_mobile.domain.model.UserPreferences
import org.utl.idgs901.space_ai_mobile.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

private val Context.preferencesDataStore: DataStore<UserPreferencesProto> by dataStore(
    fileName = "user_preferences.pb",
    serializer = PreferencesSerializer
)

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    override val userPreferences: Flow<UserPreferences> = context.preferencesDataStore.data
        .map { proto ->
            UserPreferences(
                reduceAnimations = proto.reduceAnimations,
                highContrast = proto.highContrast,
                fontSize = if (proto.fontSize.isEmpty()) "Normal" else proto.fontSize,
                screenReaderEnabled = proto.screenReaderEnabled,
                hapticFeedbackEnabled = proto.hapticFeedbackEnabled,
                advancedAccessibilityMode = proto.advancedAccessibilityMode
            )
        }

    override suspend fun updateReduceAnimations(enabled: Boolean) {
        context.preferencesDataStore.updateData { it.toBuilder().setReduceAnimations(enabled).build() }
    }

    override suspend fun updateHighContrast(enabled: Boolean) {
        context.preferencesDataStore.updateData { it.toBuilder().setHighContrast(enabled).build() }
    }

    override suspend fun updateFontSize(size: String) {
        context.preferencesDataStore.updateData { it.toBuilder().setFontSize(size).build() }
    }

    override suspend fun updateScreenReader(enabled: Boolean) {
        context.preferencesDataStore.updateData { it.toBuilder().setScreenReaderEnabled(enabled).build() }
    }

    override suspend fun updateHapticFeedback(enabled: Boolean) {
        context.preferencesDataStore.updateData { it.toBuilder().setHapticFeedbackEnabled(enabled).build() }
    }

    override suspend fun updateAdvancedAccessibility(enabled: Boolean) {
        context.preferencesDataStore.updateData { it.toBuilder().setAdvancedAccessibilityMode(enabled).build() }
    }
}
