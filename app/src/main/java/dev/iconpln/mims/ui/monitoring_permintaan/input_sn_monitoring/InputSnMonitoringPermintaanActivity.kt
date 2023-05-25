package dev.iconpln.mims.ui.monitoring_permintaan.input_sn_monitoring

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityInputSnMonitoringPermintaanBinding
import dev.iconpln.mims.ui.monitoring_permintaan.monitoring_permintaan_detail.MonitoringPermintaanDetailActivity
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InputSnMonitoringPermintaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputSnMonitoringPermintaanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listSnm: List<TMonitoringSnMaterial>
    private lateinit var monitoringPenerimaan: TTransMonitoringPermintaan
    private lateinit var adapter: MonitoringPermintaanSnAdapter
    private var noPermintaan = ""
    private var noTransaksi = ""
    private var noRepackaging = ""
    private var noMaterial = ""
    private var descMaterial = ""
    private var kategori = ""
    private var storloc = ""
    private var plant = ""
    private var qtyPermintaan = 0
    private var roleId = 0
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputSnMonitoringPermintaanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!
        noPermintaan = intent.getStringExtra("noPermintaan").toString()
        noTransaksi = intent.getStringExtra("noTransaksi").toString()
        noRepackaging = intent.getStringExtra("noRepackaging").toString()
        noMaterial = intent.getStringExtra("noMat").toString()
        descMaterial = intent.getStringExtra("desc").toString()
        kategori = intent.getStringExtra("kategori").toString()
        qtyPermintaan = intent.getIntExtra("qtyPermintaan",0)
        username = SharedPrefsUtils.getStringPreference(this@InputSnMonitoringPermintaanActivity, "username","")!!

        plant = SharedPrefsUtils.getStringPreference(this@InputSnMonitoringPermintaanActivity,"plant","").toString()
        storloc = SharedPrefsUtils.getStringPreference(this@InputSnMonitoringPermintaanActivity,"storloc","").toString()
        roleId = SharedPrefsUtils.getIntegerPreference(this@InputSnMonitoringPermintaanActivity, "roleId",0)

        listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
            .list()

        monitoringPenerimaan = daoSession.tTransMonitoringPermintaanDao.queryBuilder()
            .where(TTransMonitoringPermintaanDao.Properties.NoTransaksi.eq(noTransaksi)).list()[0]

        adapter = MonitoringPermintaanSnAdapter(arrayListOf(), object : MonitoringPermintaanSnAdapter.OnAdapterListener{
            override fun onClick(tms: TMonitoringSnMaterial) {
                val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
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
                    showPopuDelete(tms)
                    dialog.dismiss();

                }

                btnTidak.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show();
            }

        })

        adapter.setTmsList(listSnm)

        with(binding){
            txtDescMaterial.text = descMaterial
            txtKategori.text = kategori
            txtNoMaterial.text = noMaterial
            txtUnitAsal.text = monitoringPenerimaan.storLocAsalName
            txtQtyPermintaan.text = qtyPermintaan.toString()
            txtQtyScanned.text = listSnm.size.toString()

            srcNoSn.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val searchListSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                        .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                        .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
                        .where(TMonitoringSnMaterialDao.Properties.SerialNumber.like("%"+s.toString()+"%"))
                        .list()

                    adapter.setTmsList(searchListSnm)
                }

            })

            btnBack.setOnClickListener { onBackPressed() }

            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@InputSnMonitoringPermintaanActivity, LinearLayoutManager.VERTICAL,false)
            rvListSn.setHasFixedSize(true)

            btnScanSnMaterial.setOnClickListener { openScanner() }

            btnInputSnManual.setOnClickListener { showPopUp() }

            btnSimpan.setOnClickListener { validate() }
        }
    }

    private fun showPopuDelete(tms: TMonitoringSnMaterial) {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        dialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@InputSnMonitoringPermintaanActivity)
                .permintaanDeleteSn(noRepackaging,noMaterial,tms.serialNumber,roleId)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    dialog.dismiss()
                    try {
                        if (response.body()?.status == "success"){
                            showPopUpSuccess("Hapus")
                            daoSession.tMonitoringSnMaterialDao.delete(tms)

                            val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                                .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                                .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
                                .list()

                            adapter.setTmsList(reloadList)

                            val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
                                .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
                                .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
                                .list()[0]

                            permintaanDetail.qtyAkanDiScan = reloadList.size
                            daoSession.tTransMonitoringPermintaanDetailDao.update(permintaanDetail)
                            binding.txtQtyScanned.text = reloadList.size.toString()
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@InputSnMonitoringPermintaanActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        Toast.makeText(this@InputSnMonitoringPermintaanActivity,e.toString() , Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }else{
                    dialog.dismiss()
                    Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validate() {
        val listSn = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
            .list()

        val listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
            .list()


        val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
            .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
            .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
            .list()[0]

        if (permintaanDetail.qtyPermintaan != listSnm.size){
            Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Qty scan masih kurang", Toast.LENGTH_SHORT).show()
            return
        }

        if (listSnm.size == permintaanDetail.qtyPermintaan){
            permintaanDetail.isScannedSn = 1
        }

        if (listSn.size == 0){
            Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Tidak boleh simpan sebelum scan sn material", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_complaint)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        txtMessage.text = "Data material berhasil di simpan"

        btnOk.setOnClickListener {
            dialog.dismiss();
            submitForm()
        }
        dialog.show();
    }

    private fun submitForm() {
        val listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
            .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
            .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
            .list()


        val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
            .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
            .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
            .list()[0]

//        if (permintaanDetail.qtyPermintaan != listSnm.size){
//            Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Qty scan masih kurang", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (listSnm.size == permintaanDetail.qtyPermintaan){
            permintaanDetail.isScannedSn = 1
        }

        permintaanDetail.qtyAkanDiScan = listSnm.size
        permintaanDetail.isDone = 1
        daoSession.tTransMonitoringPermintaanDetailDao.update(permintaanDetail)

        Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Data Serial Material berhasil disimpan", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@InputSnMonitoringPermintaanActivity, MonitoringPermintaanDetailActivity::class.java)
            .putExtra("noPermintaan",monitoringPenerimaan.noPermintaan)
            .putExtra("noTransaksi", noTransaksi))
        finish()
    }

    private fun showPopUp() {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
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

    private fun sendSn(sn: String) {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        dialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@InputSnMonitoringPermintaanActivity)
                .permintaanAddSn(noRepackaging,noMaterial,sn,plant, storloc,roleId,username)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    try {
                        if (response.body()?.status == "success"){
                            dialog.dismiss()
                            showPopUpSuccess("Simpan")
                            val listSn = TMonitoringSnMaterial()
                            listSn.noRepackaging = noRepackaging
                            listSn.isScanned = 1
                            listSn.serialNumber = sn
                            listSn.nomorMaterial = noMaterial
                            listSn.status = "SESUAI"
                            daoSession.tMonitoringSnMaterialDao.insert(listSn)

                            val reloadList = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                                .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                                .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial)).list()

                            adapter.setTmsList(reloadList)

                            binding.txtQtyScanned.text = reloadList.size.toString()
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@InputSnMonitoringPermintaanActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        Toast.makeText(this@InputSnMonitoringPermintaanActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }else{
                    dialog.dismiss()
                    Toast.makeText(this@InputSnMonitoringPermintaanActivity, "Gagal menambah serial number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPopUpSuccess(messages: String) {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
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

    override fun onBackPressed() {
        val dialog = Dialog(this@InputSnMonitoringPermintaanActivity)
        dialog.setContentView(R.layout.popup_validation)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val icon = dialog.findViewById(R.id.imageView11) as ImageView
        val btnOk = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.message) as TextView
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        icon.setImageResource(R.drawable.ic_popup_delete)
        message.text = "Keluar"
        txtMessage.text = "Yakin untuk keluar?"


        btnTidak.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            dialog.dismiss();
            val listSnm = daoSession.tMonitoringSnMaterialDao.queryBuilder()
                .where(TMonitoringSnMaterialDao.Properties.NoRepackaging.eq(noRepackaging))
                .where(TMonitoringSnMaterialDao.Properties.NomorMaterial.eq(noMaterial))
                .list()
//
//            daoSession.tMonitoringSnMaterialDao.deleteInTx(listSnm)

            val permintaanDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
                .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
                .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.eq(noMaterial))
                .list()[0]

            permintaanDetail.qtyAkanDiScan = listSnm.size
            daoSession.tTransMonitoringPermintaanDetailDao.update(permintaanDetail)

            startActivity(Intent(this@InputSnMonitoringPermintaanActivity, MonitoringPermintaanDetailActivity::class.java)
                .putExtra("noPermintaan", monitoringPenerimaan.noPermintaan)
                .putExtra("noTransaksi", noTransaksi)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        dialog.show();
    }
}