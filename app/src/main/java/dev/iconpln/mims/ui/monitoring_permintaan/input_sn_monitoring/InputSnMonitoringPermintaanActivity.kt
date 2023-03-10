package dev.iconpln.mims.ui.monitoring_permintaan.input_sn_monitoring

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import dev.iconpln.mims.ui.monitoring_permintaan.monitoring_permintaan_detail.MonitoringPermintaanDetailActivity

class InputSnMonitoringPermintaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputSnMonitoringPermintaanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listSnm: List<TMonitoringSnMaterial>
    private lateinit var listSnPermaterial: List<TSnMonitoringPermintaan>
    private lateinit var monitoringPenerimaan: TTransMonitoringPermintaan
    private lateinit var adapter: MonitoringPermintaanSnAdapter
    private var noPermintaan: String? = ""
    private var noTransaksi: String? = ""
    private var noRepackaging: String? = ""
    private var noMaterial: String? = ""
    private var descMaterial: String? = ""
    private var kategori: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputSnMonitoringPermintaanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!
        noPermintaan = intent.getStringExtra("noPermintaan")
        noTransaksi = intent.getStringExtra("noTransaksi")
        noRepackaging = intent.getStringExtra("noRepackaging")
        noMaterial = intent.getStringExtra("noMat")
        descMaterial = intent.getStringExtra("desc")
        kategori = intent.getStringExtra("kategori")

        listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .list()

        listSnPermaterial = daoSession.tSnMonitoringPermintaanDao.queryBuilder()
            .where(TSnMonitoringPermintaanDao.Properties.NoRepackaging.eq(noRepackaging)).list()

        monitoringPenerimaan = daoSession.tTransMonitoringPermintaanDao.queryBuilder()
            .where(TTransMonitoringPermintaanDao.Properties.NoTransaksi.eq(noTransaksi)).list()[0]

        adapter = MonitoringPermintaanSnAdapter(arrayListOf(), object : MonitoringPermintaanSnAdapter.OnAdapterListener{
            override fun onClick(tms: TMonitoringSnMaterial) {
                showPopuDelete(tms)
            }

        })

        adapter.setTmsList(listSnm)

        with(binding){
            txtDescMaterial.text = descMaterial
            txtKategori.text = kategori
            txtNoMaterial.text = noMaterial
            txtUnitAsal.text = monitoringPenerimaan.storLocAsalName
            txtQtyPermaterial.text = listSnPermaterial.size.toString()

            btnBack.setOnClickListener { onBackPressed() }

            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@InputSnMonitoringPermintaanActivity, LinearLayoutManager.VERTICAL,false)
            rvListSn.setHasFixedSize(true)

            btnScanSnMaterial.setOnClickListener { openScanner() }

            btnInputSnManual.setOnClickListener { showPopUp() }

            btnSimpan.setOnClickListener { validate() }
        }
    }

    private fun showPopuDelete(tms: TMonitoringSnMaterial) {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_validation)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val icon = dialog.findViewById(R.id.imageView11) as ImageView
        val btnOk = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.message) as TextView
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        icon.setImageResource(R.drawable.ic_popup_delete)
        message.text = "Hapus Data"
        txtMessage.text = "Apakah anda yakin menghapus data ini?"


        btnTidak.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            dialog.dismiss();

            daoSession.tMonitoringSnMaterialDao.delete(tms)

            val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                .list()

            adapter.setTmsList(reloadList)

            val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
                .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
                .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
                .list()[0]

            permintaanDetail.qtyScan = reloadList.size.toString()
            daoSession.tTransMonitoringPermintaanDetailDao.update(permintaanDetail)
        }
        dialog.show();
    }

    private fun validate() {
        val listSn = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .list()

        if (listSn.size == 0){
            Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Tidak boleh simpan sebelum scan sn material", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_complaint)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        txtMessage.text = "Data material berhasil di simpan"

        btnOk.setOnClickListener {
            dialog.dismiss();
            submitForm()
        }
        dialog.show();
    }

    private fun submitForm() {
        val listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .list()

        val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
            .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
            .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
            .list()[0]

        if (listSnm.size == permintaanDetail.qtyPermintaan){
            permintaanDetail.isScannedSn = 1
        }

        permintaanDetail.qtyScan = listSnm.size.toString()
        daoSession.tTransMonitoringPermintaanDetailDao.update(permintaanDetail)

        Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Data Serial Material berhasil disimpan", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@InputSnMonitoringPermintaanActivity, MonitoringPermintaanDetailActivity::class.java)
            .putExtra("noPermintaan",monitoringPenerimaan.noPermintaan)
            .putExtra("noTransaksi", noTransaksi))
        finish()
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
            var isSnExist = false
            for (i in listSnPermaterial){
                if (i.serialNumber == etMessage.text.toString()){
                    isSnExist = true
                    listSn.noRepackaging = noRepackaging
                    listSn.isScanned = 1
                    listSn.serialNumber = etMessage.text.toString()
                    listSn.nomorMaterial = noMaterial
                    listSn.status = "SESUAI"
                    daoSession.tMonitoringSnMaterialDao.insert(listSn)
                }
            }

            if (!isSnExist){
                Toast.makeText(this@InputSnMonitoringPermintaanActivity, "SN Tidak ditemukan", Toast.LENGTH_SHORT).show()
            }

            val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                .list()

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
                var isSnExist = false
                for (i in listSnPermaterial){
                    if (i.serialNumber == result.contents){
                        isSnExist = true
                        listSn.noRepackaging = noRepackaging
                        listSn.isScanned = 1
                        listSn.serialNumber = result.contents
                        listSn.nomorMaterial = noMaterial
                        listSn.status = "SESUAI"
                        daoSession.tMonitoringSnMaterialDao.insert(listSn)
                    }
                }

                if (!isSnExist){
                    Toast.makeText(this@InputSnMonitoringPermintaanActivity, "SN Tidak ditemukan", Toast.LENGTH_SHORT).show()
                }

                val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                    .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                    .list()
                adapter.setTmsList(reloadList)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    override fun onBackPressed() {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_validation)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val icon = dialog.findViewById(R.id.imageView11) as ImageView
        val btnOk = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.message) as TextView
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        icon.setImageResource(R.drawable.ic_popup_delete)
        message.text = "Keluar"
        txtMessage.text = "Jika keluar maka data tidak disimpan"


        btnTidak.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            dialog.dismiss();
            val listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                .list()

            daoSession.tMonitoringSnMaterialDao.deleteInTx(listSnm)

            val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
                .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
                .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
                .list()[0]

            permintaanDetail.qtyScan = "0"
            daoSession.tTransMonitoringPermintaanDetailDao.update(permintaanDetail)

            startActivity(Intent(this@InputSnMonitoringPermintaanActivity, MonitoringPermintaanDetailActivity::class.java)
                .putExtra("noPermintaan", monitoringPenerimaan.noPermintaan)
                .putExtra("noTransaksi", noTransaksi)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        dialog.show();
    }
}