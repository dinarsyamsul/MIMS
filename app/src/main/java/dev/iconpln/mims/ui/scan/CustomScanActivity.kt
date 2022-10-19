package dev.iconpln.mims.ui.scan

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.journeyapps.barcodescanner.*
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityCustomBarcodeBinding

class CustomBarcodeActivity : AppCompatActivity() {

    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var binding: ActivityCustomBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)

        initializeQrScanner(savedInstanceState)

        binding.btnOption.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.btn_barcode){
                binding.borderScan.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barcode_border))
                binding.btnBarcode.setTypeface(null, Typeface.BOLD)
                binding.btnQr.setTypeface(null, Typeface.NORMAL)
            } else {
                binding.borderScan.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.scan_border))
                binding.btnQr.setTypeface(null, Typeface.BOLD)
                binding.btnBarcode.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    private fun initializeQrScanner(savedInstanceState: Bundle?) = with(binding) {
        capture = CaptureManager(this@CustomBarcodeActivity, zxingBarcodeScanner)
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
        return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}