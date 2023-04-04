package dev.iconpln.mims.ui.pengiriman

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TLokasi
import dev.iconpln.mims.data.local.database.TLokasiDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.response.DatasItemLokasi
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityUpdateLokasiBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

class UpdateLokasiActivity : AppCompatActivity(), Loadable {
    private lateinit var rvAdapter: HistoryAdapter
    private lateinit var binding: ActivityUpdateLokasiBinding
    private lateinit var daoSession: DaoSession
    private var doMims:String?=""
    private var kodeStatus:String?=""
    private var progressDialog: AlertDialog? = null
    private lateinit var session: SessionManager
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this@UpdateLokasiActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        doMims=intent.getStringExtra(EXTRA_NO_DOMIMS)
        kodeStatus=intent.getStringExtra("kodeStatus")

        session = SessionManager(this)
        daoSession = (application as MyApplication).daoSession!!

        rvAdapter = HistoryAdapter(arrayListOf(), object: HistoryAdapter.OnAdapterListener {
            override fun onClick(data: DatasItemLokasi) {
                if (kodeStatus == "105"){
                    Toast.makeText(this@UpdateLokasiActivity, "Tidak dapat melakukan delete lokasi, karena do ini sudah berstatus received", Toast.LENGTH_SHORT).show()
                }else{
                    val dialog = Dialog(this@UpdateLokasiActivity)
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
                    txtMessage.text = "Jika ya, maka lokasi akan di hapus"
                    icon.setImageResource(R.drawable.ic_warning)

                    btnYa.setOnClickListener {
                        deleteSn(data)
                        dialog.dismiss();

                    }

                    btnTidak.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show();
                }
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

        //binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnClose.setOnClickListener { finish() }

        fetchDataLocal()
    }

    private fun deleteSn(data: DatasItemLokasi) {
        dialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@UpdateLokasiActivity)
                .deleteLokasi(data.id!!)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        if (response.body()?.status == "success"){
                            dialog.dismiss()
                            //Insert lokasis
//                            daoSession.tLokasiDao.delete(data)

                            fetchDataLocal()
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@UpdateLokasiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        e.printStackTrace()
                    }
                }else {
                    dialog.dismiss()
                    Toast.makeText(this@UpdateLokasiActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchDataLocal() {
//        val listDataPengiriman = daoSession.tLokasiDao.queryBuilder().where(TLokasiDao.Properties.NoDoSns.eq(doMims)).list()
        dialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@UpdateLokasiActivity)
                .getLokasi(doMims!!)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        if (response.body()?.status == "success"){
                            dialog.dismiss()
                            rvAdapter.setData(response.body()!!.datas)
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@UpdateLokasiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        e.printStackTrace()
                    }
                }else {
                    dialog.dismiss()
                    Toast.makeText(this@UpdateLokasiActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
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

        dialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@UpdateLokasiActivity)
                .sendLokasi(doMims!!,binding.txtLokasi.text.toString())
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        if (response.body()?.status == "success"){
                            dialog.dismiss()
                            //Insert lokasis
                            var mLokasi = TLokasi()
                            mLokasi.updateDate = currentDateTime
                            mLokasi.noDoSns = doMims
                            mLokasi.ket = binding.txtLokasi.text.toString()
                            daoSession.tLokasiDao.insert(mLokasi)

                            fetchDataLocal()
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@UpdateLokasiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        dialog.dismiss()
                        binding.progressBar.visibility = View.GONE
                        e.printStackTrace()
                    }
                }else {
                    dialog.dismiss()
                    Toast.makeText(this@UpdateLokasiActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }

//        //region Add report visit to queue
//        var jwt = SharedPrefsUtils.getStringPreference(this@UpdateLokasiActivity,"jwt","")
//        var username = SharedPrefsUtils.getStringPreference(this@UpdateLokasiActivity, "username","14.Hexing_Electrical")
//        val reportId = "V_" + username + "_" + doMims + "_" + DateTime.now().toString(Config.DATETIME)
//        val reportName = "Update Lokasi"
//        val reportDescription = "$reportName: "+ " (" + reportId + ")"
//        val params = ArrayList<ReportParameter>()
//        params.add(ReportParameter("1", reportId, "do_mims", mLokasi.noDoSns, ReportParameter.TEXT))
//        params.add(ReportParameter("2", reportId, "lokasi", mLokasi.ket, ReportParameter.TEXT))
//        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.insertLokasi(), currentDate, Config.NO_CODE, currentUtc, params)
//        reports.add(report)
//        //endregion
//
//        val task = TambahReportTask(this, reports)
//        task.execute()
//
//        val iService = Intent(applicationContext, ReportUploader::class.java)
//        startService(iService)
    }

    private fun validateForm(){
        if (binding.txtLokasi.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Harap isi lokasi terkini", Toast.LENGTH_SHORT).show()
            return
        }

        if (kodeStatus == "105"){
            Toast.makeText(this, "Tidak dapat melakukan update lokasi, karena do ini sudah berstatus received", Toast.LENGTH_SHORT).show()
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