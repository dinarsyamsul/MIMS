package dev.iconpln.mims.ui.scan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.NotFound
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

        viewModel.snResponse.observe(this) { data ->
            if (data.message == "Success") {
                Toast.makeText(this, "Scan Berhasil", Toast.LENGTH_SHORT).show()
                binding.apply {
                    tvResult.text = "${data.detailSN.serialNumber}"
                    tvNoMaterial.text = "${data.detailSN.nomorMaterial}"
                    tvKodePabrik.text = "${data.detailSN.kodePabrikan}"
                    tvNamaPabrik.text = "${data.detailSN.namaPabrikan}"
                    tvTglProduksi.text = "${data.detailSN.tglProduksi}"
                    tvSpin.text = "${data.detailSN.spln}"
                    tvSpekMaterial.text = "${data.detailSN.spesifikasiMaterial}"
                    tvKatMaterial.text = "${data.detailSN.kategoriMaterial}"
                    tvMasaGaransi.text = "${data.detailSN.masaGaransi}"
                    tvNomorSert.text = "${data.detailSN.nomorSertMetrologi}"
                    tvNoProduksi.text = "${data.detailSN.noProduksi}"
                    tvNoPack.text = "${data.detailSN.noPackaging}"
                }
            }
        }


//        viewModel.errorMessage.observe(this) {
//            Log.d("ResponseActivity","cek $it")
//            if (it != null) {
//                val intent = Intent(this@ResponseScanActivity, NotFound::class.java)
//                startActivity(intent)
//                Toast.makeText(this, "Data serial number tidak sesuai", Toast.LENGTH_LONG).show()
//
//            }
//        }
    }

    companion object {
        const val EXTRA_SN = "extra_sn"
    }
}