package dev.iconpln.mims.ui.scan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityResponseScanBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
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
            val intent = Intent(this@ResponseScanActivity, DashboardPabrikanActivity::class.java)
            startActivity(intent)
        }

        viewModel.snResponse.observe(this) {data->
            Toast.makeText(this, "Scan Berhasil", Toast.LENGTH_SHORT).show()

                binding.apply {
                    tvResult.text = ": ${data.serialNumber}"
                    tvMaterialId.text =": ${data.materialId}"
                    tvNoMaterial.text =": ${data.nomorMaterial}"
                    tvKodePabrik.text =": ${data.kodePabrikan}"
                    tvNamaPabrik.text =": ${data.namaPabrikan}"
                    tvTglProduksi.text =": ${data.tglProduksi}"
                    tvSpin.text = ": ${data.spln}"
                    tvSpekMaterial.text = ": ${data.spesifikasiMaterial}"
                    tvKatMaterial.text = ": ${data.kategoriMaterial}"
                    tvMasaGaransi.text = ": ${data.masaGaransi}"
                    tvNomorSert.text = ": ${data.nomorSertMetrologi}"
                    tvNomorProduksi.text = ": ${data.noProduksi}"
                    tvNomorPackaging.text = ": ${data.noPackaging}"
                }

        }
    }

    companion object {
        const val EXTRA_SN = "extra_sn"
    }
}