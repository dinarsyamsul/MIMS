package dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
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
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityPemeriksaanDetailBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pemeriksaan.complaint.ComplaintActivity
import org.w3c.dom.Text

class PemeriksaanDetailActivity : AppCompatActivity(), Loadable {
    private lateinit var binding: ActivityPemeriksaanDetailBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: DetailPemeriksaanAdapter
    private lateinit var listSns: MutableList<TPosSns>
    private lateinit var listPemDetail: MutableList<TPemeriksaanDetail>
    private lateinit var pemeriksaan: TPemeriksaan
    private var progressDialog: AlertDialog? = null
    private var noPem: String = ""
    private var noDo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noPem = intent.getStringExtra("noPemeriksaan")!!
        noDo = intent.getStringExtra("noDo")!!

        listSns = daoSession.tPosSnsDao.queryBuilder().where(TPosSnsDao.Properties.NoDoSmar.eq(noDo)).list()
        Log.d("checkSns", listSns.size.toString())
        listPemDetail = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem))
            .list()
        pemeriksaan = daoSession.tPemeriksaanDao.queryBuilder()
            .where(TPemeriksaanDao.Properties.NoPemeriksaan.eq(noPem)).limit(1).unique()

        adapter = DetailPemeriksaanAdapter(arrayListOf(), object : DetailPemeriksaanAdapter.OnAdapterListener{
            override fun onClick(po: TPemeriksaanDetail) {

            }

        },daoSession)

        adapter.setPedList(listPemDetail)

        with(binding){
            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@PemeriksaanDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvListSn.setHasFixedSize(true)

            txtKurirPengiriman.text = pemeriksaan.namaEkspedisi
            txtTglKirim.text = "Tgl ${pemeriksaan.createdDate}"
            txtPetugasPenerima.text = pemeriksaan.petugasPenerima
            txtDeliveryOrder.text = pemeriksaan.noDoSmar
            txtNamaKurir.text = pemeriksaan.namaKurir
            txtDiujikanUji.text = "eUji Komponen"
            txtTotalPackaging.text = pemeriksaan.total
            txtPending.text = "-"

            btnScanPackaging.setOnClickListener {
                openScanner(1)
            }

            btnScanSn.setOnClickListener {
                openScanner(2)
            }

            srcNoSn.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val listSnsFilter = listPemDetail.filter {
                        it.sn.toLowerCase().contains(s.toString().toLowerCase())
                    }
                    adapter.setPedList(listSnsFilter)
                }

            })

            cbNormal.setOnCheckedChangeListener { buttonView, isChecked ->
                cbCacat.isEnabled = !isChecked
                if (isChecked){
                    for (i in listPemDetail){
                        i.statusSn = "NORMAL"
                        i.isChecked = 1
                        daoSession.update(i)
                    }
                    adapter.setPedList(listPemDetail)
                }else{
                    for (i in listPemDetail){
                        i.statusSn = ""
                        i.isChecked = 0
                        daoSession.update(i)
                    }
                    adapter.setPedList(listPemDetail)
                }
            }

            cbCacat.setOnCheckedChangeListener { buttonView, isChecked ->
                cbNormal.isEnabled = !isChecked
                if (isChecked){
                    for (i in listPemDetail){
                        i.statusSn = "CACAT"
                        i.isChecked = 1
                        daoSession.update(i)
                    }
                    adapter.setPedList(listPemDetail)
                }else{
                    for (i in listPemDetail){
                        i.statusSn = ""
                        i.isChecked = 0
                        daoSession.update(i)
                    }
                    adapter.setPedList(listPemDetail)
                }
            }

            btnKomplain.setOnClickListener {
                validComplaint()
            }

            btnTerima.setOnClickListener {
                validTerima()
            }

            btnBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun validTerima() {
        for (i in listPemDetail){
            Log.d("checkList", i.statusSn)
            if (i.statusSn.isNullOrEmpty() ){
                Toast.makeText(this@PemeriksaanDetailActivity, "Tidak boleh terima dengan status kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val dialog = Dialog(this@PemeriksaanDetailActivity)
        dialog.setContentView(R.layout.popup_validation);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.txt_message) as TextView

        message.text = "Apakah anda yakin untuk menyelesaikan pemeriksaan"

        btnTidak.setOnClickListener {
            dialog.dismiss();
        }

        btnYa.setOnClickListener {
            updateData()
            dialog.dismiss()
        }

        dialog.show();
    }

    private fun updateData() {
        var packagings = ""
        for (i in listPemDetail){
            packagings += "${i.noPackaging},"
            Log.i("noPackaging", i.noPackaging)

        }
        if (packagings != "") {
            packagings = packagings.substring(0, packagings.length - 1)
        }

        for (i in listPemDetail.filter { it.isChecked == 1 }){
            i.isDone = 1
            daoSession.tPemeriksaanDetailDao.update(i)
        }

        pemeriksaan.isDone = 1
        daoSession.tPemeriksaanDao.update(pemeriksaan)

        val dialog = Dialog(this@PemeriksaanDetailActivity)
        dialog.setContentView(R.layout.popup_complaint);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        txtMessage.text = "Data sudah berhasil diterima"

        btnOk.setOnClickListener {
            dialog.dismiss();
            startActivity(Intent(this@PemeriksaanDetailActivity, PemeriksaanActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        dialog.show();
    }

    private fun validComplaint() {
        for (i in listPemDetail){
            Log.d("checkList", i.statusSn)
            if (i.statusSn.isNullOrEmpty()){
                Toast.makeText(this@PemeriksaanDetailActivity, "Tidak boleh melakukan komplain dengan status sesuai atau kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        startActivity(Intent(this@PemeriksaanDetailActivity, ComplaintActivity::class.java)
            .putExtra("noDo", noDo))
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
                val listPackagings = daoSession.tPemeriksaanDetailDao.queryBuilder().where(TPemeriksaanDetailDao.Properties.NoPackaging.eq(result.contents)).list()
                Log.d("listPackaging", listPackagings.size.toString())
                for (i in listPackagings){
                    i.statusSn = "NORMAL"
                    i.isChecked = 1
                    daoSession.tPemeriksaanDetailDao.update(i)
                }
                adapter.setPedList(listPemDetail)
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
                val listSns = daoSession.tPemeriksaanDetailDao.queryBuilder()
                    .where(TPemeriksaanDetailDao.Properties.Sn.eq(result.contents)).limit(1).unique()
                Log.i("hit sns", listSns.toString())

                listSns.statusSn = "NORMAL"
                listSns.isChecked = 1
                daoSession.tPemeriksaanDetailDao.update(listSns)

                adapter.setPedList(listPemDetail)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}