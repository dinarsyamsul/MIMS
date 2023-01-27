package dev.iconpln.mims.ui.role.up3.pnerimaan.detail_penerimaan

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaanDao
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.data.local.database.TPosPenerimaanDao
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanBinding
import dev.iconpln.mims.ui.role.up3.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.StorageUtils
import dev.iconpln.mims.utils.UriPathHelper
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class DetailPenerimaanActivity : AppCompatActivity() {
    private lateinit var daoSession: DaoSession
    private lateinit var binding: ActivityDetailPenerimaanBinding
    private lateinit var adapter: DetailPenerimaanAdapter
    private val cameraRequestFotoBarang = 101
    private val cameraRequestFotoSuratBarang = 102
    private var filePathFotoBarang: String = ""
    private var filePathFotoSuratBarang: String = ""
    private lateinit var cal: Calendar

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()

        adapter = DetailPenerimaanAdapter(arrayListOf(), object : DetailPenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetailPenerimaan) {}

        })

        with(binding){
            recyclerView2.adapter = adapter
            recyclerView2.setHasFixedSize(true)
            recyclerView2.layoutManager = LinearLayoutManager(this@DetailPenerimaanActivity, LinearLayoutManager.VERTICAL, false)

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
                startActivity(Intent(this@DetailPenerimaanActivity, PenerimaanActivity::class.java))
            }
        }

        setPackagingList()
        setCardData()
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
}