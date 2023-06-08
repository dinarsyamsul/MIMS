package dev.iconpln.mims.ui.pemeriksaan.complaint_pemeriksaan

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.iconpln.mims.AddPhotoAdapter
import dev.iconpln.mims.CameraXActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityComplaintBinding
import dev.iconpln.mims.databinding.ActivityComplaintPemeriksaanBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import dev.iconpln.mims.utils.StorageUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ComplaintPemeriksaanActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityComplaintPemeriksaanBinding
    private var progressDialog: AlertDialog? = null
    private lateinit var daoSession: DaoSession
    private lateinit var listPhoto: List<TPhoto>
    private lateinit var pemeriksaan: TPemeriksaan
    private lateinit var adapter: AddPhotoAdapter
    private val cameraRequestFoto = 101
    private val galleryRequestFoto = 102
    private var noPem: String? = ""
    private var noDo:String? = ""
    private var photoNumber: Int = 0
    private lateinit var listDetailPem: MutableList<TPemeriksaanDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noPem = intent.getStringExtra("noPemeriksaan")
        noDo = intent.getStringExtra("noDo")

        daoSession = (application as MyApplication).daoSession!!
        listPhoto = daoSession.tPhotoDao.queryBuilder()
            .where(TPhotoDao.Properties.NoDo.eq(noPem))
            .where(TPhotoDao.Properties.Type.eq("complaint_pemeriksaan"))
            .list()

        listDetailPem = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .where(TPemeriksaanDetailDao.Properties.IsDone.eq(0))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(0)).list()

        pemeriksaan = daoSession.tPemeriksaanDao.queryBuilder().where(TPemeriksaanDao.Properties.NoPemeriksaan.eq(noPem)).limit(1).unique()
        photoNumber = listPhoto.size + 1

        adapter = AddPhotoAdapter(arrayListOf(), object : AddPhotoAdapter.OnAdapterListener{
            override fun onClick(po: TPhoto) {
                if (po.photoNumber == 5){
                    Toast.makeText(this@ComplaintPemeriksaanActivity, "Anda sudah melebihi batas upload foto", Toast.LENGTH_SHORT).show()
                }else{
                    doFoto()
                }
            }

        }, true,
            object : AddPhotoAdapter.OnAdapterListenerDelete{
                override fun onClick(po: TPhoto) {
                    val delete = daoSession.tPhotoDao.queryBuilder()
                        .where(TPhotoDao.Properties.Id.eq(po.id)).limit(1).unique()
                    daoSession.tPhotoDao.delete(delete)

                    val newList = daoSession.tPhotoDao.queryBuilder()
                        .where(TPhotoDao.Properties.NoDo.eq(noDo))
                        .where(TPhotoDao.Properties.Type.eq("complaint_pemeriksaan"))
                        .list()

                    adapter.setPhotoList(newList)
                    photoNumber--

                    if (newList.isEmpty()){
                        binding.btnUploadPhoto.visibility = View.VISIBLE
                        binding.txtFilePhoto.visibility = View.VISIBLE
                    }else {
                        binding.btnUploadPhoto.visibility = View.GONE
                        binding.txtFilePhoto.visibility = View.GONE
                    }
                }

            })

        adapter.setPhotoList(listPhoto)

        with(binding){
            rvAddFoto.adapter = adapter
            rvAddFoto.layoutManager = LinearLayoutManager(this@ComplaintPemeriksaanActivity, LinearLayoutManager.VERTICAL,false)
            rvAddFoto.setHasFixedSize(true)

            txtDeliveryOrder.text = pemeriksaan.noDoSmar
            txtKurirPengiriman.text = pemeriksaan.expeditions
            txtNamaKurir.text = pemeriksaan.namaKurir
            txtTglKirim.text = "Tgl ${pemeriksaan.createdDate}"

            if (listPhoto.isEmpty()){
                btnUploadPhoto.visibility = View.VISIBLE
                txtFilePhoto.visibility = View.VISIBLE
            }else {
                btnUploadPhoto.visibility = View.GONE
                txtFilePhoto.visibility = View.GONE
            }

            btnUploadPhoto.setOnClickListener {
                doFoto()
            }

            btnClose.setOnClickListener { onBackPressed() }

            btnSimpan.setOnClickListener { validation() }
        }
    }

    private fun validation() {
        with(binding){
            val fbComplaint = editText.text.toString()

            if (fbComplaint.isNullOrEmpty()){
                Toast.makeText(this@ComplaintPemeriksaanActivity, "Silahkan lengkapi data feedback", Toast.LENGTH_SHORT).show()
                return
            }

            if (listPhoto.isEmpty()){
                Toast.makeText(this@ComplaintPemeriksaanActivity, "Silahkan lengkapi foto complaint", Toast.LENGTH_SHORT).show()
                return
            }

            showPopUp()
        }
    }

    private fun showPopUp() {
        val dialog = Dialog(this@ComplaintPemeriksaanActivity)
        dialog.setContentView(R.layout.popup_complaint);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton

        btnOk.setOnClickListener {
            dialog.dismiss();
            insertData()
        }

        dialog.show();

    }

    private fun insertData() {
        var sns = ""

        var penerimaans = daoSession.tPosPenerimaanDao.queryBuilder()
            .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).list().get(0)

        var checkedDetPen = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoDoSmar.eq(noDo))
            .where(TPemeriksaanDetailDao.Properties.IsDone.eq(0))
            .where(TPemeriksaanDetailDao.Properties.IsChecked.eq(1)).list()

        var detailPenerimaanAkhir = daoSession.tPosDetailPenerimaanAkhirDao.queryBuilder()
            .where(TPosDetailPenerimaanAkhirDao.Properties.NoDoSmar.eq(noDo)).list()

        for (i in checkedDetPen){
            sns += "${i.noPackaging},${i.sn},SEDANG KOMPLAIN,${pemeriksaan.doLineItem},${i.noMaterail};"
            Log.i("noPackaging", i.noPackaging)

        }
        if (sns != "") {
            sns = sns.substring(0, sns.length - 1)
        }

        for (i in checkedDetPen){
            i.isComplaint = 1
            i.isDone = 1
            i.statusPenerimaan = "KOMPLAIN"
            i.statusPemeriksaan = "SELESAI"
            daoSession.tPemeriksaanDetailDao.update(i)

            for (j in detailPenerimaanAkhir){
                if (j.serialNumber == i.sn){
                    j.isComplaint = true
                    daoSession.tPosDetailPenerimaanAkhirDao.update(j)
                }
            }
        }

        if (checkedDetPen.isEmpty()){
            pemeriksaan.isDone = 1
            daoSession.tPemeriksaanDao.update(pemeriksaan)
        }

        penerimaans.statusPenerimaan = "SEDANG KOMPLAIN"
        penerimaans.statusPemeriksaan = "BELUM DIPERIKSA"
        daoSession.tPosPenerimaanDao.update(penerimaans)

        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //region Add report visit to queue
        var jwtWeb = SharedPrefsUtils.getStringPreference(this@ComplaintPemeriksaanActivity, "jwtWeb", "")
        var jwtMobile = SharedPrefsUtils.getStringPreference(this@ComplaintPemeriksaanActivity,"jwt","")
        var jwt = "$jwtMobile;$jwtWeb"
        var username = SharedPrefsUtils.getStringPreference(this@ComplaintPemeriksaanActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemeriksaan" + username + "_" + noDo + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data Komplain Pemeriksaan"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "no_do_smar", noDo!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "alasan", binding.editText.text.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "quantity", checkedDetPen.size.toString(), ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "username", username!!, ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "email", username, ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "sns", sns, ReportParameter.TEXT))
        params.add(ReportParameter("7", reportId, "status", "ACTIVE", ReportParameter.TEXT))
        params.add(ReportParameter("8", reportId, "plant_code_no", pemeriksaan.planCodeNo, ReportParameter.TEXT))
        params.add(ReportParameter("9", reportId, "no_do_line", pemeriksaan.doLineItem, ReportParameter.TEXT))

        var i = 1
        var reportParameter = 10
        for (j in listPhoto){
            params.add(ReportParameter(reportParameter.toString(), reportId, "photos$i", j.path, ReportParameter.FILE))
            i++
            reportParameter++
        }

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendComplaintPemeriksaan(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)

    }

    private fun doFoto() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this@ComplaintPemeriksaanActivity, arrayOf(
                Manifest.permission.CAMERA), cameraRequestFoto)

        val dialog = BottomSheetDialog(this@ComplaintPemeriksaanActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
        var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
        var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

        btnCamera.setOnClickListener {
//                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(cameraIntent, cameraRequestFotoSuratBarang)
            val intent = Intent(this@ComplaintPemeriksaanActivity, CameraXActivity::class.java)
                .putExtra("fotoName", "fotoPemeriksaan")
            startActivityForResult(intent,cameraRequestFoto)
            dialog.dismiss()
        }

        btnGallery.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, galleryRequestFoto)
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == galleryRequestFoto){
            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "mims" + "picturesFotoKomplain${UUID.randomUUID()}" + ".png")
            val fOut = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 65, fOut)
            fOut.flush()
            fOut.close()

            insertDataFoto(file)

        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFoto){
            val mPhotoPath = data?.getStringExtra("Path")

            var photo = TPhoto()
            photo.noDo = noPem
            photo.type = "complaint_pemeriksaan"
            photo.path = mPhotoPath
            photo.photoNumber = photoNumber++
            daoSession.tPhotoDao.insert(photo)

            listPhoto = daoSession.tPhotoDao.queryBuilder()
                .where(TPhotoDao.Properties.Type.eq("complaint_pemeriksaan"))
                .where(TPhotoDao.Properties.NoDo.eq(noPem)).list()

            if (listPhoto.isEmpty()){
                binding.btnUploadPhoto.visibility = View.VISIBLE
                binding.txtFilePhoto.visibility = View.VISIBLE
            }else {
                binding.btnUploadPhoto.visibility = View.GONE
                binding.txtFilePhoto.visibility = View.GONE
            }

            adapter.setPhotoList(listPhoto)
        }else{
            Log.d("cancel", "cacelPhoto")
        }
    }

    private fun insertDataFoto(file: File) {

        if (file.length() > Config.MAX_PHOTO_SIZE){
            Toast.makeText(this@ComplaintPemeriksaanActivity, "Ukuran foto tidak boleh melebihi 200 kb", Toast.LENGTH_SHORT).show()
            return
        }

        var photo = TPhoto()
        photo.noDo = noPem
        photo.type = "complaint_pemeriksaan"
        photo.path = file.toString()
        photo.photoNumber = photoNumber++
        daoSession.tPhotoDao.insert(photo)

        listPhoto = daoSession.tPhotoDao.queryBuilder()
            .where(TPhotoDao.Properties.Type.eq("complaint_pemeriksaan"))
            .where(TPhotoDao.Properties.NoDo.eq(noPem))
            .list()

        if (listPhoto.isEmpty()){
            binding.btnUploadPhoto.visibility = View.VISIBLE
            binding.txtFilePhoto.visibility = View.VISIBLE
        }else {
            binding.btnUploadPhoto.visibility = View.GONE
            binding.txtFilePhoto.visibility = View.GONE
        }

        adapter.setPhotoList(listPhoto)
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
        startActivity(
            Intent(this@ComplaintPemeriksaanActivity, PemeriksaanActivity::class.java )
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
        daoSession.tPhotoDao.deleteInTx(listPhoto)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        daoSession.tPhotoDao.deleteInTx(listPhoto)
        super.onBackPressed()
    }
}