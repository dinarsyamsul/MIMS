package dev.iconpln.mims.ui.tracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.BottomsheetTrackingActivity
import dev.iconpln.mims.databinding.ActivityTrackingBinding
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.utils.PopupDialog

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
//            bottomsheetFragment.show(supportFragmentManager,"silahkan scan bang")
            openScanner()
        }

//        val sn = "PLN0219000022402314000000018"

        binding.btnInputManual.setOnClickListener {
            popupDialog.show(supportFragmentManager,"Masukan SN Manual")
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
        ScanContract()
    ) { result: ScanIntentResult ->
        try {
            if (!result.contents.isNullOrEmpty()) {

//            viewModel.getDetailBySN(result.contents)

                val intent = Intent(this, DataMaterialTrackingActivity::class.java)
                intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, result.contents)
                startActivity(intent)
//            Toast.makeText(this, "Serial Number: ${result.contents}", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }
}