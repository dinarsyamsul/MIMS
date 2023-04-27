package dev.iconpln.mims.ui.pemakaian

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
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
import dev.iconpln.mims.databinding.ActivityInputSnPemakaianBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.rating.RatingActivity
import dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan.DetailPemeriksaanActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList

class InputSnPemakaianActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputSnPemakaianBinding
    private lateinit var daoSession: DaoSession
    private var noTransaksi: String = ""
    private lateinit var adapter: PemakaianUlpSnAdapter
    private lateinit var pemakaian: TPemakaian
    private lateinit var pemakaianDetail: TTransPemakaianDetail
    private var plant: String = ""
    private var storloc: String = ""
    private var noMat = ""
    private lateinit var lisSn : List<TListSnMaterialPemakaianUlp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputSnPemakaianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        noTransaksi = intent.getStringExtra("noTransaksi")!!

        noMat = intent.getStringExtra("noMat")!!
        plant = SharedPrefsUtils.getStringPreference(this@InputSnPemakaianActivity,"plant","")!!
        storloc = SharedPrefsUtils.getStringPreference(this@InputSnPemakaianActivity,"storloc","")!!

        lisSn = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
            .where(TListSnMaterialPemakaianUlpDao.Properties.NoTransaksi.eq(noTransaksi))
            .where(TListSnMaterialPemakaianUlpDao.Properties.NoMaterial.eq(noMat)).list()

        pemakaian = daoSession.tPemakaianDao.queryBuilder()
            .where(TPemakaianDao.Properties.NoTransaksi.eq(noTransaksi)).list().get(0)

        pemakaianDetail = daoSession.tTransPemakaianDetailDao.queryBuilder()
            .where(TTransPemakaianDetailDao.Properties.NoTransaksi.eq(noTransaksi))
            .where(TTransPemakaianDetailDao.Properties.NomorMaterial.eq(noMat)).list().get(0)

        adapter = PemakaianUlpSnAdapter(arrayListOf(), object : PemakaianUlpSnAdapter.OnAdapterListener{
            override fun onClick(tms: TListSnMaterialPemakaianUlp) {
                val dialog = Dialog(this@InputSnPemakaianActivity)
                dialog.setContentView(R.layout.popup_validation);
                dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
                val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
                val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
                val message = dialog.findViewById(R.id.message) as TextView
                val txtMessage = dialog.findViewById(R.id.txt_message) as TextView
                val icon = dialog.findViewById(R.id.imageView11) as ImageView

                message.text = "Yakin untuk untuk menghapus?"
                txtMessage.text = "Jika ya, maka serial number akan di hapus"
                icon.setImageResource(R.drawable.ic_warning)

                btnYa.setOnClickListener {
                    deleteSn(tms.noSerialNumber)
                    dialog.dismiss();

                }

                btnTidak.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show();
            }

        })

        adapter.setTmsList(lisSn)

        with(binding){
            val jumlahPemakaian = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
                .where(TListSnMaterialPemakaianUlpDao.Properties.NoTransaksi.eq(noTransaksi))
                .where(TListSnMaterialPemakaianUlpDao.Properties.NoMaterial.eq(noMat)).list()

            btnScanSnMaterial.setOnClickListener { openScanner() }
            btnInputSnManual.setOnClickListener { showPopUp() }

            txtIdPelanggan.text = if(pemakaian.idPelanggan.isNullOrEmpty()) "-" else pemakaian.idPelanggan
            txtNoAgenda.text = if(pemakaian.noAgenda.isNullOrEmpty()) "-" else pemakaian.noAgenda

            btnBack.setOnClickListener { onBackPressed() }
            btnSimpan.setOnClickListener {
                pemakaianDetail.isDone = 1
                pemakaianDetail.qtyPemakaian = jumlahPemakaian.size.toString()
                daoSession.tTransPemakaianDetailDao.update(pemakaianDetail)

                Toast.makeText(this@InputSnPemakaianActivity, "Berhasil simpan data",Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this@InputSnPemakaianActivity, DetailPemakaianUlpYantekActivity::class.java)
                        .putExtra("noTransaksi", noTransaksi)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }

            rvListSn.adapter = adapter
            rvListSn.setHasFixedSize(true)
            rvListSn.layoutManager = LinearLayoutManager(this@InputSnPemakaianActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun deleteSn(sn: String) {
        val dialog = Dialog(this@InputSnPemakaianActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        dialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@InputSnPemakaianActivity)
                .deleteSn(pemakaian.noTransaksi,pemakaianDetail.nomorMaterial,sn,plant,storloc)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    dialog.dismiss()
                    try {
                        if (response.body()?.status == "success"){
                            showPopUpSuccess("Hapus")
                            val toDelete = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
                                .where(TListSnMaterialPemakaianUlpDao.Properties.NoSerialNumber.eq(sn)).list().get(0)

                            daoSession.tListSnMaterialPemakaianUlpDao.delete(toDelete)

                            val refreshList = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
                                .where(TListSnMaterialPemakaianUlpDao.Properties.NoTransaksi.eq(noTransaksi))
                                .where(TListSnMaterialPemakaianUlpDao.Properties.NoMaterial.eq(noMat)).list()
                            adapter.setTmsList(refreshList)
                            showLoading(false)
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@InputSnPemakaianActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        Toast.makeText(this@InputSnPemakaianActivity,e.toString() , Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }else{
                    dialog.dismiss()
                    Toast.makeText(this@InputSnPemakaianActivity, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPopUp() {
        val dialog = Dialog(this@InputSnPemakaianActivity)
        dialog.setContentView(R.layout.monitoring_permintaan_popdialog);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_Ok) as AppCompatButton
        val etMessage = dialog.findViewById(R.id.inpt_snMaterial) as EditText

        btnOk.setOnClickListener {
            sendSn(etMessage.text.toString())
            dialog.dismiss();

        }
        dialog.show();
    }

    private fun sendSn(sn: String) {
        val dialog = Dialog(this@InputSnPemakaianActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        dialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@InputSnPemakaianActivity)
                .addSn(pemakaian.noTransaksi,noMat,sn,plant,storloc)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    try {
                        if (response.body()?.status == "success"){
                            dialog.dismiss()
                            showPopUpSuccess("Simpan")
                            val listSn = TListSnMaterialPemakaianUlp()
                            listSn.isScanned = 1
                            listSn.noMaterial = noMat
                            listSn.noSerialNumber = sn
                            listSn.noTransaksi = pemakaianDetail.noTransaksi
                            daoSession.tListSnMaterialPemakaianUlpDao.insert(listSn)

                            val reloadList = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
                                .where(TListSnMaterialPemakaianUlpDao.Properties.NoTransaksi.eq(noTransaksi))
                                .where(TListSnMaterialPemakaianUlpDao.Properties.NoMaterial.eq(noMat)).list()

                            adapter.setTmsList(reloadList)
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@InputSnPemakaianActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        Toast.makeText(this@InputSnPemakaianActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }else{
                    dialog.dismiss()
                    Toast.makeText(this@InputSnPemakaianActivity, "Gagal menambah serial number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPopUpSuccess(messages: String) {
        val dialog = Dialog(this@InputSnPemakaianActivity)
        dialog.setContentView(R.layout.popup_penerimaan)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val message = dialog.findViewById(R.id.textView16) as TextView
        val txtMessage = dialog.findViewById(R.id.textView22) as TextView

        message.text = "Berhasil"
        txtMessage.text = "Data material berhasil di $messages"

        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading(isShowDialog: Boolean){
        val dialog = Dialog(this@InputSnPemakaianActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        if (isShowDialog){
            dialog.show()
            return
        }

        dialog.dismiss()

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
            if(!result.contents.isNullOrEmpty()){
                Log.i("hit barcode","${result.contents}")
                sendSn(result.contents)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }
}