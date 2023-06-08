package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan_akhir

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityComplaintPemeriksaanBinding
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanAkhirBinding
import dev.iconpln.mims.ui.pnerimaan.detail_penerimaan.DetailPenerimaanDokumentasiAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPenerimaanAkhirActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPenerimaanAkhirBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: DetailPenerimaanAkhirAdapter
    private lateinit var penerimaan: TPosPenerimaan
    private lateinit var listDokumentasi: List<String>
    private var noDo = ""
    private lateinit var dataPenerimaanAkhir: List<TPosDetailPenerimaanAkhir>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanAkhirBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noDo = intent.getStringExtra("noDo")!!
        getDokumentasi(noDo)

        dataPenerimaanAkhir = daoSession.tPosDetailPenerimaanAkhirDao.queryBuilder()
            .where(TPosDetailPenerimaanAkhirDao.Properties.NoDoSmar.eq(noDo)).list()

        penerimaan = daoSession.tPosPenerimaanDao.queryBuilder()
            .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        adapter = DetailPenerimaanAkhirAdapter(arrayListOf(), object : DetailPenerimaanAkhirAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetailPenerimaanAkhir) {}

        },daoSession)

        adapter.setData(dataPenerimaanAkhir)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@DetailPenerimaanAkhirActivity, LinearLayoutManager.VERTICAL,false)
            rvListSn.setHasFixedSize(true)

            txtKurirPengiriman.text = penerimaan.expeditions
            txtTglKirim.text = "Tgl ${penerimaan.createdDate}"
            txtPetugasPenerima.text = penerimaan.petugasPenerima
            txtDeliveryOrder.text = penerimaan.noDoSmar
            txtNamaKurir.text = penerimaan.kurirPengantar

            txtDokumentasi.setOnClickListener {
                val dialog = Dialog(this@DetailPenerimaanAkhirActivity)
                dialog.setContentView(R.layout.popup_dokumentasi)
                dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.setCancelable(false)
                dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
                val adapter = DetailPenerimaanDokumentasiAdapter(arrayListOf())

                val recyclerView = dialog.findViewById<RecyclerView>(R.id.rv_dokumentasi)
                val btnClose = dialog.findViewById<ImageView>(R.id.btn_close)

                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@DetailPenerimaanAkhirActivity,LinearLayoutManager.HORIZONTAL,false)
                recyclerView.setHasFixedSize(true)

                adapter.setData(listDokumentasi)

                btnClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show();
            }

            cbSesuai.isEnabled = false
            cbTidakSesuai.isEnabled = false

            btnKomplain.isEnabled = false
            btnKomplain.setBackgroundColor(Color.GRAY)
            btnTerima.isEnabled = false
            btnTerima.setBackgroundColor(Color.GRAY)

            srcNoSn.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val searchSnPenerimaanAkhir = daoSession.tPosDetailPenerimaanAkhirDao.queryBuilder()
                        .where(TPosDetailPenerimaanAkhirDao.Properties.NoDoSmar.eq(noDo))
                        .whereOr(TPosDetailPenerimaanAkhirDao.Properties.NoPackaging.like("%"+s.toString()+"%"),
                            TPosDetailPenerimaanAkhirDao.Properties.SerialNumber.like("%"+s.toString()+"%"))
                        .list()

                    adapter.setData(searchSnPenerimaanAkhir)
                }

            })

            btnScanPackaging.setOnClickListener {
                openScanner(1)
            }

            btnScanSn.setOnClickListener {
                openScanner(2)
            }
        }

    }

    private fun getDokumentasi(noDo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@DetailPenerimaanAkhirActivity).getDokumentasi(noDo)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    try {
                        if (response.body()?.status == "failure"){
                            Toast.makeText(this@DetailPenerimaanAkhirActivity, response.body()?.message,Toast.LENGTH_SHORT).show()
                        }else{
                            listDokumentasi = response.body()?.doc?.array!!
                        }
                    }catch (e: Exception){
                        Toast.makeText(this@DetailPenerimaanAkhirActivity, response.body()?.message,Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }finally {
                        Log.d("Error","Terjadi Kesalahan")
                    }
                }else{
                    Toast.makeText(this@DetailPenerimaanAkhirActivity, response.message(),Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                binding.srcNoSn.setText(result.contents)

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
                binding.srcNoSn.setText(result.contents)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }
}