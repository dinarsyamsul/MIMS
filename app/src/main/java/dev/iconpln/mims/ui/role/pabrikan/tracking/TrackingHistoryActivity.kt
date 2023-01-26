package dev.iconpln.mims.ui.role.pabrikan.tracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TrackingHistoryActivity : AppCompatActivity() {
    private val trackingViewModel: TrackingHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        val sn = "PLN0219000022402314000000018"
        trackingViewModel.getTrackingHistory(sn, this)

        trackingViewModel.trackingResponse.observe(this){
            Toast.makeText(this, "${it.historis}", Toast.LENGTH_SHORT).show().also {
                startActivity(Intent(this@TrackingHistoryActivity, DetailTrackingHistoryActivity::class.java))
            }
        }
    }
}