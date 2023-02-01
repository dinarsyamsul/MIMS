package dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import dev.iconpln.mims.databinding.ActivityPemeriksaanDetailBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.utils.SharedPrefsUtils
import dev.iconpln.mims.utils.StorageUtils
import org.joda.time.DateTime
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PemeriksaanDetailActivity : AppCompatActivity(), Loadable {
    private lateinit var binding: ActivityPemeriksaanDetailBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: DetailPemeriksaanAdapter

    private lateinit var data: TPemeriksaanDetail
    private lateinit var dataPemeriksaan: TPosDetailPenerimaan
    private lateinit var arrayStringPackaging: List<String>

    private val cameraRequestKomplain = 101
    private val cameraRequestKomplainGallery = 102
    private val cameraRequestReject = 103
    private val cameraRequestRejectGallery = 104
    private var filePathKomplain: String = ""
    private var filePathReject: String = ""

    private var reasonReject: String = ""
    private var reasonKomplain: String = ""

    private var noDo: String = ""
    private var mAnggota: String = ""
    private var mKetua: String = ""
    private var mManager: String = ""
    private var mSekretaris: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noDo = intent.getStringExtra("noDo")!!

        data = daoSession.tPemeriksaanDetailDao.queryBuilder().where(TPemeriksaanDetailDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()
        dataPemeriksaan = daoSession.tPosDetailPenerimaanDao.queryBuilder().where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        arrayStringPackaging = data.noPackaging.split(",")

        adapter = DetailPemeriksaanAdapter(arrayListOf(),object : DetailPemeriksaanAdapter.OnAdapterListener{
            override fun onClick(po: String) {}

        }, daoSession)

        adapter.setPedList(arrayStringPackaging)

        with(binding){
            btnLanjut.setOnClickListener {
                validation()
            }

            btnKomplain.setOnClickListener {
                constraintDetail2.visibility = View.GONE
                constraintKomplain.visibility = View.VISIBLE
            }

            btnReject.setOnClickListener {
                constraintDetail2.visibility = View.GONE
                constraintReject.visibility = View.VISIBLE
            }

            btnMenyetujui.setOnClickListener {
                submitForm("APPROVED")
            }

            btnSelesaiReject.setOnClickListener {
                validateReject()
            }

            btnSelesaiKomplain.setOnClickListener {
                validateKomplain()
            }

            btnFotoDocKomplain.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this@PemeriksaanDetailActivity, arrayOf(
                        Manifest.permission.CAMERA), cameraRequestKomplain)

                val dialog = BottomSheetDialog(this@PemeriksaanDetailActivity, R.style.AppBottomSheetDialogTheme)
                val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
                var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
                var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

                btnCamera.setOnClickListener {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, cameraRequestKomplain)
                    dialog.dismiss()
                }

                btnGallery.setOnClickListener {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, cameraRequestKomplainGallery)
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
            }

            btnFotoDocReject.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this@PemeriksaanDetailActivity, arrayOf(
                        Manifest.permission.CAMERA), cameraRequestReject)

                val dialog = BottomSheetDialog(this@PemeriksaanDetailActivity, R.style.AppBottomSheetDialogTheme)
                val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
                var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
                var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

                btnCamera.setOnClickListener {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, cameraRequestReject)
                    dialog.dismiss()
                }

                btnGallery.setOnClickListener {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, cameraRequestRejectGallery)
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
            }

            barcode2.setOnClickListener { openScanner() }

            recyclerView2.adapter = adapter
            recyclerView2.setHasFixedSize(true)
            recyclerView2.layoutManager = LinearLayoutManager(this@PemeriksaanDetailActivity, LinearLayoutManager.VERTICAL, false)

            srcSerialNumber.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val filter = arrayStringPackaging.filter { it.lowercase().contains(s.toString().lowercase()) }
                    adapter.setPedList(filter)
                }

            })
        }
    }

    private fun validateReject() {

        with(binding){
            val reason = edtAlasanReject.text.toString()

            if (reason.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (filePathReject.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            reasonReject = reason
            submitForm("Reject")

        }

    }

    private fun validateKomplain() {
        with(binding){
            val reason = edtAlasanKomplain.text.toString()

            if (reason.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (filePathKomplain.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            reasonKomplain = reason
            submitFormKomplain()

        }
    }

    private fun submitFormKomplain() {
        val reports = java.util.ArrayList<GenericReport>()

        data.status = "COMPLAINT"
        data.isDone = 1
        daoSession.update(data)

        var item = TPemeriksaan()
        item.namaKetua = mKetua
        item.namaManager = mManager
        item.namaSekretaris = mSekretaris
        item.anggota = mAnggota
        item.state = 2
        daoSession.update(item)

        var jwt = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity,"jwt","")
        var username = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity, "username","")
        var email = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity, "email","")
        val currentDate = DateTime.now().toString("yyyy-MM-dd")
        val reportId = "Pemeriksaan" + DateTime.now().toString("yMdHmsSSS")
        val reportName = "Pemeriksaan"
        val reportDescription = "Pemeriksaan-${item.noDoSmar}-${item.packangings}-${DateTime.now().toString("yyyy-MM-dd")}"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "plant_code_no", item.planCodeNo, ReportParameter.TEXT ))
        params.add(ReportParameter("2", reportId, "no_do_smar", item.noDoSmar, ReportParameter.TEXT ))
        params.add(ReportParameter("3", reportId, "no_mat_sap", dataPemeriksaan.noMatSap, ReportParameter.TEXT ))
        params.add(ReportParameter("4", reportId, "penerima", item.petugasPenerima, ReportParameter.TEXT ))
        params.add(ReportParameter("5", reportId, "tanggal", item.tanggalDiterima, ReportParameter.TEXT ))
        params.add(ReportParameter("6", reportId, "kurir", item.namaKurir, ReportParameter.TEXT ))
        params.add(ReportParameter("7", reportId, "ekspedisi", item.namaEkspedisi, ReportParameter.TEXT ))
        params.add(ReportParameter("8", reportId, "quantity", dataPemeriksaan.qty, ReportParameter.TEXT ))
        params.add(ReportParameter("9", reportId, "username", username!!, ReportParameter.TEXT ))
        params.add(ReportParameter("10", reportId, "email",email!! , ReportParameter.TEXT ))
        params.add(ReportParameter("11", reportId, "no_packaging", item.packangings, ReportParameter.TEXT ))
        params.add(ReportParameter("12", reportId, "status_name", "COMPLAINT", ReportParameter.TEXT ))
        params.add(ReportParameter("13", reportId, "sns", item.packangings, ReportParameter.TEXT ))
        params.add(ReportParameter("14", reportId, "alasan", reasonKomplain, ReportParameter.TEXT ))
        params.add(ReportParameter("15", reportId, "photo_file", filePathKomplain, ReportParameter.FILE ))
        val reportPenerimaan = GenericReport(reportId, username, reportName, reportDescription, ApiConfig.sendComplaint(), currentDate, 0, 11119209101, params)
        reports.add(reportPenerimaan)

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(this@PemeriksaanDetailActivity, ReportUploader::class.java)
        startService(iService)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == cameraRequestRejectGallery){
            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoReject${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.ivDokumentasiReject.setImageBitmap(bitmap)
            filePathReject = file.toString()

        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestKomplainGallery){
            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoKomplain${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.ivDokumentasiReject.setImageBitmap(bitmap)
            filePathReject = file.toString()
        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestReject){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoReject${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.ivDokumentasiReject.setImageBitmap(bitmap)
            filePathReject = file.toString()

        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestKomplain){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoKomplain${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.ivDokumentasiKomplain.setImageBitmap(bitmap)
            filePathKomplain = file.toString()
        }else{
            Log.d("cancel", "cacelPhoto")
        }
    }

    private fun submitForm(status: String) {
        val reports = java.util.ArrayList<GenericReport>()

        data.status = status
        data.isDone = 1
        daoSession.update(data)

        var item = TPemeriksaan()
        item.namaKetua = mKetua
        item.namaManager = mManager
        item.namaSekretaris = mSekretaris
        item.anggota = mAnggota
        item.state = 2
        daoSession.update(item)

        var jwt = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity,"jwt","")
        var username = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity, "username","")
        var email = SharedPrefsUtils.getStringPreference(this@PemeriksaanDetailActivity, "email","")
        val currentDate = DateTime.now().toString("yyyy-MM-dd")
        val reportId = "Pemeriksaan" + DateTime.now().toString("yMdHmsSSS")
        val reportName = "Pemeriksaan"
        val reportDescription = "Pemeriksaan-${item.noDoSmar}-${item.packangings}-${DateTime.now().toString("yyyy-MM-dd")}"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "plant_code_no", item.planCodeNo, ReportParameter.TEXT ))
        params.add(ReportParameter("2", reportId, "no_do_smar", item.noDoSmar, ReportParameter.TEXT ))
        params.add(ReportParameter("3", reportId, "no_mat_sap", dataPemeriksaan.noMatSap, ReportParameter.TEXT ))
        params.add(ReportParameter("4", reportId, "penerima", item.petugasPenerima, ReportParameter.TEXT ))
        params.add(ReportParameter("5", reportId, "tanggal", item.tanggalDiterima, ReportParameter.TEXT ))
        params.add(ReportParameter("6", reportId, "kurir", item.namaKurir, ReportParameter.TEXT ))
        params.add(ReportParameter("7", reportId, "ekspedisi", item.namaEkspedisi, ReportParameter.TEXT ))
        params.add(ReportParameter("8", reportId, "quantity", dataPemeriksaan.qty, ReportParameter.TEXT ))
        params.add(ReportParameter("9", reportId, "username", username!!, ReportParameter.TEXT ))
        params.add(ReportParameter("10", reportId, "email",email!! , ReportParameter.TEXT ))
        params.add(ReportParameter("11", reportId, "no_packaging", item.packangings, ReportParameter.TEXT ))
        params.add(ReportParameter("12", reportId, "status_name", status, ReportParameter.TEXT ))
        params.add(ReportParameter("13", reportId, "sns", item.packangings, ReportParameter.TEXT ))
        val reportPenerimaan = GenericReport(reportId, username, reportName, reportDescription, ApiConfig.sendPemeriksaan(), currentDate, 0, 11119209101, params)
        reports.add(reportPenerimaan)

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(this@PemeriksaanDetailActivity, ReportUploader::class.java)
        startService(iService)
    }

    private fun validation() {
        with(binding){
            val anggota = edtAnggota.text.toString()
            val ketua = edtKetua.text.toString()
            val manager = edtManager.text.toString()
            val sekretaris = edtSekretaris.text.toString()

            if (anggota.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (ketua.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (manager.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (sekretaris.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Silahkan isi semua data field yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            mAnggota = anggota
            mSekretaris = sekretaris
            mKetua = ketua
            mManager = manager

            constraintDetail1.visibility = View.GONE
            constraintDetail2.visibility = View.VISIBLE

        }
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
            if (!result.contents.isNullOrEmpty()) {

                Toast.makeText(this@PemeriksaanDetailActivity, "Scanning success : ${result.contents}",Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Log.e("checkException", e.toString())
        }
    }

    override fun setLoading(show: Boolean, title: String, message: String) {

    }

    override fun setFinish(result: Boolean, message: String) {
        if (result){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            startActivity(Intent(this@PemeriksaanDetailActivity, PenerimaanActivity::class.java))
            finish()
        }
    }
}