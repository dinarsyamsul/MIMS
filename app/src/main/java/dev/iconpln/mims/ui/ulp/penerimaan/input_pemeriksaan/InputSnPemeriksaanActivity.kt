package dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TListSnMaterialPenerimaanUlp
import dev.iconpln.mims.data.local.database.TListSnMaterialPenerimaanUlpDao
import dev.iconpln.mims.data.local.database.TMonitoringSnMaterial
import dev.iconpln.mims.data.local.database.TMonitoringSnMaterialDao
import dev.iconpln.mims.data.local.database.TTransPenerimaanDetailUlp
import dev.iconpln.mims.data.local.database.TTransPenerimaanDetailUlpDao
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlp
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlpDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityInputSnPemeriksaanBinding
import dev.iconpln.mims.databinding.ItemDetailPemeriksaanUlpBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.ulp.penerimaan.PenerimaanUlpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList

class InputSnPemeriksaanActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityInputSnPemeriksaanBinding
    private lateinit var daoSession: DaoSession
    private var noMaterial: String = ""
    private var noRepackaging: String = ""
    private var noTransaksi: String = ""
    private lateinit var adapter: PemeriksaanUlpSnAdapter
    private lateinit var lisSn : List<TListSnMaterialPenerimaanUlp>
    private lateinit var details: TTransPenerimaanDetailUlp
    private lateinit var penerimaan: TTransPenerimaanUlp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputSnPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noMaterial = intent.getStringExtra("noMaterial")!!
        noRepackaging = intent.getStringExtra("noRepackaging")!!
        noTransaksi = intent.getStringExtra("noTransaksi")!!

        lisSn = daoSession.tListSnMaterialPenerimaanUlpDao.queryBuilder()
            .where(TListSnMaterialPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging))
            .where(TListSnMaterialPenerimaanUlpDao.Properties.NoMaterial.eq(noMaterial)).list()

        details = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
            .where(TTransPenerimaanDetailUlpDao.Properties.NoTransaksi.eq(noTransaksi)).list().get(0)

        penerimaan = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .where(TTransPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging)).list().get(0)

        adapter = PemeriksaanUlpSnAdapter(arrayListOf(), object : PemeriksaanUlpSnAdapter.OnAdapterListener{
            override fun onClick(tms: TListSnMaterialPenerimaanUlp) {}

        })

        with(binding){
            txtGudangAsal.text = penerimaan.gudangAsal
            txtNoMaterial.text = noMaterial
            txtSpesifikasi.text = details.materialDesc

            btnScanSnMaterial.setOnClickListener { openScanner() }
            btnInputSnManual.setOnClickListener { showPopUp() }
            btnScan.setOnClickListener { openScanner() }

            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@InputSnPemeriksaanActivity,LinearLayoutManager.VERTICAL,false)
            rvListSn.setHasFixedSize(true)

            btnBack.setOnClickListener { onBackPressed() }
            btnSimpan.setOnClickListener { validation() }

            srcNoSn.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val sn = s.toString()
                   val listSearch =  daoSession.tListSnMaterialPenerimaanUlpDao.queryBuilder()
                        .where(TListSnMaterialPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging))
                        .where(TListSnMaterialPenerimaanUlpDao.Properties.NoMaterial.eq(noMaterial))
                       .where(TListSnMaterialPenerimaanUlpDao.Properties.NoSerialNumber.like("%"+sn+"%")).list()

                    adapter.setTmsList(listSearch)
                }

            })
        }

        adapter.setTmsList(lisSn)
    }

    private fun validation() {
//        lisSn = daoSession.tListSnMaterialPenerimaanUlpDao.queryBuilder()
//            .where(TListSnMaterialPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging))
//            .where(TListSnMaterialPenerimaanUlpDao.Properties.NoMaterial.eq(noMaterial)).list()
//
//        if (lisSn.isEmpty()){
//            Toast.makeText(this@InputSnPemeriksaanActivity, "Kamu belum scan Serial Number", Toast.LENGTH_SHORT).show()
//            return
//        }

        submitForm()
    }

    private fun submitForm() {
        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        details.isDone = 1
        daoSession.tTransPenerimaanDetailUlpDao.update(details)

        var jwt = SharedPrefsUtils.getStringPreference(this@InputSnPemeriksaanActivity,"jwt","")
        var plant = SharedPrefsUtils.getStringPreference(this@InputSnPemeriksaanActivity,"plant","")
        var storloc = SharedPrefsUtils.getStringPreference(this@InputSnPemeriksaanActivity,"storloc","")
        var username = SharedPrefsUtils.getStringPreference(this@InputSnPemeriksaanActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemeriksaanUp3_detail" + username + "_" + penerimaan.noPermintaan + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data pemeriksaan Detail up 3"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()

        params.add(ReportParameter("1", reportId, "no_transaksi", details.noTransaksi!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "no_material", details.noMaterial, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "username", username!!, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "qty",lisSn.size.toString() , ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendReportPenerimaanUlpDetail(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)

    }

    private fun showPopUp() {
        val dialog = Dialog(this@InputSnPemeriksaanActivity)
        dialog.setContentView(R.layout.monitoring_permintaan_popdialog);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_Ok) as AppCompatButton
        val etMessage = dialog.findViewById(R.id.inpt_snMaterial) as EditText

        btnOk.setOnClickListener {
            val listSn = TListSnMaterialPenerimaanUlp()
            listSn.noRepackaging = noRepackaging
            listSn.isScanned = 1
            listSn.status = "SESUAI"
            listSn.noMaterial = noMaterial
            listSn.noSerialNumber = etMessage.text.toString()
            daoSession.tListSnMaterialPenerimaanUlpDao.insert(listSn)
//            val listSn = TListSnMaterialPenerimaanUlp()
//            var isSnExist = false
//            for (i in lisSn){
//                if (i.noSerialNumber == etMessage.text.toString()){
//                    isSnExist = true
//                    listSn.noRepackaging = noRepackaging
//                    listSn.isScanned = 1
//                    listSn.serialNumber = etMessage.text.toString()
//                    listSn.nomorMaterial = noMaterial
//                    listSn.status = "SESUAI"
//                    daoSession.tMonitoringSnMaterialDao.insert(listSn)
//                }
//            }
//
//            if (!isSnExist){
//                Toast.makeText(this@InputSnPemeriksaanActivity, "SN Tidak ditemukan", Toast.LENGTH_SHORT).show()
//            }

            val reloadList = daoSession.tListSnMaterialPenerimaanUlpDao.queryBuilder()
                .where(TListSnMaterialPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging))
                .where(TListSnMaterialPenerimaanUlpDao.Properties.NoMaterial.eq(noMaterial)).list()

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
                binding.srcNoSn.setText(result.contents)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    override fun setLoading(show: Boolean, title: String, message: String) {

    }

    override fun setFinish(result: Boolean, message: String) {
        Toast.makeText(this@InputSnPemeriksaanActivity, message,Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@InputSnPemeriksaanActivity, DetailPemeriksaanActivity::class.java)
            .putExtra("noRepackaging", noRepackaging)
            .putExtra("noPengiriman", penerimaan.noPengiriman)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}