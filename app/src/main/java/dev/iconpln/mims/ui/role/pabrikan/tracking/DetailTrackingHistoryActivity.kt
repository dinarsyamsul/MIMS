package dev.iconpln.mims.ui.role.pabrikan.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.R

class DetailTrackingHistoryActivity : AppCompatActivity() {
    private val detailTrackingViewModel: TrackingHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tracking)

        val sn = "PLN0219000022402314000000018"
        val noTransaksi = "20230124-3-3"
        val status = "210"

        detailTrackingViewModel.getDetailTrackingHistory(sn, noTransaksi, status, this)
        
        detailTrackingViewModel.detailTrackingHistoryResponse.observe(this){
            Toast.makeText(this, "${it.datas}", Toast.LENGTH_SHORT).show()
        }

    }
}