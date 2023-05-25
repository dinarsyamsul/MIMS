package dev.iconpln.mims.ui.pemakaian

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailPemakaianUlpYantekBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pemakaian.input_pemakaian.InputPemakaianActivity
import dev.iconpln.mims.ui.ulp.penerimaan.PenerimaanUlpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList

class DetailPemakaianUlpYantekActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityDetailPemakaianUlpYantekBinding
    private lateinit var daoSession: DaoSession
    private lateinit var pemakaian: TPemakaian
    private lateinit var adapter: PemakaianDetailAdapter
    private var noTransaksi: String = ""
    private lateinit var detailPemakaians: List<TTransPemakaianDetail>
    private var noMat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemakaianUlpYantekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        noTransaksi = intent.getStringExtra("noTransaksi")!!

        pemakaian = daoSession.tPemakaianDao.queryBuilder()
            .where(TPemakaianDao.Properties.NoTransaksi.eq(noTransaksi)).list().get(0)

        detailPemakaians = daoSession.tTransPemakaianDetailDao.queryBuilder()
            .where(TTransPemakaianDetailDao.Properties.NoTransaksi.eq(noTransaksi)).list()

        adapter = PemakaianDetailAdapter(arrayListOf(), object : PemakaianDetailAdapter.OnAdapterListener{
            override fun onClick(pemakaian: TTransPemakaianDetail) {
                if (pemakaian.isDone == 1){
                    Toast.makeText(this@DetailPemakaianUlpYantekActivity, "Anda sudah menyelesaikan pemakaian ini", Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@DetailPemakaianUlpYantekActivity, InputSnPemakaianActivity::class.java)
                        .putExtra("noTransaksi", pemakaian.noTransaksi)
                        .putExtra("noMat",pemakaian.nomorMaterial))
                }
            }

        },daoSession)

        adapter.setpemakaianList(detailPemakaians)

        with(binding){
            txtNoReservasi.text = pemakaian.noReservasi
            txtTotalData.text = "Total ${detailPemakaians.size} data"

            rvPemakaianUlp.adapter = adapter
            rvPemakaianUlp.setHasFixedSize(true)
            rvPemakaianUlp.layoutManager = LinearLayoutManager(this@DetailPemakaianUlpYantekActivity, LinearLayoutManager.VERTICAL, false)

            btnBack.setOnClickListener { onBackPressed() }
            btnSimpan.setOnClickListener { validation() }

            btnPemakaian.setOnClickListener {
                if (pemakaian.isDonePemakai == 1){
                    Toast.makeText(this@DetailPemakaianUlpYantekActivity, "Anda sudah melakukan input pemakaian",Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@DetailPemakaianUlpYantekActivity, InputPemakaianActivity::class.java)
                        .putExtra("noTransaksi", noTransaksi))
                }
            }

            srcNomorMaterial.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    noMat = s.toString()
                    doSearch()
                }

            })
        }
    }

    private fun doSearch() {
       val listSearch = daoSession.tTransPemakaianDetailDao.queryBuilder()
            .where(TTransPemakaianDetailDao.Properties.NoTransaksi.eq(noTransaksi))
           .where(TTransPemakaianDetailDao.Properties.NomorMaterial.like("%"+noMat+"%")).list()

        adapter.setpemakaianList(listSearch)
    }

    private fun validation() {
        for (i in detailPemakaians){
            val jumlahPemakaian = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
                .where(TListSnMaterialPemakaianUlpDao.Properties.NoTransaksi.eq(i.noTransaksi))
                .where(TListSnMaterialPemakaianUlpDao.Properties.NoMaterial.eq(i.nomorMaterial)).list()

            if (i.isDone == 0){
                Toast.makeText(this@DetailPemakaianUlpYantekActivity, "Kamu belum menyelesaikan semua pemakaian", Toast.LENGTH_SHORT).show()
                return
            }

            if (i.qtyReservasi != jumlahPemakaian.size.toString()){
                Toast.makeText(this@DetailPemakaianUlpYantekActivity, "Jumlah reservasi ${i.nomorMaterial} masih kurang", Toast.LENGTH_SHORT).show()
                return
            }
        }

        submitForm()
    }

    private fun submitForm() {
        val detailPemakaian = daoSession.tTransPemakaianDetailDao.queryBuilder()
            .where(TTransPemakaianDetailDao.Properties.NoTransaksi.eq(noTransaksi)).list()
        
        var materials = ""
        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        pemakaian.isDone = 1
        pemakaian.statusPemakaian = "TERPAKAI"
        daoSession.tPemakaianDao.update(pemakaian)

        for (i in detailPemakaian){
            val jumlahPemakaian = daoSession.tListSnMaterialPemakaianUlpDao.queryBuilder()
                .where(TListSnMaterialPemakaianUlpDao.Properties.NoTransaksi.eq(i.noTransaksi))
                .where(TListSnMaterialPemakaianUlpDao.Properties.NoMaterial.eq(i.nomorMaterial)).list().size

            materials += "${i.nomorMaterial},${jumlahPemakaian};"
            Log.d("checkMaterials", materials)
        }

        if (materials != "") {
            materials = materials.substring(0, materials.length - 1)
        }

        //region Add report visit to queue
        var jwtWeb = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity, "jwtWeb", "")
        var jwtMobile = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity,"jwt","")
        var jwt = "$jwtMobile;$jwtWeb"
        var plant = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity,"plant","")
        var storloc = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity,"storloc","")
        var username = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemakaian Ulp" + username + "_" + pemakaian.noTransaksi + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data pemakaian Ulp"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()

        params.add(ReportParameter("1", reportId, "no_transaksi", pemakaian.noTransaksi!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "materials",materials , ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendReportPemakaianUlpSelesai(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)
    }

    override fun setLoading(show: Boolean, title: String, message: String) {

    }

    override fun setFinish(result: Boolean, message: String) {
        Toast.makeText(this@DetailPemakaianUlpYantekActivity, message,Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@DetailPemakaianUlpYantekActivity, PemakaianActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onResume() {
        Log.d("checkRefresh","refresh recylerview detail oemakaian ulp")
        adapter.setpemakaianList(detailPemakaians)
        super.onResume()
    }
}