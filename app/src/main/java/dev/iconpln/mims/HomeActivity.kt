package dev.iconpln.mims

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityHomeBinding
import dev.iconpln.mims.ui.pnerimaan.PenerimaanViewModel
import dev.iconpln.mims.ui.tracking.DataMaterialTrackingActivity
import dev.iconpln.mims.ui.tracking.SpecMaterialActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: PenerimaanViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        val penerimaan = daoSession.tPosPenerimaanDao.queryBuilder().list()

        dialog = Dialog(this@HomeActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        viewModel.getPenerimaan(daoSession,penerimaan)


        binding.fab1.setOnClickListener {
            openScanner()
        }

        val navView: BottomNavigationView = binding.navViewPabrikan
        navView.itemIconTintList = null;

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        navView.setupWithNavController(navController)
    }

    companion object {
        private var instance: HomeActivity? = null
        fun getInstance() = instance ?: HomeActivity().also {
            instance = it
        }

        var data: Any? = null
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
        try {
            if(!result.contents.isNullOrEmpty()){
                val sn=result.contents
                Log.i("hit Api","$sn")
                val apiService = ApiConfig.getApiService(this@HomeActivity)
                CoroutineScope(Dispatchers.IO).launch {
                    val response = apiService.getTrackingHistory(sn)
                    withContext(Dispatchers.Main) {
                        dialog.show()
                        try {
                            if (response.isSuccessful) {
                                dialog.dismiss()
                                if (response.body()?.datas.isNullOrEmpty()){
                                    Toast.makeText(this@HomeActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                                }else{
                                    dialog.dismiss()
                                    val intent = Intent(this@HomeActivity, SpecMaterialActivity::class.java)
                                    intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, sn)
                                    startActivity(intent)
                                }
                            } else {
                                dialog.dismiss()
                                Toast.makeText(this@HomeActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()

                            }
                        }catch (e: Exception){
                            Toast.makeText(this@HomeActivity, e.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("error", e.toString())
                        }
                    }
                }
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }
}