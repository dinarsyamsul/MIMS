package dev.iconpln.mims.ui.pemeriksaan.input_petugas_penerimaan

import android.Manifest
import android.app.DatePickerDialog
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
import androidx.activity.viewModels
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
import dev.iconpln.mims.databinding.ActivityInputPetugasPenerimaanBinding
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanViewModel
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.StorageUtils
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class InputPetugasPenerimaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputPetugasPenerimaanBinding
    private lateinit var daoSession: DaoSession
    private val viewModel: PemeriksaanViewModel by viewModels()
    private lateinit var cal: Calendar
    private lateinit var listPhoto: List<TPhoto>
    private lateinit var dataPem: TPemeriksaan
    private lateinit var adapter: AddPhotoAdapter
    var pemeriksaan = TPemeriksaan()
    private val cameraRequestFoto = 101
    private val galleryRequestFoto = 102
    private var noDo: String? = ""
    private var photoNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputPetugasPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()
        noDo = intent.getStringExtra("noDo")
        listPhoto = daoSession.tPhotoDao.queryBuilder()
            .where(TPhotoDao.Properties.NoDo.eq(noDo))
            .where(TPhotoDao.Properties.Type.eq("input penerima"))
            .list()
        dataPem = daoSession.tPemeriksaanDao.queryBuilder().where(TPemeriksaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        photoNumber = listPhoto.size + 1

        adapter = AddPhotoAdapter(arrayListOf(), object : AddPhotoAdapter.OnAdapterListener{
            override fun onClick(po: TPhoto) {
                if (po.photoNumber == 5){
                    Toast.makeText(this@InputPetugasPenerimaanActivity, "Anda sudah melebihi batas upload foto", Toast.LENGTH_SHORT).show()
                }else{
                    doFoto()
                }
            }

        })

        adapter.setPhotoList(listPhoto)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }

            edtTanggalDiterima.setText("${dataPem.tanggalDiterima}")
            edtPetugasPenerima.setText("${dataPem.petugasPenerima}")
            edtEkspedisi.setText("${dataPem.namaEkspedisi}")
            edtNamaKurir.setText("${dataPem.namaKurir}")

            if (listPhoto.isEmpty()){
                btnUploadPhoto.visibility = View.VISIBLE
            }else {
                btnUploadPhoto.visibility = View.GONE
            }

            btnUploadPhoto.setOnClickListener {
                doFoto()
            }

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edtTanggalDiterima.setText(sdf.format(cal.time))

            }

            edtTanggalDiterima.setOnClickListener{
                DatePickerDialog(this@InputPetugasPenerimaanActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            btnSimpan.setOnClickListener { validation() }

            rvAddFoto.adapter = adapter
            rvAddFoto.layoutManager = LinearLayoutManager(this@InputPetugasPenerimaanActivity, LinearLayoutManager.VERTICAL, false)
            rvAddFoto.setHasFixedSize(true)
        }
    }

    private fun doFoto() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this@InputPetugasPenerimaanActivity, arrayOf(
                Manifest.permission.CAMERA), cameraRequestFoto)

        val dialog = BottomSheetDialog(this@InputPetugasPenerimaanActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_photo, null)
        var btnCamera = view.findViewById<CardView>(R.id.cv_kamera)
        var btnGallery = view.findViewById<CardView>(R.id.cv_gallery)

        btnCamera.setOnClickListener {
//                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(cameraIntent, cameraRequestFotoSuratBarang)
            val intent = Intent(this@InputPetugasPenerimaanActivity, CameraXActivity::class.java)
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

    private fun validation() {
        with(binding){
            val tglDiterima = edtTanggalDiterima.text.toString()
            val petugasPenerima = edtPetugasPenerima.text.toString()
            val namaKurir = edtNamaKurir.text.toString()
            val namaEkspedisi = edtEkspedisi.text.toString()

            if (tglDiterima.isNullOrEmpty()){
                Toast.makeText(this@InputPetugasPenerimaanActivity, "Lengkapi setiap data yang akan di inputkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (petugasPenerima.isNullOrEmpty()){
                Toast.makeText(this@InputPetugasPenerimaanActivity, "Lengkapi setiap data yang akan di inputkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaKurir.isNullOrEmpty()){
                Toast.makeText(this@InputPetugasPenerimaanActivity, "Lengkapi setiap data yang akan di inputkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaEkspedisi.isNullOrEmpty()){
                Toast.makeText(this@InputPetugasPenerimaanActivity, "Lengkapi setiap data yang akan di inputkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (listPhoto.isEmpty()){
                Toast.makeText(this@InputPetugasPenerimaanActivity, "Silahkan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show()
                return
            }

            insertData(tglDiterima,petugasPenerima,namaKurir,namaEkspedisi)
        }
    }

    private fun insertData(
        tglDiterima: String,
        petugasPenerima: String,
        namaKurir: String,
        namaEkspedisi: String
    ) {
        val noPemeriksaan = "PEM${noDo}${DateTimeUtils.currentUtcString}"
        dataPem.noPemeriksaan = noPemeriksaan
        dataPem.tanggalDiterima = tglDiterima
        dataPem.petugasPenerima = petugasPenerima
        dataPem.namaKurir = namaKurir
        dataPem.namaEkspedisi = namaEkspedisi

        dataPem.state = 2
        dataPem.isDone = 0

        daoSession.update(dataPem)

        viewModel.setDataDetailPemeriksaan(daoSession,noPemeriksaan,noDo)

        val dialog = Dialog(this@InputPetugasPenerimaanActivity)
        dialog.setContentView(R.layout.popup_penerimaan);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton

        btnOk.setOnClickListener {
            dialog.dismiss();
            startActivity(Intent(this@InputPetugasPenerimaanActivity, PemeriksaanActivity::class.java ))
            finish()
        }

        dialog.show();
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
            photo.type = "input penerima"
            photo.path = file.toString()
            photo.photoNumber = photoNumber++
            daoSession.tPhotoDao.insert(photo)

            listPhoto = daoSession.tPhotoDao.queryBuilder().where(TPhotoDao.Properties.NoDo.eq(noDo)).list()

            adapter.setPhotoList(listPhoto)
        }else{
            Log.d("cancel", "cacelPhoto")
        }

        if (resultCode == RESULT_OK && requestCode == cameraRequestFoto){
            val mPhotoPath = data?.getStringExtra("Path")

            var photo = TPhoto()
            photo.noDo = noDo
            photo.type = "input penerima"
            photo.path = mPhotoPath
            photo.photoNumber = photoNumber++
            daoSession.tPhotoDao.insert(photo)

            listPhoto = daoSession.tPhotoDao.queryBuilder().where(TPhotoDao.Properties.NoDo.eq(noDo)).list()

            adapter.setPhotoList(listPhoto)
        }else{
            Log.d("cancel", "cacelPhoto")
        }
    }
}