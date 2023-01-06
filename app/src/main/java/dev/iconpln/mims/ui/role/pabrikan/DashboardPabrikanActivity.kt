package dev.iconpln.mims.ui.role.pabrikan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityDashboardPabrikanBinding
import dev.iconpln.mims.ui.scan.CustomScanActivity
import dev.iconpln.mims.utils.MemuatData

class DashboardPabrikanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardPabrikanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab1.setOnClickListener {
//            val i = Intent (this@DashboardPabrikanActivity, ScanActivity::class.java)
//            startActivity(i)
//            startActivity(
//                Intent(
//                    this,
//                    ScannerActivity::class.java
//                )
//            ) //ini menggunakan library ML Kit
            openScanner() // ini menggunakan library zxing
        }

        val navView: BottomNavigationView = binding.navViewPabrikan

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
        if (!result.contents.isNullOrEmpty()) {

//            viewModel.getDetailBySN(result.contents)

            val intent = Intent(this, MemuatData::class.java)
            intent.putExtra(MemuatData.EXTRA_SN, result.contents)
            startActivity(intent)
            Toast.makeText(this, "Serial Number: ${result.contents}", Toast.LENGTH_LONG).show()
        } else {
//             CANCELED
        }
    }

    //    class DataSn private constructor(){
    companion object {
        private var instance: DashboardPabrikanActivity? = null
        fun getInstance() = instance ?: DashboardPabrikanActivity().also {
            instance = it
        }

        var data: Any? = null
    }
//    }

}