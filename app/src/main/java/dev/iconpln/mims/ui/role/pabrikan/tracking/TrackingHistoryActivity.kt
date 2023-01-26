package dev.iconpln.mims.ui.role.pabrikan.tracking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession

class TrackingHistoryActivity : AppCompatActivity() {
    private val trackingViewModel: TrackingHistoryViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        daoSession = (application as MyApplication).daoSession!!

        val sn = "PLN0219000022402314000000018"
        trackingViewModel.getTrackingHistory(sn, this)

        trackingViewModel.trackingResponse.observe(this) {
            Toast.makeText(this, "${it.historis}", Toast.LENGTH_SHORT).show().also {
                startActivity(
                    Intent(
                        this@TrackingHistoryActivity,
                        DetailTrackingHistoryActivity::class.java
                    )
                )
            }
        }

        val listDataPo = daoSession.tPosDao.queryBuilder().list()
        listDataPo.forEach {
            Log.d("Tracking", "cek atuh ah ${it.tlskNo}")
        }

        val listDataPoSns = daoSession.tPosSnsDao.queryBuilder().list()
        listDataPoSns.forEach {
            Log.d("Tracking", "cek atuh ih ${it.doStatus}")
        }
    }


}