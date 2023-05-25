package dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
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
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityPemeriksaanDetailBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pemeriksaan.complaint_pemeriksaan.ComplaintPemeriksaanActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList

class PemeriksaanDetailActivity : AppCompatActivity(), Loadable {
    private lateinit var binding: ActivityPemeriksaanDetailBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: DetailPemeriksaanAdapter
    private lateinit var listSns: MutableList<TPosSns>
    private lateinit var listPemDetail: MutableList<TPemeriksaanDetail>
    private lateinit var pemeriksaan: TPemeriksaan
    private var progressDialog: AlertDialog? = null
    private var noPem: String = ""
    private var noDo: String = ""
    private var totalCacat = 0
    private var totalNormal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noPem = intent.getStringExtra("noPemeriksaan")!!
        noDo = intent.getStringExtra("noDo")!!

        listSns = daoSession.tPosSnsDao.queryBuilder().where(TPosSnsDao.Properties.NoDoSmar.eq(noDo)).list()
        Log.d("checkSns", listSns.size.toString())
        listPemDetail = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .list()
        pemeriksaan = daoSession.tPemeriksaanDao.queryBuilder()
            .where(TPemeriksaanDao.Properties.NoPemeriksaan.eq(noPem)).limit(1).unique()

        adapter = DetailPemeriksaanAdapter(arrayListOf(), object : DetailPemeriksaanAdapter.OnAdapterListenerNormal{
            override fun onClick(po: Boolean) {
                if (po) {
                    totalNormal++
                    binding.tvTotalCacat.text = "${totalCacat} Cacat"
                    binding.tvTotalNormal.text = "${totalNormal} Normal"
                }else{
                    totalNormal--
                    binding.tvTotalCacat.text = "${totalCacat} Cacat"
                    binding.tvTotalNormal.text = "${totalNormal} Normal"
                }
            }

        },object : DetailPemeriksaanAdapter.OnAdapterListenerCacat{
            override fun onClick(po: Boolean) {
                if (po) {
                    totalCacat++
                    binding.tvTotalCacat.text = "${totalCacat} Cacat"
                    binding.tvTotalNormal.text = "${totalNormal} Normal"
                }else{
                    totalCacat--
                    binding.tvTotalCacat.text = "${totalCacat} Cacat"
                    binding.tvTotalNormal.text = "${totalNormal} Normal"
                }
            }

        },daoSession)

        adapter.setPedList(listPemDetail)

