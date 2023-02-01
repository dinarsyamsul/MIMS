package dev.iconpln.mims.ui.rating.detail_rating

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDao
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailRatingBinding
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.rating.RatingActivity
import dev.iconpln.mims.utils.SharedPrefsUtils
import dev.iconpln.mims.utils.StorageUtils
import org.joda.time.DateTime
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DetailRatingActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityDetailRatingBinding
    private lateinit var daoSession: DaoSession
    private lateinit var data: TPemeriksaan
    private var noDo: String = ""
    private var nilaiKualitasMaterial : String = "0"
    private var nilaiRespon : String = "0"
    private var nilaiWaktu: String = "0"
    private var filePath: String = ""
    private lateinit var dataDetail: TPemeriksaanDetail
    private val cameraRequestFotoBarang = 103
    private val cameraRequestFotoBarangGallery = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        noDo = intent?.getStringExtra("noDo")!!

        data = daoSession.tPemeriksaanDao.queryBuilder().where(TPemeriksaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()
        dataDetail = daoSession.tPemeriksaanDetailDao.queryBuilder().where(TPemeriksaanDetailDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        with(binding){

            btnSimpan.setOnClickListener {
                submitForm()
            }
            btnFotoSuratBarang.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this@DetailRatingActivity, arrayOf(
                        Manifest.permission.CAMERA), cameraRequestFotoBarang)

                val dialog = BottomSheetDialog(this@DetailRatingActivity, R.style.AppBottomSheetDialogTheme)
                val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
                var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
                var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

                btnCamera.setOnClickListener {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, cameraRequestFotoBarang)
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
            kualitasMaterial1.setOnClickListener{
                kualitasMaterial1.setImageResource(R.drawable.ic_star_true)
                nilaiKualitasMaterial = "20"
            }
            kualitasMaterial2.setOnClickListener{
                kualitasMaterial1.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial2.setImageResource(R.drawable.ic_star_true)
                nilaiKualitasMaterial = "40"
            }
            kualitasMaterial3.setOnClickListener{
                kualitasMaterial1.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial2.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial3.setImageResource(R.drawable.ic_star_true)
                nilaiKualitasMaterial = "60"
            }
            kualitasMaterial4.setOnClickListener{
                kualitasMaterial1.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial2.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial3.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial4.setImageResource(R.drawable.ic_star_true)
                nilaiKualitasMaterial = "80"
            }
            kualitasMaterial5.setOnClickListener{
                kualitasMaterial1.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial2.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial3.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial4.setImageResource(R.drawable.ic_star_true)
                kualitasMaterial5.setImageResource(R.drawable.ic_star_true)
                nilaiKualitasMaterial = "100"
            }

            kualitasRespon1.setOnClickListener{
                kualitasRespon1.setImageResource(R.drawable.ic_star_true)
                nilaiRespon = "20"
            }
            kualitasRespon2.setOnClickListener{
                kualitasRespon1.setImageResource(R.drawable.ic_star_true)
                kualitasRespon2.setImageResource(R.drawable.ic_star_true)
                nilaiRespon = "40"
            }
            kualitasRespon3.setOnClickListener{
                kualitasRespon1.setImageResource(R.drawable.ic_star_true)
                kualitasRespon2.setImageResource(R.drawable.ic_star_true)
                kualitasRespon3.setImageResource(R.drawable.ic_star_true)
                nilaiRespon = "60"
            }
            kualitasRespon4.setOnClickListener{
                kualitasRespon1.setImageResource(R.drawable.ic_star_true)
                kualitasRespon2.setImageResource(R.drawable.ic_star_true)
                kualitasRespon3.setImageResource(R.drawable.ic_star_true)
                kualitasRespon4.setImageResource(R.drawable.ic_star_true)
                nilaiRespon = "80"
            }
            kualitasRespon5.setOnClickListener{
                kualitasRespon1.setImageResource(R.drawable.ic_star_true)
                kualitasRespon2.setImageResource(R.drawable.ic_star_true)
                kualitasRespon3.setImageResource(R.drawable.ic_star_true)
                kualitasRespon4.setImageResource(R.drawable.ic_star_true)
                kualitasRespon5.setImageResource(R.drawable.ic_star_true)
                nilaiRespon = "100"
            }

            kualitasWaktu1.setOnClickListener{
                kualitasWaktu1.setImageResource(R.drawable.ic_star_true)
                nilaiWaktu = "20"
            }
            kualitasWaktu2.setOnClickListener{
                kualitasWaktu1.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu2.setImageResource(R.drawable.ic_star_true)
                nilaiWaktu = "40"
            }
            kualitasWaktu3.setOnClickListener{
                kualitasWaktu1.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu2.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu3.setImageResource(R.drawable.ic_star_true)
                nilaiWaktu = "60"
            }
            kualitasWaktu4.setOnClickListener{
                kualitasWaktu1.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu2.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu3.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu4.setImageResource(R.drawable.ic_star_true)
                nilaiWaktu = "80"
            }
            kualitasWaktu5.setOnClickListener{
                kualitasWaktu1.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu2.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu3.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu4.setImageResource(R.drawable.ic_star_true)
                kualitasWaktu5.setImageResource(R.drawable.ic_star_true)
                nilaiWaktu = "100"
            }

        }
    }

    private fun submitForm() {
        val reports = ArrayList<GenericReport>()

        data.ratingPath = filePath
        data.ratingWaktu = nilaiWaktu
        data.ratingQuality = nilaiKualitasMaterial
        data.ratingPenerimaan = nilaiRespon
        data.isDone = 1
        data.state = 3
        daoSession.update(data)

        var jwt = SharedPrefsUtils.getStringPreference(this@DetailRatingActivity,"jwt","")
        var username = SharedPrefsUtils.getStringPreference(this@DetailRatingActivity, "username","")
        var email = SharedPrefsUtils.getStringPreference(this@DetailRatingActivity, "email","")
        val currentDate = DateTime.now().toString("yyyy-MM-dd")
        val reportId = "Rating" + DateTime.now().toString("yMdHmsSSS")
        val reportName = "Rating"
        val reportDescription = "Rating-${data.noDoSmar}-${data.packangings}-${DateTime.now().toString("yyyy-MM-dd")}"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "plant_code_no", data.planCodeNo, ReportParameter.TEXT ))
        params.add(ReportParameter("2", reportId, "no_do_smar", data.noDoSmar, ReportParameter.TEXT ))
        params.add(ReportParameter("3", reportId, "username", username!!, ReportParameter.TEXT ))
        params.add(ReportParameter("4", reportId, "email", email!!, ReportParameter.TEXT ))
        params.add(ReportParameter("5", reportId, "no_packaging", dataDetail.noPackaging, ReportParameter.TEXT ))
        params.add(ReportParameter("6", reportId, "no_mat_sap", data.noDoMims, ReportParameter.TEXT ))
        params.add(ReportParameter("7", reportId, "ekspedisi", data.namaEkspedisi, ReportParameter.TEXT ))
        params.add(ReportParameter("8", reportId, "rating_delivery", nilaiWaktu, ReportParameter.TEXT ))
        params.add(ReportParameter("9", reportId, "rating_response", nilaiRespon, ReportParameter.TEXT ))
        params.add(ReportParameter("10", reportId, "rating_quality",nilaiKualitasMaterial , ReportParameter.TEXT ))
        params.add(ReportParameter("11", reportId, "photo_file", filePath, ReportParameter.FILE ))
        val reportPenerimaan = GenericReport(reportId, username, reportName, reportDescription, ApiConfig.sendRating(), currentDate, 0, 11119209101, params)
        reports.add(reportPenerimaan)

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(this, ReportUploader::class.java)
        startService(iService)
    }

    override fun setLoading(show: Boolean, title: String, message: String) {

    }

    override fun setFinish(result: Boolean, message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT)
        startActivity(Intent(this, RatingActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == cameraRequestFotoBarangGallery){
            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesRatingFotoBarang${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.idFileName.setImageBitmap(bitmap)
            filePath = file.toString()
        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFotoBarang){
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesRatingFotoBarang${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            binding.idFileName.setImageBitmap(bitmap)
            filePath = file.toString()
        }else{
            Log.d("cancel", "cacelPhoto")
        }
    }
}