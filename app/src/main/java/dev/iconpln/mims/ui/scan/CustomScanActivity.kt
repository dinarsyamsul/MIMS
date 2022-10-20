package dev.iconpln.mims.ui.scan

import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityCustomScanBinding

class CustomScanActivity : AppCompatActivity() {

    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var binding: ActivityCustomScanBinding
    private var flash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)

        initializeQrScanner(savedInstanceState)

        binding.btnOption.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.btn_barcode) {
                binding.borderScan.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.barcode_border
                    )
                )
                binding.btnBarcode.setTypeface(null, Typeface.BOLD)
                binding.btnQr.setTypeface(null, Typeface.NORMAL)
            } else {
                binding.borderScan.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.scan_border
                    )
                )
                binding.btnQr.setTypeface(null, Typeface.BOLD)
                binding.btnBarcode.setTypeface(null, Typeface.NORMAL)
            }
        }

        binding.flash.setOnClickListener {
            if (!flash) {
                flash = true
                flashState(flash)
                barcodeScannerView.setTorchOn()
            } else {
                flash = false
                flashState(flash)
                barcodeScannerView.setTorchOff()
            }
        }
    }

    private fun flashState(flash: Boolean) {
        if (flash) {
            binding.flash.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_flash_on_24)
            )
        } else {
            binding.flash.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_flash_off_24)
            )
        }
    }

    private fun initializeQrScanner(savedInstanceState: Bundle?) = with(binding) {
        capture = CaptureManager(this@CustomScanActivity, zxingBarcodeScanner)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(false)
        capture.decode()
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(
            keyCode,
            event
        )
    }
}