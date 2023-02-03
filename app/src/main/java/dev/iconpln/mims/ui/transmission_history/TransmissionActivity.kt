package dev.iconpln.mims.ui.transmission_history

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database_local.DatabaseReport
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.utils.DataController
import java.util.*

class TransmissionActivity : AppCompatActivity() {

    private lateinit var reciever: MyBroadcastReciever
    private lateinit var toSend: Array<String>
    private lateinit var sent: Array<String>

    private var adapterTerkirim: ArrayAdapter<String>? = null
    private var adapterBelumTerkirim: ArrayAdapter<String>? = null
    private var databaseReport: DatabaseReport? = null
    private var listViewtoBeSent: ListView? = null
    private var listViewSent: ListView? = null
    private var switchOn = false
    private var sending = false

    private lateinit var btn : Button

    private lateinit var daoSession: DaoSession

    //region Privilege Variables
    private lateinit var mPrivileges: Map<String, String>
    private var mPageTitle: String = ""
    private var mEmailSupport: String = ""
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transmission_history)
        initData()
        init()
    }

    private fun initData() {
        daoSession = (application as MyApplication).daoSession!!
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Intent.ACTION_SEND)
        reciever = MyBroadcastReciever(this)
        registerReceiver(reciever, filter)
        update()
    }

    private fun init() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        listViewSent = findViewById(R.id.transmission_listView_sent)
        listViewtoBeSent = findViewById(R.id.transmission_listView_tobesent)
        listViewSent?.emptyView = DataController.getEmptyView(this)
        listViewtoBeSent?.emptyView = DataController.getEmptyView(this)

        switchOn = true

        update()
    }

    fun update() {
        if (!isLoading) {
            val thread = Thread(Runnable {
                isLoading = true
                try {
                    databaseReport = DatabaseReport.getDatabase(applicationContext)

                    toSend = DatabaseReport.getDeskripsi(databaseReport?.reportToBeSent!!)
                    sent = DatabaseReport.getDeskripsi(databaseReport?.reportSent!!)

                    runOnUiThread {
                        if (adapterBelumTerkirim != null) {
                            adapterBelumTerkirim!!.clear()
                            adapterBelumTerkirim!!.addAll(*toSend)
                            adapterBelumTerkirim!!.notifyDataSetChanged()
                        } else {
                            val itemToSend = ArrayList(Arrays.asList(*toSend))
                            adapterBelumTerkirim = ArrayAdapter(this@TransmissionActivity, R.layout.activity_transmission_list_row, itemToSend)
                            listViewtoBeSent?.adapter = adapterBelumTerkirim
                        }

                        if (adapterTerkirim != null) {
                            adapterTerkirim!!.clear()
                            adapterTerkirim!!.addAll(*sent)
                            adapterTerkirim!!.notifyDataSetChanged()
                        } else {
                            val itemSent = ArrayList(Arrays.asList(*sent))
                            adapterTerkirim = ArrayAdapter(this@TransmissionActivity, R.layout.activity_transmission_list_row, itemSent)
                            listViewSent?.adapter = adapterTerkirim

                        }
                    }

                    Log.i("accepet reciever", "eksekusi reciever")

                } catch (e: Exception) {
                    e.printStackTrace()

                } finally {
                    isLoading = false
                }
            })

            thread.start()
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(reciever)
    }

    class MyBroadcastReciever : BroadcastReceiver {
        private var act: TransmissionActivity? = null

        constructor()

        constructor(activity: TransmissionActivity) {
            act = activity
        }

        override fun onReceive(context: Context, intent: Intent) {
            try {
                context.startService(Intent(context, ReportUploader::class.java))
                act?.update()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        var isLoading: Boolean = false
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    internal fun sendReport() {
        try{
            sending = true
            val databaseReport = DatabaseReport.getDatabase(applicationContext)
            val toBeSent = databaseReport.reportToBeSent
            for (report in toBeSent) {
                if (report.sendReport()) {
                    databaseReport.doneReport(report, 1)
                    sendResult()
                }
            }
            sending = false
        }catch (e: java.lang.Exception) {
            e.printStackTrace()
//            sending = false
        }

    }

    private fun sendResult() {
        if (!isLoading) {
            val intent = Intent(Intent.ACTION_SEND)
            sendBroadcast(intent)
            Log.i("ReportUploader.kt", "Send result reciever")
        }
    }
}
