package dev.iconpln.mims.data.local.databasereport

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.*

class ReportUploader : Service() {
    private lateinit var timer: Timer
    private lateinit var binder: IBinder
    private lateinit var handler: Handler
    internal var flag: Boolean = false

    private val updateTask = object : TimerTask() {
        override fun run() {
            if (!sending && isInternetConnection)
                sendReport()
            Log.i("ReportUploader.kt", "SendQueue: selesai kirim data")
        }
    }

    internal fun sendReport() {
        sending = true
        val databaseReport = DatabaseReport.getDatabase(applicationContext)
        val toBeSent = databaseReport.reportToBeSent

        for (report in toBeSent) {
            if (report.sendReport()) {
                databaseReport.doneReport(report, 1)
//                report.parameterList
//                        .filter { it.tipeParameter == ReportParameter.FILE }
//                        .map { File(it.valueParameter) }
//                        .filter { it.exists() }
//                        .forEach {
//                            try {
//                                it.delete()
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                        }
                sendResult()
            }
        }
        sending = false
    }

    private fun sendResult() {
//        if (!TransmissionActivity.isLoading) {
//            val intent = Intent(Intent.ACTION_SEND)
//            sendBroadcast(intent)
//            Log.i("ReportUploader.kt", "Send result reciever")
//        }
    }

    private lateinit var broadcaster: LocalBroadcastManager

    override fun onCreate() {
        handler = Handler()
        timer = Timer()
        timer.schedule(updateTask, 0L, 60 * 1000L)
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    val isInternetConnection: Boolean
        get() {
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (connectivityManager.activeNetworkInfo == null) false else connectivityManager!!.activeNetworkInfo!!.isConnected
        }

    inner class LocalBinder : Binder() {
        val serverInstance: ReportUploader
            get() = this@ReportUploader
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return true
    }

    /** Called when a client is binding to the service with bindService().  */
    override fun onRebind(intent: Intent) {
    }

    override fun onDestroy() {
        timer.cancel()
    }

    companion object {
        internal var sending: Boolean = false
    }
}
