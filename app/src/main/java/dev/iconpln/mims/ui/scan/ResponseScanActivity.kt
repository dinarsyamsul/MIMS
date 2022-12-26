package dev.iconpln.mims.ui.scan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityResponseScanBinding
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.TokenManager
import dev.iconpln.mims.utils.ViewModelFactory

class ResponseScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResponseScanBinding
    private lateinit var viewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResponseScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = TokenManager(this)
        val apiService = ApiConfig.getApiService()
        val networkStatusTracker = NetworkStatusTracker(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(session, apiService, networkStatusTracker)
        )[ScanViewModel::class.java]

        val data = intent.extras
        if (data != null) {
            binding.tvResult.text = data.getString(EXTRA_SN)
        }

        val sn = data?.getString(EXTRA_SN)

        if (sn != null) {
            viewModel.getDetailBySN(sn)
        }
        
        binding.btnsimpan.setOnClickListener {
            Toast.makeText(this, "Data Berhasil DiSimpan", Toast.LENGTH_SHORT).show()
        }

        viewModel.snResponse.observe(this) {
            Toast.makeText(this, "Scan Berhasil", Toast.LENGTH_SHORT).show()
            for (data in it) {
                binding.apply {
                    tvResult.text = ": ${data.serialNumber}"
                    tvAsset.text = ": ${data.noAsset}"
                    tvMaterial.text = ": ${data.materialDesc}"
                    tvNamafab.text = ": ${data.namaPabrikan}"
                    tvNamainsp.text = ": ${data.namaInspektur}"
                    tvNamaklas.setText(": ${data.namaKlasifikasiRetur}")
                    tvTglRetur.setText(": ${data.tglInspeksiretur}")
                    tvNoba.text = ": ${data.noBa}"
                    tvTahun.text = ": ${data.tahun}"
                }
            }
        }
    }

    companion object {
        const val EXTRA_SN = "extra_sn"
    }
}