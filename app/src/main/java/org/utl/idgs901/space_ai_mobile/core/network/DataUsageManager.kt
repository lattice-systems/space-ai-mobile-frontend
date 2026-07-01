package org.utl.idgs901.space_ai_mobile.core.network

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import java.util.Calendar

@Singleton
class DataUsageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class UsageStats(
        val wifiBytes: Long,
        val mobileBytes: Long
    )

    fun getUsageStats(): UsageStats {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return UsageStats(0, 0)
        }

        val networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        return try {
            val wifiStats = networkStatsManager.querySummaryForDevice(
                ConnectivityManager.TYPE_WIFI,
                null,
                startTime,
                endTime
            )
            val mobileStats = networkStatsManager.querySummaryForDevice(
                ConnectivityManager.TYPE_MOBILE,
                null,
                startTime,
                endTime
            )

            UsageStats(
                wifiBytes = wifiStats.rxBytes + wifiStats.txBytes,
                mobileBytes = mobileStats.rxBytes + mobileStats.txBytes
            )
        } catch (e: Exception) {
            UsageStats(0, 0)
        }
    }
}
