package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import dev.iconpln.mims.ui.scan.ResponseScanActivity
import dev.iconpln.mims.ui.scan.ScanViewModel
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.TokenManager
import dev.iconpln.mims.utils.ViewModelFactory

class MemuatData : AppCompatActivity() {

    private lateinit var viewModel: ScanViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memuat_data)

        val session = TokenManager(this)
        val apiService = ApiConfig.getApiService()
        val networkStatusTracker = NetworkStatusTracker(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(session, apiService, networkStatusTracker)
        )[ScanViewModel::class.java]

        val data = intent.extras
        val sn = data?.getString(ResponseScanActivity.EXTRA_SN)

        if (sn != null) {
            viewModel.getDetailBySN(sn)
        }

//        val sn = data?.getString(ResponseScanActivity.EXTRA_SN)


        viewModel.snResponse.observe(this) { data ->
            Log.d("customactivty","cek data ${data.message}")
            if (data.message == "Success"){
                val intent = Intent(this@MemuatData, ResponseScanActivity::class.java)
                intent.putExtra(ResponseScanActivity.EXTRA_SN, data.detailSN.serialNumber)
                startActivity(intent)
            }
        }
        viewModel.errorMessage.observe(this){
            Log.d("ResponseActivity","cek $it")
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