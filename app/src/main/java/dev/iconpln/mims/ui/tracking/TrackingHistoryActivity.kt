package dev.iconpln.mims.ui.tracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.BottomsheetTrackingActivity
import dev.iconpln.mims.databinding.ActivityTrackingBinding
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.ui.monitoring.MonitoringViewModel
import dev.iconpln.mims.utils.PopupDialog

class TrackingHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackingBinding
    private val viewModel: TrackingHistoryViewModel by viewModels()
    private var sn=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val popupDialog = PopupDialog()

        binding.btnScan.setOnClickListener {
            openScanner()
        }

        binding.btnInputManual.setOnClickListener {
            popupDialog.show(supportFragmentManager,"Masukan SN Manual")
        }

        viewModel.trackingResponse.observe(this) {
            binding.apply {
                if (it.datas.isNullOrEmpty()){
                    Toast.makeText(this@TrackingHistoryActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@TrackingHistoryActivity, SpecMaterialActivity::class.java)
                    intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, sn)
                    startActivity(intent)
                }
            }
        }

        viewModel.isLoading.observe(this){
            when(it) {
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }


    }

    private fun openScanner() {
        val scan = ScanOptions()
        scan.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        scan.setCameraId(0)
        scan.setBeepEnabled(true)
        scan.setBarcodeImageEnabled(true)
        scan.captureActivity = CustomScanActivity::class.java
        barcodeLauncher.launch(scan)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()) {result: ScanIntentResult ->
        try {
            if(!result.contents.isNullOrEmpty()){
                sn=result.contents
                Log.i("hit Api","${sn}")
                viewModel.getTrackingHistory(result.contents, this)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

}