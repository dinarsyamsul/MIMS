package dev.iconpln.mims.ui.pemeriksaan.input_data_pemeriksa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityInputDataPemeriksaBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList

class InputDataPemeriksaActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityInputDataPemeriksaBinding
    private lateinit var daoSession: DaoSession
    private lateinit var pemeriksaan: TPemeriksaan
    private var noPem : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataPemeriksaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noPem = intent.getStringExtra("noPemeriksaan")!!

        pemeriksaan = daoSession.tPemeriksaanDao.queryBuilder()
            .where(TPemeriksaanDao.Properties.NoPemeriksaan.eq(noPem)).list()[0]

        with(binding){
            txtDeliveryOrder.text = pemeriksaan.noDoSmar
            txtNamaKurir.text = pemeriksaan.namaKurir
            txtPending.text = "-"
            txtDiajukanUji.text = "-"
            txtPetugasPenerima.text = pemeriksaan.petugasPenerima
            txtKurirPengiriman.text = pemeriksaan.expeditions
            txtTglDiterima.text = pemeriksaan.tanggalDiterima
            txtTotalPackaging.text = pemeriksaan.total

            btnBack.setOnClickListener { onBackPressed() }
            btnSimpan.setOnClickListener { validation() }
            btnTambahAnggota.setOnClickListener {
                lblNewAnggota.visibility = View.VISIBLE
                edtNewAnggota.visibility = View.VISIBLE
                btnTambahAnggota.visibility = View.GONE
            }
        }
    }

    private fun validation() {
        with(binding){
            val namaManager = edtNamaManager.text.toString()
            val namaAnggota = edtAnggota.text.toString()
            val namaKetua = edtKetuaPemeriksa.text.toString()
            val namaSekretaris = edtSekretaris.text.toString()
            val namaAnggotaBaru = edtNewAnggota.text.toString()

            if (namaManager.isNullOrEmpty()){
                Toast.makeText(this@InputDataPemeriksaActivity, "Pastikan anda mengisi semua data yang akan di kirimm", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaAnggota.isNullOrEmpty()){
                Toast.makeText(this@InputDataPemeriksaActivity, "Pastikan anda mengisi semua data yang akan di kirimm", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaKetua.isNullOrEmpty()){
                Toast.makeText(this@InputDataPemeriksaActivity, "Pastikan anda mengisi semua data yang akan di kirimm", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaSekretaris.isNullOrEmpty()){
                Toast.makeText(this@InputDataPemeriksaActivity, "Pastikan anda mengisi semua data yang akan di kirimm", Toast.LENGTH_SHORT).show()
                return
            }

            submitForm(namaManager,namaAnggota,namaKetua,namaSekretaris,namaAnggotaBaru)
        }
    }

    private fun submitForm(
        namaManager: String,
        namaAnggota: String,
        namaKetua: String,
        namaSekretaris: String,
        namaAnggotaBaru: String
    ) {
        pemeriksaan.namaManager = namaManager
        pemeriksaan.namaAnggota = namaAnggota
        pemeriksaan.namaKetua = namaKetua
        pemeriksaan.namaSekretaris = namaSekretaris
        pemeriksaan.namaAnggotaBaru = namaAnggotaBaru
        daoSession.tPemeriksaanDao.update(pemeriksaan)

        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //region Add report visit to queue
        var jwt = SharedPrefsUtils.getStringPreference(this@InputDataPemeriksaActivity,"jwt","")
        var username = SharedPrefsUtils.getStringPreference(this@InputDataPemeriksaActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemeriksaan" + username + "_" + pemeriksaan.noDoSmar + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data Petugas Pemeriksaan"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()
        params.add(ReportParameter("1", reportId, "no_do_smar", pemeriksaan.noDoSmar, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "ketua", namaKetua, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "manager", namaManager, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "sekretaris", namaSekretaris, ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "anggota", namaAnggota, ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "anggota_baru", namaAnggotaBaru, ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendPemeriksaanPerson(), currentDate, Config.NO_CODE, currentUtc, params)
        reports.add(report)
        //endregion

        val task = TambahReportTask(this, reports)
        task.execute()

        val iService = Intent(applicationContext, ReportUploader::class.java)
        startService(iService)

    }

    override fun setLoading(show: Boolean, title: String, message: String) {}

    override fun setFinish(result: Boolean, message: String) {
        if (result) {
            Log.i("finish","Yes")
        }
        startActivity(Intent(this@InputDataPemeriksaActivity, PemeriksaanActivity::class.java )
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}