package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
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
import java.util.Random
import java.util.UUID


class DetailPenerimaanActivity : AppCompatActivity(),Loadable {
    private lateinit var daoSession: DaoSession
    private var progressDialog: AlertDialog? = null
    private lateinit var binding: ActivityDetailPenerimaanBinding
    private val viewModel: PenerimaanViewModel by viewModels()
    private lateinit var adapter: DetailPenerimaanAdapter
    private lateinit var listDetailPen: MutableList<TPosDetailPenerimaan>
    private lateinit var penerimaan: TPosPenerimaan
    private var noDo: String = ""
    private var partialCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noDo = intent.getStringExtra("noDo")!!
        partialCode = "${noDo}${UUID.randomUUID()}"

        listDetailPen = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0)).list()

        penerimaan = daoSession.tPosPenerimaanDao.queryBuilder()
            .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        adapter = DetailPenerimaanAdapter(arrayListOf(), object : DetailPenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetailPenerimaan) {}},daoSession,partialCode)

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
                        i.statusPenerimaan = "SESUAI"
                        i.isChecked = 1
                        i.partialCode = partialCode
                        daoSession.update(i)
                    }
                    Log.d("partialCode", partialCode)
                    adapter.setData(listDetailPen)
                }else{
                    for (i in listDetailPen){
                        i.statusPenerimaan = ""
                        i.isChecked = 0
                        i.partialCode = ""
                        daoSession.update(i)
                    }
                    Log.d("partialCode", partialCode)
                    adapter.setData(listDetailPen)
                }
            }

            cbTidakSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                cbSesuai.isEnabled = !isChecked
                if (isChecked){
                    for (i in listDetailPen){
                        i.statusPenerimaan = "TIDAK SESUAI"
                        i.isChecked = 1
                        daoSession.update(i)
                    }
                    adapter.setData(listDetailPen)
                }else{
                    for (i in listDetailPen){
                        i.statusPenerimaan = ""
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
        var data = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0))
            .where(TPosDetailPenerimaanDao.Properties.IsChecked.eq(1)).list()

        Log.d("size", data.size.toString())

        if (data.size > 0){
            for (i in data){
                Log.d("checkList", i.statusPenerimaan)
                if (i.statusPenerimaan == "TIDAK SESUAI"){
                    Toast.makeText(this@DetailPenerimaanActivity, "Tidak boleh terima dengan status tidak sesuai", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }else{
            Toast.makeText(this@DetailPenerimaanActivity, "Tidak boleh terima dengan status kosong", Toast.LENGTH_SHORT).show()
            return
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
        var checkedDetPen = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsChecked.eq(1))
            .where(TPosDetailPenerimaanDao.Properties.IsComplaint.eq(0)).list()

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
        params.add(ReportParameter("2", reportId, "plant_code_no", penerimaan.planCodeNo, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "plant_code_no", currentDate, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "quantity", checkedDetPen.size.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "is_periksa", isPeriksa.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "sns", sns, ReportParameter.TEXT))
        params.add(ReportParameter("7", reportId, "username", username!!, ReportParameter.TEXT))
        params.add(ReportParameter("8", reportId, "email", username, ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendPenerimaan(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)

        for (i in checkedDetPen){
            i.isDone = 1
            daoSession.tPosDetailPenerimaanDao.update(i)
        }

        var noPrk = "PRK${noDo}${currentDateTime}"
        if (isPeriksa == 1){

            var item = TPemeriksaan()
            item.isDone = 0
            item.createdDate = penerimaan.createdDate
            item.namaKurir = penerimaan.kurirPengantar
            item.kdPabrikan = penerimaan.kdPabrikan
            item.materialGroup = penerimaan.materialGroup
            item.namaEkspedisi = penerimaan.expeditions
            item.petugasPenerima = penerimaan.petugasPenerima
            item.tanggalDiterima = penerimaan.tanggalDiterima
            item.namaKategoriMaterial = penerimaan.namaKategoriMaterial
            item.courierPersonName = penerimaan.courierPersonName
            item.expeditions = penerimaan.expeditions
            item.doStatus = penerimaan.doStatus
            item.noDoMims = penerimaan.noDoMims
            item.plantName = penerimaan.plantName
            item.planCodeNo = penerimaan.planCodeNo
            item.leadTime = penerimaan.leadTime
            item.noDoSmar = penerimaan.noDoSmar
            item.poMpNo = penerimaan.poMpNo
            item.poSapNo = penerimaan.poSapNo
            item.tlskNo = penerimaan.tlskNo
            item.total = penerimaan.total
            item.storLoc = penerimaan.storloc
            item.noPemeriksaan = noPrk
            item.doLineItem = penerimaan.doLineItem

            item.namaKetua = ""
            item.namaManager = ""
            item.namaSekretaris = ""
            item.namaAnggota = ""
            item.namaAnggotaBaru = ""

            daoSession.tPemeriksaanDao.insert(item)

            val detailListlistToInsert = daoSession.tPosDetailPenerimaanDao.queryBuilder()
                .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
                .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(1))
                .where(TPosDetailPenerimaanDao.Properties.PartialCode.eq(partialCode))
                .where(TPosDetailPenerimaanDao.Properties.IsComplaint.eq(0)).list()

            val size = detailListlistToInsert.size
            if (size > 0) {
                val items = arrayOfNulls<TPemeriksaanDetail>(size)
                var item: TPemeriksaanDetail
                for ((i, model) in detailListlistToInsert.withIndex()){
                    item = TPemeriksaanDetail()
                    item.isDone = 0
                    item.isChecked = 0
                    item.statusPenerimaan = model.statusPenerimaan
                    item.statusPemeriksaan = ""
                    item.sn = model.serialNumber
                    item.noDoSmar = model.noDoSmar
                    item.kategori = model.namaKategoriMaterial
                    item.noMaterail = model.noMaterial
                    item.noPackaging = model.noPackaging
                    item.noPemeriksaan = noPrk
                    item.isComplaint = model.isComplaint
                    item.isPeriksa = 1

                    items[i] = item
                }
                daoSession.tPemeriksaanDetailDao.insertInTx(items.toList())
            }
        }

        updateData()
    }

    private fun updateData() {
        val checkedList = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(1)).list()

        val checkSnKomplaint = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(1))
            .where(TPosDetailPenerimaanDao.Properties.IsComplaint.eq(1)).list().size > 0

        val checkListPemeriksaan = daoSession.tPemeriksaanDao.queryBuilder()
            .where(TPemeriksaanDao.Properties.NoDoSmar.eq(noDo)).list().size > 0

        if (checkedList.size == listDetailPen.size){

            if (checkListPemeriksaan){
                if (checkSnKomplaint){
                    penerimaan.statusPenerimaan = "DITERIMA"
                    penerimaan.isRating = 0
                    daoSession.tPosPenerimaanDao.update(penerimaan)
                }else{
                    penerimaan.statusPenerimaan = "DITERIMA"
                    penerimaan.isRating = 0
                    daoSession.tPosPenerimaanDao.update(penerimaan)
                }
            }else{
                if (checkSnKomplaint){
                    penerimaan.statusPenerimaan = "DITERIMA"
                    penerimaan.isRating = 0
                    daoSession.tPosPenerimaanDao.update(penerimaan)
                }else{
                    penerimaan.statusPenerimaan = "DITERIMA"
                    penerimaan.isRating = 1
                    penerimaan.statusPemeriksaan = "SUDAH DIPERIKSA"
                    daoSession.tPosPenerimaanDao.update(penerimaan)
                }
            }
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
            startActivity(Intent(this@DetailPenerimaanActivity, PenerimaanActivity::class.java )
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        dialog.show();
    }

    private fun validComplaint() {
        var data = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0))
            .where(TPosDetailPenerimaanDao.Properties.IsChecked.eq(1)).list()

        if (data.size > 0){
            for (i in data){
                Log.d("checkList", i.statusPenerimaan)
                if (i.statusPenerimaan == "SESUAI"){
                    Toast.makeText(this@DetailPenerimaanActivity, "Tidak boleh melakukan komplain dengan status sesuai atau kosong", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
        else{
            Toast.makeText(this@DetailPenerimaanActivity, "Harap pilih serial number yang tidak sesuai", Toast.LENGTH_SHORT).show()
            return
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
                    i.statusPenerimaan = "SESUAI"
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

                listSns.statusPenerimaan = "SESUAI"
                listSns.isChecked = 1
                daoSession.tPosDetailPenerimaanDao.update(listSns)

                adapter.setData(listDetailPen)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    override fun onBackPressed() {
        val dialog = Dialog(this@DetailPenerimaanActivity)
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
            for (i in listDetailPen){
                i.statusPenerimaan = ""
                i.isChecked = 0
                i.partialCode = ""
                daoSession.update(i)
            }

            dialog.dismiss()
        }

        dialog.show();
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}