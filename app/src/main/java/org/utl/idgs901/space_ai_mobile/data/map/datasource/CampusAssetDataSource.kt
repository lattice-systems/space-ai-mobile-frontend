package org.utl.idgs901.space_ai_mobile.data.map.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CampusAssetDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readAsset(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
