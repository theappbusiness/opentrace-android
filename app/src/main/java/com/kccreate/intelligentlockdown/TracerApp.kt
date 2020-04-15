package com.kccreate.intelligentlockdown

import android.app.Application
import android.content.Context
import android.os.Build
import com.kccreate.intelligentlockdown.idmanager.TempIDManager
import com.kccreate.intelligentlockdown.logging.CentralLog
import com.kccreate.intelligentlockdown.services.BluetoothMonitoringService
import com.kccreate.intelligentlockdown.streetpass.CentralDevice
import com.kccreate.intelligentlockdown.streetpass.PeripheralDevice

class TracerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TracerApp.Companion.AppContext = applicationContext
    }

    companion object {

        private val TAG = "TracerApp"
        const val ORG = BuildConfig.ORG

        lateinit var AppContext: Context

        fun thisDeviceMsg(): String {
            BluetoothMonitoringService.broadcastMessage?.let {
                CentralLog.i(TracerApp.Companion.TAG, "Retrieved BM for storage: $it")

                if (!it.isValidForCurrentTime()) {

                    var fetch = TempIDManager.retrieveTemporaryID(TracerApp.Companion.AppContext)
                    fetch?.let {
                        CentralLog.i(TracerApp.Companion.TAG, "Grab New Temp ID")
                        BluetoothMonitoringService.broadcastMessage = it
                    }

                    if (fetch == null) {
                        CentralLog.e(TracerApp.Companion.TAG, "Failed to grab new Temp ID")
                    }

                }
            }
            return BluetoothMonitoringService.broadcastMessage?.tempID ?: "Missing TempID"
        }

        fun asPeripheralDevice(): PeripheralDevice {
            return PeripheralDevice(Build.MODEL, "SELF")
        }

        fun asCentralDevice(): CentralDevice {
            return CentralDevice(Build.MODEL, "SELF")
        }
    }
}