        with(binding){
            tvTotalData.text = "Total: ${listPemDetail.size} data"

            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@PemeriksaanDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvListSn.setHasFixedSize(true)

            txtKurirPengiriman.text = pemeriksaan.namaEkspedisi
            txtTglKirim.text = "Tgl ${pemeriksaan.createdDate}"
            txtPetugasPenerima.text = pemeriksaan.petugasPenerima
            txtDeliveryOrder.text = pemeriksaan.noDoSmar
            txtNamaKurir.text = pemeriksaan.namaKurir
            txtDiujikanUji.text = "Uji Komponen"
            txtTotalPackaging.text = pemeriksaan.total
            txtPending.text = "-"

            btnScanPackaging.setOnClickListener {
                openScanner(1)
            }

            btnScanSn.setOnClickListener {
                openScanner(2)
            }

            srcNoSn.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val listSnsFilter = daoSession.tPemeriksaanDetailDao.queryBuilder()
                        .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
                        .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
                        .whereOr(TPemeriksaanDetailDao.Properties.Sn.like("%"+s.toString()+"%"),
                            TPemeriksaanDetailDao.Properties.NoPackaging.like("%"+s.toString()+"%"))
                        .list()
                    adapter.setPedList(listSnsFilter)
                }

            })

            cbNormal.setOnCheckedChangeListener { buttonView, isChecked ->
                cbCacat.isEnabled = !isChecked
                if (isChecked){
                    for (i in listPemDetail){
                        i.statusPemeriksaan = "NORMAL"
                        i.isChecked = 1
                        daoSession.update(i)
                    }

                    tvTotalCacat.text = "${0} Cacat"
                    tvTotalNormal.text = "${listPemDetail.size} Normal"
                    adapter.setPedList(listPemDetail)
                }else{
                    for (i in listPemDetail){
                        i.statusPemeriksaan = ""
                        i.isChecked = 0
                        daoSession.update(i)
                    }
                    tvTotalCacat.text = "${0} Cacat"
                    tvTotalNormal.text = "${0} Normal"
                    adapter.setPedList(listPemDetail)
                }
            }

            cbCacat.setOnCheckedChangeListener { buttonView, isChecked ->
                cbNormal.isEnabled = !isChecked
                if (isChecked){
                    for (i in listPemDetail){
                        i.statusPemeriksaan = "CACAT"
                        i.isChecked = 1
                        daoSession.update(i)
                    }
                    tvTotalCacat.text = "${listPemDetail.size} Cacat"
                    tvTotalNormal.text = "${0} Normal"
                    adapter.setPedList(listPemDetail)
                }else{
                    for (i in listPemDetail){
                        i.statusPemeriksaan = ""
                        i.isChecked = 0
                        daoSession.update(i)
                    }
                    tvTotalCacat.text = "${0} Cacat"
                    tvTotalNormal.text = "${0} Normal"
                    adapter.setPedList(listPemDetail)
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
        val data = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .list()

        if (data.size == 0){
            Toast.makeText(this@PemeriksaanDetailActivity, "Tidak boleh terima dengan status kosong", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in data){
            Log.d("checkList", i.statusPemeriksaan)
            if (i.statusPemeriksaan == "CACAT"){
                Toast.makeText(this@PemeriksaanDetailActivity, "Tidak boleh terima dengan status cacat", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val dialog = Dialog(this@PemeriksaanDetailActivity)
        dialog.setContentView(R.layout.popup_validation);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.txt_message) as TextView

        message.text = "Apakah anda yakin untuk menyelesaikan pemeriksaan"

        btnTidak.setOnClickListener {
            dialog.dismiss();
        }

        btnYa.setOnClickListener {
            updateData()
            dialog.dismiss()
        }

        dialog.show();
    }

    private fun updateData() {
        var checkIsDone = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsDone.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .list()

        var checkSnDiterima = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1))
            .where(TPemeriksaanDetailDao.Properties.StatusPemeriksaan.notEq("DITERIMA"))
            .list()

        var checkSnKomplain = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1))
            .list()

        var listPemDetailChecked = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsDone.eq(0))
            .list()

        var packagings = ""
        for (i in listPemDetailChecked){
            packagings += "${i.noPackaging},${i.sn},${i.noMaterail},${i.statusPemeriksaan},${pemeriksaan.doLineItem};"
            Log.i("noPackaging", i.noPackaging)

        }

        if (packagings != "") {
            packagings = packagings.substring(0, packagings.length - 1)
        }

        for (i in listPemDetail.filter { it.isChecked == 1 }){
            i.isDone = 1
            daoSession.tPemeriksaanDetailDao.update(i)
        }

        if (checkSnDiterima.size == 0){
            pemeriksaan.statusPemeriksaan = "SELESAI"
            pemeriksaan.isDone = 1
            daoSession.tPemeriksaanDao.update(pemeriksaan)
        }

        val dialog = Dialog(this@PemeriksaanDetailActivity)
        dialog.setContentView(R.layout.popup_complaint)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        txtMessage.text = "Data sudah berhasil diterima"

        btnOk.setOnClickListener {
            dialog.dismiss();
            pemeriksaan.isDone = 1
            daoSession.tPemeriksaanDao.update(pemeriksaan)

            if (checkSnKomplain.isNullOrEmpty()){
                val updatePenerimaan = daoSession.tPosPenerimaanDao.queryBuilder()
                    .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).list().get(0)
                updatePenerimaan.isRating = 1
                updatePenerimaan.statusPemeriksaan = "SELESAI"
                daoSession.tPosPenerimaanDao.update(updatePenerimaan)
            }

            if (checkIsDone.size == listPemDetail.size){
                val updatePenerimaan = daoSession.tPosPenerimaanDao.queryBuilder()
                    .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).list().get(0)
                updatePenerimaan.statusPemeriksaan = "SELESAI"
                daoSession.tPosPenerimaanDao.update(updatePenerimaan)

                pemeriksaan.statusPemeriksaan = "SELESAI"
                daoSession.tPemeriksaanDao.update(pemeriksaan)
            }

            submitForm(packagings)
            startActivity(Intent(this@PemeriksaanDetailActivity, PemeriksaanActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        dialog.show();
    }

    private fun submitForm(packagings: String) {
        var quantity = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsDone.eq(0))
            .list().size

        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //region Add report visit to queue
        var jwtWeb = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity, "jwtWeb", "")
        var jwtMobile = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity,"jwt","")
        var jwt = "$jwtMobile;$jwtWeb"
        Log.d("nih jwt nya",jwt)
        var username = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemeriksaan" + username + "_" + noDo + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data Dokumen Pemeriksaan"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "no_do_smar", noDo!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "sns", packagings, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "status", "DITERIMA", ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "qty", quantity.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "tgl_terima", pemeriksaan.tanggalDiterima, ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "plant_code_no", pemeriksaan.planCodeNo, ReportParameter.TEXT))
        params.add(ReportParameter("7", reportId, "username", username!!, ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendPemeriksaan(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)
    }

    private fun validComplaint() {
        val data = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
            .list()

        if(data.isNullOrEmpty()){
            Toast.makeText(this@PemeriksaanDetailActivity, "Tidak boleh melakukan komplain dengan status kosong", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in data){
            Log.d("checkList", i.statusPemeriksaan)
            if (i.statusPemeriksaan == "NORMAL"){
                Toast.makeText(this@PemeriksaanDetailActivity, "Tidak boleh melakukan komplain dengan status normal", Toast.LENGTH_SHORT).show()
                return
            }
        }

        startActivity(Intent(this@PemeriksaanDetailActivity, ComplaintPemeriksaanActivity::class.java)
            .putExtra("noDo", noDo)
            .putExtra("noPemeriksaan", noPem))
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
                val listPackagings = daoSession.tPemeriksaanDetailDao.queryBuilder().where(TPemeriksaanDetailDao.Properties.NoPackaging.eq(result.contents)).list()
                Log.d("listPackaging", listPackagings.size.toString())
                for (i in listPackagings){
                    i.statusPemeriksaan = "NORMAL"
                    i.isChecked = 1
                    daoSession.tPemeriksaanDetailDao.update(i)
                }
                adapter.setPedList(listPemDetail)
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
                val listSns = daoSession.tPemeriksaanDetailDao.queryBuilder()
                    .where(TPemeriksaanDetailDao.Properties.Sn.eq(result.contents)).limit(1).unique()
                Log.i("hit sns", listSns.toString())

                listSns.statusPemeriksaan = "NORMAL"
                listSns.isChecked = 1
                daoSession.tPemeriksaanDetailDao.update(listSns)

                adapter.setPedList(listPemDetail)
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

    override fun onBackPressed() {
        val dialog = Dialog(this@PemeriksaanDetailActivity)
        dialog.setContentView(R.layout.popup_validation)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.message) as TextView
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView
        val icon = dialog.findViewById(R.id.imageView11) as ImageView

        message.text = "Yakin untuk keluar?"
        txtMessage.text = "Jika ya maka data tidak akan tersimpan"
        icon.setImageResource(R.drawable.ic_warning)

        btnTidak.setOnClickListener {
            dialog.dismiss();
        }

        btnYa.setOnClickListener {
            super.onBackPressed()
            for (i in listPemDetail){
//                i.statusPemeriksaan = ""
                i.isChecked = 0
                daoSession.update(i)
            }

            dialog.dismiss()
        }

        dialog.show();
    }

    override fun setFinish(result: Boolean, message: String) {
        if (result) {
            Log.i("finish","Yes")
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()    }
}