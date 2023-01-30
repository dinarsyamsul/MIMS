package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaanDao
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.data.local.database.TPosPenerimaanDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.databasereport.ReportParameter
import dev.iconpln.mims.data.local.databasereport.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.transmission_history.TransmissionActivity
import dev.iconpln.mims.utils.StorageUtils
import org.joda.time.DateTime
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailPenerimaanActivity : AppCompatActivity(),Loadable {
    private lateinit var daoSession: DaoSession
    private lateinit var binding: ActivityDetailPenerimaanBinding
    private lateinit var adapter: DetailPenerimaanAdapter
    private val cameraRequestFotoBarang = 101
    private val cameraRequestFotoSuratBarang = 102
    private var filePathFotoBarang: String = ""
    private var filePathFotoSuratBarang: String = ""
    private lateinit var cal: Calendar
    private var noDo: String = ""
    private lateinit var data: TPosPenerimaan

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()

        noDo = intent.getStringExtra("do").toString()

        data = daoSession.tPosPenerimaanDao.queryBuilder().where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()


        adapter = DetailPenerimaanAdapter(arrayListOf(), object : DetailPenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetailPenerimaan) {}

        }, daoSession)

        with(binding){
            recyclerView2.adapter = adapter
            recyclerView2.setHasFixedSize(true)
            recyclerView2.layoutManager = LinearLayoutManager(this@DetailPenerimaanActivity, LinearLayoutManager.VERTICAL, false)

            btnBack.setOnClickListener { startActivity(Intent(this@DetailPenerimaanActivity, TransmissionActivity::class.java)) }

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

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, cameraRequestFotoSuratBarang)


            }

            btnFotoBarang.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this@DetailPenerimaanActivity, arrayOf(Manifest.permission.CAMERA), cameraRequestFotoBarang)

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, cameraRequestFotoBarang)
            }

            barcode2.setOnClickListener {
                val intentIntegrator = IntentIntegrator(this@DetailPenerimaanActivity)
                intentIntegrator.setPrompt("Scan a barcode or QR Code")
                intentIntegrator.setOrientationLocked(true)
                intentIntegrator.initiateScan()
            }

            btnSimpan.setOnClickListener {
                validatete()
            }
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
        val list = daoSession.tPosDetailPenerimaanDao.queryBuilder().where(TPosDetailPenerimaanDao.Properties.IsDone.eq("1")).list()
        var packagings = ""
        for (i in list){
            packagings += "${i.noPackaging},"
            Log.i("noPackaging", i.noPackaging)

        }
        if (packagings != "") {
            packagings = packagings.substring(0, packagings.length - 1)
        }

        with(binding){
            data.petugasPenerima = edtPetugasPenerima.text.toString()
            data.namaEkspedisi = edtEkspedisi.text.toString()
            data.namaKurir = edtNamaKurir.text.toString()
            data.photoSuratBarang = filePathFotoSuratBarang
            data.photoBarang = filePathFotoBarang
            data.tanggalDiterima = edtTanggalDiterima.text.toString()
            data.isChecked = 1
            daoSession.update(data)


            var item = TPemeriksaan()
            item.noPemeriksaan = "V.${data.noDoSmar}.${ DateTime.now().toString("yyMMddHHmmssSS")}"
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
            item.isDone = 0

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

            val currentDate = DateTime.now().toString("yyyy-MM-dd")
            val reportId = "ABS" + DateTime.now().toString("yMdHmsSSS")
            val reportName = "Absensi"
            val params = ArrayList<ReportParameter>()
            params.add(ReportParameter("1", reportId, "plant_code_no", item.planCodeNo, ReportParameter.TEXT ))
            params.add(ReportParameter("2", reportId, "no_do_smar", item.noDoSmar, ReportParameter.TEXT ))
            params.add(ReportParameter("3", reportId, "no_mat_sap", "", ReportParameter.TEXT ))
            params.add(ReportParameter("4", reportId, "penerima", item.petugasPenerima, ReportParameter.TEXT ))
            params.add(ReportParameter("5", reportId, "tanggal", item.tanggalDiterima, ReportParameter.TEXT ))
            params.add(ReportParameter("6", reportId, "kurir", item.namaKurir, ReportParameter.TEXT ))
            params.add(ReportParameter("7", reportId, "ekspedisi", item.namaEkspedisi, ReportParameter.TEXT ))
            params.add(ReportParameter("8", reportId, "quantity", "", ReportParameter.TEXT ))
            params.add(ReportParameter("9", reportId, "username", "", ReportParameter.TEXT ))
            params.add(ReportParameter("10", reportId, "email", "", ReportParameter.TEXT ))
            params.add(ReportParameter("11", reportId, "no_packagings", packagings, ReportParameter.TEXT ))
            params.add(ReportParameter("12", reportId, "photo_file", filePathFotoBarang, ReportParameter.FILE ))
            params.add(ReportParameter("13", reportId, "photo_file2", filePathFotoSuratBarang, ReportParameter.FILE ))
            val reportPenerimaan = GenericReport(reportId, "", reportName, "reportDescription", ApiConfig.sendPenerimaan(), currentDate, 0, 11119209101, params)
            reports.add(reportPenerimaan)

        }

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(this, ReportUploader::class.java)
        startService(iService)
    }

    private fun setCardData() {}

    private fun setPackagingList() {
        val list = daoSession.tPosDetailPenerimaanDao.queryBuilder().list()
        adapter.setPoList(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (requestCode == cameraRequestFotoSuratBarang){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoSuratBarang${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.idFileName.text = file.toString()
            filePathFotoSuratBarang = file.toString()

        }

        if (requestCode == cameraRequestFotoBarang){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoBarang${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.idFileNameBarang.text = file.toString()
            filePathFotoBarang = file.toString()
        }

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, intentResult.contents, Toast.LENGTH_SHORT).show()
                Toast.makeText(this, intentResult.formatName, Toast.LENGTH_SHORT).show()

                val data = daoSession.tPosDetailPenerimaanDao.queryBuilder().where(TPosDetailPenerimaanDao.Properties.NoPackaging.eq(intentResult.contents)).limit(1).unique()
                data.isDone = "1"
                daoSession.update(data)

                val list = daoSession.tPosDetailPenerimaanDao.queryBuilder().list()
                adapter.setPoList(list)
                Toast.makeText(this, data.noPackaging, Toast.LENGTH_SHORT).show()


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    override fun setLoading(show: Boolean, title: String, message: String) {}

    override fun setFinish(result: Boolean, message: String) {
        if (result){
            startActivity(Intent(this@DetailPenerimaanActivity, PenerimaanActivity::class.java))
            finish()
        }
    }
}