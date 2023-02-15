package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.CameraXActivity
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
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.transmission_history.TransmissionActivity
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import dev.iconpln.mims.utils.StorageUtils
import org.joda.time.DateTime
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class DetailPenerimaanActivity : AppCompatActivity(),Loadable {
    private lateinit var daoSession: DaoSession
    private lateinit var session: SessionManager
    private lateinit var binding: ActivityDetailPenerimaanBinding
    private lateinit var adapter: DetailPenerimaanAdapter
    private val cameraRequestFotoBarang = 101
    private val cameraRequestFotoBarangGallery = 102
    private val cameraRequestFotoSuratBarang = 103
    private val cameraRequestFotoSuratBarangGallery = 104
    private var filePathFotoBarang: String = ""
    private var filePathFotoSuratBarang: String = ""
    private lateinit var cal: Calendar
    private var noDo: String = ""
    private lateinit var data: TPosPenerimaan
    private lateinit var packagingList: List<TPosDetailPenerimaan>
    private lateinit var dataDetailPenerimaan: TPosDetailPenerimaan

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()
        session = SessionManager(this)

        noDo = intent.getStringExtra("do").toString()

        data = daoSession.tPosPenerimaanDao.queryBuilder().where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()
        dataDetailPenerimaan = daoSession.tPosDetailPenerimaanDao.queryBuilder().where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        packagingList = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.NoPemeriksaan.eq("")).list()
        Log.d("checkSizePackagingList", packagingList.size.toString())

        if (packagingList.isEmpty()){
            Toast.makeText(this@DetailPenerimaanActivity, "data packaging tidak ditemukan", Toast.LENGTH_SHORT).show()
            binding.btnSimpan.isEnabled = false
        }

        adapter = DetailPenerimaanAdapter(arrayListOf(), object : DetailPenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetailPenerimaan) {}

        }, daoSession)

        with(binding){
            recyclerView2.adapter = adapter
            recyclerView2.setHasFixedSize(true)
            recyclerView2.layoutManager = LinearLayoutManager(this@DetailPenerimaanActivity, LinearLayoutManager.VERTICAL, false)

            btnBack.setOnClickListener { onBackPressed() }

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                edtTanggalDiterima.setText(sdf.format(cal.time))

            }

            edtTanggalDiterima.setOnClickListener{
                DatePickerDialog(this@DetailPenerimaanActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            btnFotoSuratBarang.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this@DetailPenerimaanActivity, arrayOf(Manifest.permission.CAMERA), cameraRequestFotoSuratBarang)

                val dialog = BottomSheetDialog(this@DetailPenerimaanActivity, R.style.AppBottomSheetDialogTheme)
                val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
                var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
                var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

                btnCamera.setOnClickListener {
//                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(cameraIntent, cameraRequestFotoSuratBarang)
                    val intent = Intent(this@DetailPenerimaanActivity, CameraXActivity::class.java)
                        .putExtra("fotoName", "fotoSuratBarang")
                    startActivityForResult(intent,cameraRequestFotoSuratBarang)
                    dialog.dismiss()
                }

                btnGallery.setOnClickListener {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, cameraRequestFotoSuratBarangGallery)
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
            }

            btnFotoBarang.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this@DetailPenerimaanActivity, arrayOf(Manifest.permission.CAMERA), cameraRequestFotoSuratBarang)

                val dialog = BottomSheetDialog(this@DetailPenerimaanActivity, R.style.AppBottomSheetDialogTheme)
                val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
                var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
                var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

                btnCamera.setOnClickListener {
//                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(cameraIntent, cameraRequestFotoBarang)

                    val intent = Intent(this@DetailPenerimaanActivity, CameraXActivity::class.java)
                        .putExtra("fotoName", "fotoBarang")
                    startActivityForResult(intent,cameraRequestFotoBarang)
                    dialog.dismiss()
                }

                btnGallery.setOnClickListener {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, cameraRequestFotoBarangGallery)
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
            }

            barcode2.setOnClickListener {
                openScanner()
            }

            btnSimpan.setOnClickListener {
                validatete()
            }
            txtPrimaryOrder.text = dataDetailPenerimaan.noDoMims
            txtNoDo.text = dataDetailPenerimaan.noDoSmar
            txtPlant.text = dataDetailPenerimaan.plantName
            txtTlsk.text = data.tlskNo
            txtUnit.text = dataDetailPenerimaan.uom
            txtKurirPengiriman.text = data.namaKurir
            txtPetugasPengiriman.text = data.courierPersonName
            txtStoreloc.text = dataDetailPenerimaan.storLoc
            txtTglKirim.text = dataDetailPenerimaan.createdDate
            txtVendor.text = dataDetailPenerimaan.plantName
        }

        setPackagingList()
        setCardData()
    }

    private fun validatete() {
        if (filePathFotoSuratBarang.isNullOrEmpty()){
            Toast.makeText(this, "Ambil foto barang terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (filePathFotoBarang.isNullOrEmpty()){
            Toast.makeText(this, "Ambil foto surat barang terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.edtEkspedisi.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Harap lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.edtPetugasPenerima.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Harap lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.edtTanggalDiterima.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Harap lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.edtNamaKurir.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Harap lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        submitForm()
    }

    private fun submitForm() {
        val reports = java.util.ArrayList<GenericReport>()
        val list = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(noDo))
            .where(TPosDetailPenerimaanDao.Properties.NoPemeriksaan.eq(""))
            .where(TPosDetailPenerimaanDao.Properties.IsChecked.eq(1)).list()
        var packagings = ""
        for (i in list){
            packagings += "${i.noPackaging},"
            Log.i("noPackaging", i.noPackaging)

        }
        if (packagings != "") {
            packagings = packagings.substring(0, packagings.length - 1)
        }

        with(binding){
            val noPackagings = packagingList.filter { it.isChecked == 1 }.size
            data.petugasPenerima = edtPetugasPenerima.text.toString()
            data.namaEkspedisi = edtEkspedisi.text.toString()
            data.namaKurir = edtNamaKurir.text.toString()
            data.photoSuratBarang = filePathFotoSuratBarang
            data.photoBarang = filePathFotoBarang
            data.tanggalDiterima = edtTanggalDiterima.text.toString()
            Log.d("noPackagings", noPackagings.toString())
            if (noPackagings == packagingList.size){
                data.isDone = 1
            }else{
                data.isDone = 0
            }
            daoSession.update(data)

            val noPemeriksaan = "P.${data.noDoSmar}.${ DateTime.now().toString("yyMMddHHmmssSS")}"

            if (packagingList.isNotEmpty()){
               for (i in packagingList){
                   if (i.isChecked == 1){
                       i.noPemeriksaan = noPemeriksaan
                       daoSession.tPosDetailPenerimaanDao.update(i)
                   }
               }
            }


            var item = TPemeriksaan()
            item.noPemeriksaan = noPemeriksaan
            item.createdDate = data.createdDate
            item.leadTime = data.leadTime
            item.storloc = data.storLoc
            item.noDoSmar = data.noDoSmar
            item.planCodeNo = data.planCodeNo
            item.plantName = data.plantName
            item.poMpNo = data.poMpNo
            item.poSapNo = data.poSapNo
            item.storLoc = data.storLoc
            item.tlskNo = data.tlskNo
            item.total = data.total
            item.kdPabrikan = data.kdPabrikan
            item.materialGroup = data.materialGroup
            item.namaKategoriMaterial = data.namaKategoriMaterial
            item.noDoMims = data.noDoMims
            item.tanggalDiterima = data.tanggalDiterima
            item.namaKurir = data.namaKurir
            item.namaEkspedisi = data.namaEkspedisi
            item.petugasPenerima = data.petugasPenerima

            //baru
            item.namaKetua = ""
            item.namaManager = ""
            item.namaSekretaris = ""
            item.anggota = ""
            item.ratingPenerimaan = ""
            item.descPenerimaan = ""
            item.ratingQuality = ""
            item.descQuality = ""
            item.ratingWaktu = ""
            item.descWaktu = ""
            item.ratingPath = ""
            item.packangings = packagings
            item.state = 1
            item.isDone = 0
            daoSession.insert(item)

            var jwt = SharedPrefsUtils.getStringPreference(this@DetailPenerimaanActivity,"jwt","")
            var username = SharedPrefsUtils.getStringPreference(this@DetailPenerimaanActivity, "username","")
            var email = SharedPrefsUtils.getStringPreference(this@DetailPenerimaanActivity, "email","")
            val currentDate = DateTime.now().toString("yyyy-MM-dd")
            val reportId = "Penerimaan" + DateTime.now().toString("yMdHmsSSS")
            val reportName = "Penerimaan"
            val reportDescription = "Penerimaan-${item.noDoSmar}-${item.packangings}-${DateTime.now().toString("yyyy-MM-dd")}"
            val params = ArrayList<ReportParameter>()
            params.add(ReportParameter("1", reportId, "plant_code_no", item.planCodeNo, ReportParameter.TEXT ))
            params.add(ReportParameter("2", reportId, "no_do_smar", item.noDoSmar, ReportParameter.TEXT ))
            params.add(ReportParameter("3", reportId, "no_mat_sap", dataDetailPenerimaan.noMatSap, ReportParameter.TEXT ))
            params.add(ReportParameter("4", reportId, "penerima", item.petugasPenerima, ReportParameter.TEXT ))
            params.add(ReportParameter("5", reportId, "tanggal", item.tanggalDiterima, ReportParameter.TEXT ))
            params.add(ReportParameter("6", reportId, "kurir", item.namaKurir, ReportParameter.TEXT ))
            params.add(ReportParameter("7", reportId, "ekspedisi", item.namaEkspedisi, ReportParameter.TEXT ))
            params.add(ReportParameter("8", reportId, "quantity", dataDetailPenerimaan.qty, ReportParameter.TEXT ))
            params.add(ReportParameter("9", reportId, "username", username!!, ReportParameter.TEXT ))
            params.add(ReportParameter("10", reportId, "email",email!! , ReportParameter.TEXT ))
            params.add(ReportParameter("11", reportId, "no_packagings", packagings, ReportParameter.TEXT ))
            params.add(ReportParameter("12", reportId, "photo_file", filePathFotoBarang, ReportParameter.FILE ))
            params.add(ReportParameter("13", reportId, "photo_file2", filePathFotoSuratBarang, ReportParameter.FILE ))
            val reportPenerimaan = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendPenerimaan(), currentDate, 0, 11119209101, params)
            reports.add(reportPenerimaan)

        }

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(this, ReportUploader::class.java)
        startService(iService)
    }

    private fun setCardData() {}

    private fun setPackagingList() {
        adapter.setPoList(packagingList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == cameraRequestFotoSuratBarangGallery){
            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoSuratBarang${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.idFileName.setImageBitmap(bitmap)
            filePathFotoSuratBarang = file.toString()

        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFotoBarangGallery){
            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoBarang${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.idFileNameBarang.setImageBitmap(bitmap)
            filePathFotoBarang = file.toString()
        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFotoSuratBarang){
            val mPhotoCompetitorPath = data?.getStringExtra("Path")

            binding.idFileName.setImageBitmap(BitmapFactory.decodeFile(mPhotoCompetitorPath))
            filePathFotoSuratBarang = mPhotoCompetitorPath.toString()

        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFotoBarang){
            val mPhotoCompetitorPath = data?.getStringExtra("Path")

            binding.idFileNameBarang.setImageBitmap(BitmapFactory.decodeFile(mPhotoCompetitorPath))
            filePathFotoBarang = mPhotoCompetitorPath.toString()
        }else{
            Log.d("cancel", "cacelPhoto")
        }
    }

    override fun setLoading(show: Boolean, title: String, message: String) {}

    override fun setFinish(result: Boolean, message: String) {
        if (result){
            startActivity(Intent(this, PenerimaanActivity::class.java))
            finish()
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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

                val data = daoSession.tPosDetailPenerimaanDao.queryBuilder().where(TPosDetailPenerimaanDao.Properties.Barcode.eq(result.contents)).limit(1).unique()
                data.isChecked = 1
                daoSession.update(data)

                adapter.setPoList(packagingList)
                Toast.makeText(this@DetailPenerimaanActivity, "Scanning success : ${result.contents}",Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Log.e("checkException", e.toString())
        }
    }
}