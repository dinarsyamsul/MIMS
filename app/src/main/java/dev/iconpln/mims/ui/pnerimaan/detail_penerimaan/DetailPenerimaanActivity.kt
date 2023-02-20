package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pemeriksaan.complaint.ComplaintActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanViewModel
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList


class DetailPenerimaanActivity : AppCompatActivity(),Loadable {
    private lateinit var daoSession: DaoSession
    private var progressDialog: AlertDialog? = null
    private lateinit var binding: ActivityDetailPenerimaanBinding
    private val viewModel: PenerimaanViewModel by viewModels()
    private lateinit var adapter: DetailPenerimaanAdapter
    private lateinit var listDetailPen: MutableList<TPosDetailPenerimaan>
    private lateinit var penerimaan: TPosPenerimaan
    private var noDo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noDo = intent.getStringExtra("noDo")!!

        listDetailPen = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0))
            .where(TPosDetailPenerimaanDao.Properties.IsChecked.eq(0)).list()

        penerimaan = daoSession.tPosPenerimaanDao.queryBuilder()
            .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        adapter = DetailPenerimaanAdapter(arrayListOf(), object : DetailPenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetailPenerimaan) {}},daoSession)

        adapter.setData(listDetailPen)

        with(binding){
            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@DetailPenerimaanActivity, LinearLayoutManager.VERTICAL, false)
            rvListSn.setHasFixedSize(true)

            txtKurirPengiriman.text = penerimaan.expeditions
            txtTglKirim.text = "Tgl ${penerimaan.createdDate}"
            txtPetugasPenerima.text = penerimaan.petugasPenerima
            txtDeliveryOrder.text = penerimaan.noDoSmar
            txtNamaKurir.text = penerimaan.kurirPengantar

            btnScanPackaging.setOnClickListener {
                openScanner(1)
            }

            btnScanSn.setOnClickListener {
                openScanner(2)
            }

            srcNoSn.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val listSnsFilter = listDetailPen.filter {
                        it.serialNumber.toLowerCase().contains(s.toString().toLowerCase())
                    }
                    adapter.setData(listSnsFilter)
                }

            })

            cbSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                cbTidakSesuai.isEnabled = !isChecked
                if (isChecked){
                    for (i in listDetailPen){
                        i.status = "SESUAI"
                        i.isChecked = 1
                        daoSession.update(i)
                    }
                    adapter.setData(listDetailPen)
                }else{
                    for (i in listDetailPen){
                        i.status = ""
                        i.isChecked = 0
                        daoSession.update(i)
                    }
                    adapter.setData(listDetailPen)
                }
            }

            cbTidakSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                cbSesuai.isEnabled = !isChecked
                if (isChecked){
                    for (i in listDetailPen){
                        i.status = "TIDAK SESUAI"
                        i.isChecked = 1
                        daoSession.update(i)
                    }
                    adapter.setData(listDetailPen)
                }else{
                    for (i in listDetailPen){
                        i.status = ""
                        i.isChecked = 0
                        daoSession.update(i)
                    }
                    adapter.setData(listDetailPen)
                }
            }

            btnKomplain.setOnClickListener {
                validComplaint()
            }

            btnTerima.setOnClickListener {
                validTerima()
            }

            btnBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun validTerima() {
        for (i in listDetailPen){
            Log.d("checkList", i.status)
            if (i.status == "TIDAK SESUAI" || i.status.isNullOrEmpty() ){
                Toast.makeText(this@DetailPenerimaanActivity, "Tidak boleh terima dengan status tidak sesuai atau kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val dialog = Dialog(this@DetailPenerimaanActivity)
        dialog.setContentView(R.layout.popup_validation);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton

        btnTidak.setOnClickListener {
            submitForm(0)
            dialog.dismiss();
        }

        btnYa.setOnClickListener {
            submitForm(1)
            dialog.dismiss()
        }

        dialog.show();
    }

    private fun submitForm(isPeriksa: Int) {
        var sns = ""
        var checkedDetPen = listDetailPen.filter { it.isChecked == 1 }
        for (i in checkedDetPen){
            sns += "${i.noPackaging},${i.serialNumber},${i.noMaterial};"
            Log.i("noPackaging", i.noPackaging)

        }
        if (sns != "") {
            sns = sns.substring(0, sns.length - 1)
        }

        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //region Add report visit to queue
        var jwt = SharedPrefsUtils.getStringPreference(this@DetailPenerimaanActivity,"jwt","")
        var username = SharedPrefsUtils.getStringPreference(this@DetailPenerimaanActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_penerimaan" + username + "_" + noDo + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data Dokumen Penerimaan"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "no_do_smar", noDo!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "quantity", checkedDetPen.size.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "do_line_item", penerimaan.doLineItem, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "is_periksa", isPeriksa.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "sns", sns, ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "username", username!!, ReportParameter.TEXT))
        params.add(ReportParameter("7", reportId, "email", username, ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendPenerimaan(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)

        updateData(checkedDetPen)
    }

    private fun updateData(checkedDetPen: List<TPosDetailPenerimaan>) {
        for (i in checkedDetPen){
            i.isDone = 1
            daoSession.tPosDetailPenerimaanDao.update(i)
        }

        val dialog = Dialog(this@DetailPenerimaanActivity)
        dialog.setContentView(R.layout.popup_complaint);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        txtMessage.text = "Material berhasil diterima tanpa pemeriksaan"

        btnOk.setOnClickListener {
            dialog.dismiss();
            onBackPressed()
            finish()
        }
        dialog.show();
    }

    private fun validComplaint() {
        for (i in listDetailPen){
            Log.d("checkList", i.status)
            if (i.status == "SESUAI"){
                Toast.makeText(this@DetailPenerimaanActivity, "Tidak boleh melakukan komplain dengan status sesuai atau kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        startActivity(Intent(this@DetailPenerimaanActivity, ComplaintActivity::class.java)
            .putExtra("noDo", noDo))
    }

    private fun openScanner(typeScanning: Int) {
        val scan = ScanOptions()
        scan.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        scan.setCameraId(0)
        scan.setBeepEnabled(true)
        scan.setBarcodeImageEnabled(true)
        scan.captureActivity = CustomScanActivity::class.java
        when(typeScanning){
            1 -> barcodeLauncherPackaging.launch(scan)
            2 -> barcodeLauncherSn.launch(scan)
        }
    }

    private val barcodeLauncherPackaging = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        try {
            if(!result.contents.isNullOrEmpty()){
                Log.i("hit barcode","${result.contents}")
                val listPackagings = daoSession.tPosDetailPenerimaanDao.queryBuilder().where(TPosDetailPenerimaanDao.Properties.NoPackaging.eq(result.contents)).list()
                Log.d("listPackaging", listPackagings.size.toString())
                for (i in listPackagings){
                    i.status = "SESUAI"
                    i.isChecked = 1
                    daoSession.tPosDetailPenerimaanDao.update(i)
                }
                adapter.setData(listDetailPen)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    private val barcodeLauncherSn = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        try {
            if(!result.contents.isNullOrEmpty()){
                Log.i("hit barcode","${result.contents}")
                val listSns = daoSession.tPosDetailPenerimaanDao.queryBuilder()
                    .where(TPosDetailPenerimaanDao.Properties.SerialNumber.eq(result.contents)).limit(1).unique()
                Log.i("hit sns", listSns.toString())

                listSns.status = "SESUAI"
                listSns.isChecked = 1
                daoSession.tPosDetailPenerimaanDao.update(listSns)

                adapter.setData(listDetailPen)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    override fun setLoading(show: Boolean, title: String, message: String) {
        try {
            if (progressDialog != null){
                if (show) {
                    progressDialog!!.apply { show() }
                } else {
                    progressDialog!!.dismiss()
                }
            }

        } catch (e: Exception) {
            progressDialog!!.dismiss()
            e.printStackTrace()
        }
    }

    override fun setFinish(result: Boolean, message: String) {
        if (result) {
            Log.i("finish","Yes")
        }
        startActivity(Intent(this@DetailPenerimaanActivity, PenerimaanActivity::class.java ))
        finish()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}