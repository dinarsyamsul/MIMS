package dev.iconpln.mims.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.databinding.ActivityDashboardBinding
import dev.iconpln.mims.ui.scan.CustomScanActivity
import dev.iconpln.mims.ui.scan.ResponseScanActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanner.setOnClickListener {
//            startActivity(Intent(this, ScanActivity::class.java)) //ini menggunakan library yuriy budiev
            openScanner() // ini menggunakan library zxing
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
        if (!result.contents.isNullOrEmpty()) {
            val intent = Intent(this, ResponseScanActivity::class.java)
            intent.putExtra(ResponseScanActivity.EXTRA_SN, result.contents)
            startActivity(intent)
            Toast.makeText(this, "Scan Result: ${result.contents}", Toast.LENGTH_LONG).show()
        } else {
            // CANCELED
        }
    }
}