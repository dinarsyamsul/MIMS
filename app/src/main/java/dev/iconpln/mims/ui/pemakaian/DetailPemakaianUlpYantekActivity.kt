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
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemakaian
import dev.iconpln.mims.data.local.database.TPemakaianDao
import dev.iconpln.mims.data.local.database.TTransPemakaianDetail
import dev.iconpln.mims.data.local.database.TTransPemakaianDetailDao
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
                startActivity(Intent(this@DetailPemakaianUlpYantekActivity, InputSnPemakaianActivity::class.java)
                    .putExtra("noTransaksi", pemakaian.noTransaksi))
            }

        })

        adapter.setpemakaianList(detailPemakaians)

        with(binding){
            txtNoPemakaian.text = pemakaian.noReservasi
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
            if (i.isDone == 0){
                Toast.makeText(this@DetailPemakaianUlpYantekActivity, "Kamu belum menyelesaikan semua pemakaian", Toast.LENGTH_SHORT).show()
                return
            }
        }

        submitForm()
    }

    private fun submitForm() {
        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //region Add report visit to queue
        var jwt = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity,"jwt","")
        var plant = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity,"plant","")
        var storloc = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity,"storloc","")
        var username = SharedPrefsUtils.getStringPreference(this@DetailPemakaianUlpYantekActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemakaian Ulp" + username + "_" + pemakaian.noTransaksi + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data pemakaian Ulp"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()

        params.add(ReportParameter("1", reportId, "no_transaksi", pemakaian.noTransaksi!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "user_plant", plant!!, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "user_loc", storloc!!, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "username",username!! , ReportParameter.TEXT))

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
}