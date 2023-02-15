package dev.iconpln.mims.ui.pemeriksaan.complaint

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import dev.iconpln.mims.databinding.ActivityComplaintBinding
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.utils.StorageUtils
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ComplaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComplaintBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listPhoto: List<TPhoto>
    private lateinit var dataPem: TPemeriksaan
    private lateinit var adapter: AddPhotoAdapter
    private val cameraRequestFoto = 101
    private val galleryRequestFoto = 102
    private var noDo: String? = ""
    private var photoNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noDo = intent.getStringExtra("noDo")

        daoSession = (application as MyApplication).daoSession!!
        listPhoto = daoSession.tPhotoDao.queryBuilder()
            .where(TPhotoDao.Properties.NoDo.eq(noDo))
            .where(TPhotoDao.Properties.Type.eq("complaint"))
            .list()
        dataPem = daoSession.tPemeriksaanDao.queryBuilder().where(TPemeriksaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()
        photoNumber = listPhoto.size + 1

        adapter = AddPhotoAdapter(arrayListOf(), object : AddPhotoAdapter.OnAdapterListener{
            override fun onClick(po: TPhoto) {
                if (po.photoNumber == 5){
                    Toast.makeText(this@ComplaintActivity, "Anda sudah melebihi batas upload foto", Toast.LENGTH_SHORT).show()
                }else{
                    doFoto()
                }
            }

        })

        adapter.setPhotoList(listPhoto)

        with(binding){
            rvAddFoto.adapter = adapter
            rvAddFoto.layoutManager = LinearLayoutManager(this@ComplaintActivity, LinearLayoutManager.VERTICAL,false)
            rvAddFoto.setHasFixedSize(true)

            txtDeliveryOrder.text = dataPem.noDoSmar
            txtKurirPengiriman.text = dataPem.namaEkspedisi
            txtNamaKurir.text = dataPem.namaKurir
            txtTglKirim.text = "Tgl ${dataPem.createdDate}"

            if (listPhoto.isEmpty()){
                btnUploadPhoto.visibility = View.VISIBLE
            }else {
                btnUploadPhoto.visibility = View.GONE
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
                Toast.makeText(this@ComplaintActivity, "Silahkan lengkapi data feedback", Toast.LENGTH_SHORT).show()
                return
            }

            if (listPhoto.isEmpty()){
                Toast.makeText(this@ComplaintActivity, "Silahkan lengkapi foto complaint", Toast.LENGTH_SHORT).show()
                return
            }

            showPopUp()
        }
    }

    private fun showPopUp() {
        val dialog = Dialog(this@ComplaintActivity)
        dialog.setContentView(R.layout.popup_complaint);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton

        btnOk.setOnClickListener {
            dialog.dismiss();
            startActivity(Intent(this@ComplaintActivity, PemeriksaanActivity::class.java))
            finish()
        }

        insertData()

        dialog.show();

    }

    private fun insertData() {

    }

    private fun doFoto() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this@ComplaintActivity, arrayOf(
                Manifest.permission.CAMERA), cameraRequestFoto)

        val dialog = BottomSheetDialog(this@ComplaintActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
        var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
        var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

        btnCamera.setOnClickListener {
//                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(cameraIntent, cameraRequestFotoSuratBarang)
            val intent = Intent(this@ComplaintActivity, CameraXActivity::class.java)
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

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()

            var photo = TPhoto()
            photo.noDo = noDo
            photo.type = "complaint"
            photo.path = file.toString()
            photo.photoNumber = photoNumber++
            daoSession.tPhotoDao.insert(photo)

            listPhoto = daoSession.tPhotoDao.queryBuilder()
                .where(TPhotoDao.Properties.Type.eq("complaint"))
                .where(TPhotoDao.Properties.NoDo.eq(noDo))
                .list()

            adapter.setPhotoList(listPhoto)
        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFoto){
            val mPhotoPath = data?.getStringExtra("Path")

            var photo = TPhoto()
            photo.noDo = noDo
            photo.type = "complaint"
            photo.path = mPhotoPath
            photo.photoNumber = photoNumber++
            daoSession.tPhotoDao.insert(photo)

            listPhoto = daoSession.tPhotoDao.queryBuilder()
                .where(TPhotoDao.Properties.Type.eq("complaint"))
                .where(TPhotoDao.Properties.NoDo.eq(noDo)).list()

            adapter.setPhotoList(listPhoto)
        }else{
            Log.d("cancel", "cacelPhoto")
        }
    }
}