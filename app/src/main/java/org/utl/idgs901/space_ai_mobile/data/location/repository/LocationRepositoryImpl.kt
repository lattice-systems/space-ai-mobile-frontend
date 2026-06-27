package org.utl.idgs901.space_ai_mobile.data.location.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.data.location.datasource.FusedLocationDataSource
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocation
import org.utl.idgs901.space_ai_mobile.domain.location.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataSource: FusedLocationDataSource
) : LocationRepository {

    override fun observeLocation(): Flow<CampusLocation> = dataSource.observeLocation()

    override suspend fun getCurrentLocation(): CampusLocation? = dataSource.getCurrentLocation()

    override fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        return fineLocation || coarseLocation
    }
}
