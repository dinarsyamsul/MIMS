package dev.iconpln.mims.ui.pengiriman

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TLokasi
import dev.iconpln.mims.data.local.database.TLokasiDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityUpdateLokasiBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

class UpdateLokasiActivity : AppCompatActivity(), Loadable {
    private lateinit var rvAdapter: HistoryAdapter
    private lateinit var binding: ActivityUpdateLokasiBinding
    private lateinit var daoSession: DaoSession
    private var doMims:String?=""
    private var progressDialog: AlertDialog? = null
    private lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doMims=intent.getStringExtra(EXTRA_NO_DOMIMS)
        session = SessionManager(this)
        daoSession = (application as MyApplication).daoSession!!

        rvAdapter = HistoryAdapter(arrayListOf(), object: HistoryAdapter.OnAdapterListener {
            override fun onClick(data: TLokasi) {

            }
        })

        binding.rvHistory.apply {
            adapter = rvAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@UpdateLokasiActivity)
        }

        binding.btnUpdate.setOnClickListener {
            validateForm()
        }

        fetchDataLocal()
    }

    private fun fetchDataLocal() {
        val listDataPengiriman = daoSession.tLokasiDao.queryBuilder().where(TLokasiDao.Properties.NoDoSns.eq(doMims)).list()
        rvAdapter.setData(listDataPengiriman)
    }

    companion object{
        const val EXTRA_NO_DOMIMS = "extra_no_do_mims"
    }

    private fun submitForm() {
        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //Insert lokasis
        var mLokasi = TLokasi()
        mLokasi.updateDate = currentDateTime
        mLokasi.noDoSns = doMims
        mLokasi.ket = binding.txtLokasi.text.toString()
        daoSession.tLokasiDao.insert(mLokasi)



        //region Add report visit to queue
        var jwt = SharedPrefsUtils.getStringPreference(this@UpdateLokasiActivity,"jwt","")
        val reportId = "V_" + session.username + "_" + doMims + "_" + DateTime.now().toString(Config.DATETIME)
        val reportName = "Update Lokasi"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "do_mims", mLokasi.noDoSns, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "lokasi", mLokasi.ket, ReportParameter.TEXT))
       val report = GenericReport(reportId, session.username, reportName, reportDescription, ApiConfig.insertLokasi(), currentDate, Config.NO_CODE, currentUtc, params,jwt!!)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()
    }

    private fun validateForm(){
        if (binding.txtLokasi.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Harap isi lokasi terkini", Toast.LENGTH_SHORT).show()
            return
        }

        submitForm()
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
            fetchDataLocal()
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}