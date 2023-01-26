package dev.iconpln.mims.ui.role.pabrikan.tracking

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.BottomsheetTrackingActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.databinding.ActivityTrackingBinding
import dev.iconpln.mims.utils.PopupDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TrackingHistoryActivity : AppCompatActivity() {
    private val trackingViewModel: TrackingHistoryViewModel by viewModels()
    private lateinit var binding: ActivityTrackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomsheetFragment = BottomsheetTrackingActivity()
        val popupDialog = PopupDialog()


        binding.btnScan.setOnClickListener {
            bottomsheetFragment.show(supportFragmentManager,"silahkan scan bang")
        }

        val sn = "PLN0219000022402314000000018"
        trackingViewModel.getTrackingHistory(sn, this)

        trackingViewModel.trackingResponse.observe(this){
            Toast.makeText(this, "${it.historis}", Toast.LENGTH_SHORT).show().also {
                startActivity(Intent(this@TrackingHistoryActivity, DetailTrackingHistoryActivity::class.java))
            }
        }

        binding.btnInputManual.setOnClickListener {
            popupDialog.show(supportFragmentManager,"Masukan SN Manual")
        }
    }
}