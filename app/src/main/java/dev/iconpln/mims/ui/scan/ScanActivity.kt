package dev.iconpln.mims.ui.scan

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.CodeScanner.ALL_FORMATS
import com.budiyev.android.codescanner.CodeScanner.CAMERA_BACK
import dev.iconpln.mims.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var scanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermission()
        codeScanner()

        binding.btnOption.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == dev.iconpln.mims.R.id.btn_barcode) {
                binding.scan.setFrameAspectRatio(3f, 1.2f)
                binding.btnBarcode.setTypeface(null, Typeface.BOLD)
                binding.btnQr.setTypeface(null, Typeface.NORMAL)
            } else {
                binding.scan.setFrameAspectRatio(1f, 1f)
                binding.btnQr.setTypeface(null, Typeface.BOLD)
                binding.btnBarcode.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    private fun codeScanner() {
        scanner = CodeScanner(this, binding.scan)

        scanner.apply {
            camera = CAMERA_BACK
            formats = ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val intent = Intent(this@ScanActivity, ResponseScanActivity::class.java)
                    intent.putExtra(ResponseScanActivity.EXTRA_SN, it.text)
                    startActivity(intent)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner : ${it.message}")
                }
            }

            binding.scan.setOnClickListener {
                scanner.startPreview()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scanner.startPreview()
    }

    override fun onPause() {
        scanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}