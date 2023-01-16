package dev.iconpln.mims.utils

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.HasilScan
import dev.iconpln.mims.R
import dev.iconpln.mims.ui.scan.ResponseScanActivity
import dev.iconpln.mims.ui.scan.ScanViewModel

@AndroidEntryPoint
class MemuatData : AppCompatActivity() {

    private val viewModel: ScanViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memuat_data)

        val data = intent.extras
        val sn = data?.getString(EXTRA_SN)

        if (sn != null) {
            viewModel.getDetailBySN(sn)
        }

        viewModel.snResponse.observe(this) { data ->
            Log.d("customactivty", "cek data ${data.message}")
            if (data.message == "Success") {
                data.detailSN.forEach {result ->
                    val intent = Intent(this@MemuatData, HasilScan::class.java)
                    intent.putExtra(HasilScan.EXTRA_SN, result.serialNumber)
                    startActivity(intent)
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            Log.d("ResponseActivity", "cek response $it")
            if (it != null) {
                val intent = Intent(this@MemuatData, NotFound::class.java)
                startActivity(intent)
                Toast.makeText(this, "Data serial number tidak sesuai", Toast.LENGTH_LONG).show()

            }
        }
    }

    companion object {
        const val EXTRA_SN = "extra_sn"
    }
}