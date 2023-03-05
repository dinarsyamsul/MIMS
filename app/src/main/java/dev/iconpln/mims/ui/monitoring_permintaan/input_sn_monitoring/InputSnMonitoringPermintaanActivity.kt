package dev.iconpln.mims.ui.monitoring_permintaan.input_sn_monitoring

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityInputSnMonitoringPermintaanBinding
import dev.iconpln.mims.databinding.ActivityMonitoringPermintaanDetailBinding

class InputSnMonitoringPermintaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputSnMonitoringPermintaanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listSnm: List<TMonitoringSnMaterial>
    private lateinit var adapter: MonitoringPermintaanSnAdapter
    private var noPermintaan: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputSnMonitoringPermintaanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!
        noPermintaan = intent.getStringExtra("noPermintaan")

        listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoPermintaan.eq(noPermintaan)).list()

        adapter = MonitoringPermintaanSnAdapter(arrayListOf(), object : MonitoringPermintaanSnAdapter.OnAdapterListener{
            override fun onClick(tms: TMonitoringSnMaterial) {
                for (i in listSnm){
                    if (i.id == tms.id){
                        daoSession.tMonitoringSnMaterialDao.delete(i)
                    }
                }

                val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                    .where(TMonitoringSnMaterialDao.Properties.NoPermintaan.eq(noPermintaan)).list()

                adapter.setTmsList(reloadList)

            }

        })

        adapter.setTmsList(listSnm)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }

            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@InputSnMonitoringPermintaanActivity, LinearLayoutManager.VERTICAL,false)
            rvListSn.setHasFixedSize(true)

            btnScanSnMaterial.setOnClickListener { openScanner() }

            btnInputSnManual.setOnClickListener { showPopUp() }
        }
    }

    private fun showPopUp() {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.monitoring_permintaan_popdialog);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_Ok) as AppCompatButton
        val etMessage = dialog.findViewById(R.id.inpt_snMaterial) as EditText

        btnOk.setOnClickListener {
            val listSn = TMonitoringSnMaterial()
            listSn.noPermintaan = noPermintaan
            listSn.isScanned = "1"
            listSn.snMaterial = etMessage.text.toString()
            daoSession.tMonitoringSnMaterialDao.insert(listSn)

            val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                .where(TMonitoringSnMaterialDao.Properties.NoPermintaan.eq(noPermintaan)).list()

            adapter.setTmsList(reloadList)
            dialog.dismiss();
        }
        dialog.show();
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
                Log.i("hit barcode","${result.contents}")
                val listSn = TMonitoringSnMaterial()
                listSn.noPermintaan = noPermintaan
                listSn.isScanned = "1"
                listSn.snMaterial = result.contents
                daoSession.tMonitoringSnMaterialDao.insert(listSn)

                val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                    .where(TMonitoringSnMaterialDao.Properties.NoPermintaan.eq(noPermintaan)).list()

                adapter.setTmsList(reloadList)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }
}