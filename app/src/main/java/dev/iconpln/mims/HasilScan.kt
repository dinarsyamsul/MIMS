package dev.iconpln.mims

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.databinding.ActivityHasilScanBinding
import dev.iconpln.mims.ui.scan.ScanViewModel

@AndroidEntryPoint
class HasilScan : AppCompatActivity() {

    private lateinit var binding: ActivityHasilScanBinding
    private val viewModel: ScanViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
//        if (data != null) {
//            binding.tvResult.text = data.getString(ResponseScanActivity.EXTRA_SN)
//        }

        val sn = data?.getString(EXTRA_SN)

        if (sn != null) {
            viewModel.getDetailBySN(sn)
        }

        viewModel.snResponse.observe(this) { data ->
            if (data.message == "Success") {
                Toast.makeText(this, "Scan Berhasil", Toast.LENGTH_SHORT).show()
                data.detailSN.forEach { result ->
                    binding.apply {
                        tvSn.text = "${result.serialNumber}"
                        tvVendor.text = "${result.namaPabrikan}"
                        tvKategori.text = "${result.kategoriMaterial}"
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_SN = "extra_sn"
    }
}