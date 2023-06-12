package dev.iconpln.mims.ui.pemakaian.input_pemakaian

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemakaian
import dev.iconpln.mims.data.local.database.TPemakaianDao
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityInputPemakaianBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.pemakaian.DetailPemakaianUlpYantekActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.ArrayList

class InputPemakaianActivity : AppCompatActivity(), Loadable {
    private lateinit var binding: ActivityInputPemakaianBinding
    private lateinit var daoSession: DaoSession
    private lateinit var pemakaian: TPemakaian
    private var noTransaksi = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputPemakaianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!

        noTransaksi = intent.getStringExtra("noTransaksi")!!

        pemakaian = daoSession.tPemakaianDao.queryBuilder()
            .where(TPemakaianDao.Properties.NoTransaksi.eq(noTransaksi)).list().get(0)

        with(binding){
            edtNoPk.setText(pemakaian.noPk)
            edtNamaKegiatan.setText(pemakaian.namaKegiatan)
            edtNamaPelanggan.setText(pemakaian.namaPelanggan)
            edtLokasi.setText(pemakaian.lokasi)
            edtPemeriksa.setText(pemakaian.pemeriksa)
            edtPenerima.setText(pemakaian.penerima)
            edtKepalaGudang.setText("ADMIN GUDANG ULP")
            edtPejabatPengesahan.setText("MANAJER ULP")

            if (pemakaian.isDonePemakai == 1){
                btnSimpan.isEnabled = false
                btnSimpan.setBackgroundColor(Color.GRAY)
            }

            btnBack.setOnClickListener { onBackPressed() }
            btnSimpan.setOnClickListener {
                validation()
            }
        }
    }

    private fun validation() {
        with(binding){
            val noPk = edtNoPk.text.toString()
            val namaKegiatan = edtNamaKegiatan.text.toString()
            val namaPelanggan = edtNamaPelanggan.text.toString()
            val lokasi = edtLokasi.text.toString()
            val pemeriksa = edtPemeriksa.text.toString()
            val penerima = edtPenerima.text.toString()
            val kepalaGudang = edtKepalaGudang.text.toString()
            val pejabatPengesahan = edtPejabatPengesahan.text.toString()

            if (noPk.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaKegiatan.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (namaPelanggan.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (lokasi.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (pemeriksa.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (penerima.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (kepalaGudang.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (pejabatPengesahan.isNullOrEmpty()){
                Toast.makeText(this@InputPemakaianActivity, "Mohon lengkapi data yang di butuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            submitForm(noPk,namaKegiatan,namaPelanggan,lokasi,pemeriksa,penerima,kepalaGudang,pejabatPengesahan)
        }
    }

    private fun submitForm(
        noPk: String,
        namaKegiatan: String,
        namaPelanggan: String,
        lokasi: String,
        pemeriksa: String,
        penerima: String,
        kepalaGudang: String,
        pejabatPengesahan: String
    ) {
        pemakaian.noPk = noPk
        pemakaian.namaKegiatan = namaKegiatan
        pemakaian.namaPelanggan = namaPelanggan
        pemakaian.lokasi = lokasi
        pemakaian.pemeriksa = pemeriksa
        pemakaian.penerima = penerima
        pemakaian.kepalaGudang = kepalaGudang
        pemakaian.isDonePemakai = 1
        daoSession.tPemakaianDao.update(pemakaian)

        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        //region Add report visit to queue
        var jwt = SharedPrefsUtils.getStringPreference(this@InputPemakaianActivity,"jwt","")
        var username = SharedPrefsUtils.getStringPreference(this@InputPemakaianActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_input_pemakaian Ulp" + username + "_" + pemakaian.noTransaksi + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data input pemakaian Ulp"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()

        params.add(ReportParameter("1", reportId, "no_transaksi", noTransaksi, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "no_pk", noPk, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "nama_kegiatan", namaKegiatan, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "nama_pelanggan",namaPelanggan , ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "lokasi",lokasi , ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "pemeriksa",pemeriksa , ReportParameter.TEXT))
        params.add(ReportParameter("7", reportId, "penerima",penerima , ReportParameter.TEXT))
        params.add(ReportParameter("8", reportId, "kepala_gudang",kepalaGudang , ReportParameter.TEXT))
        params.add(ReportParameter("9", reportId, "pejabat_pengesahan",pejabatPengesahan , ReportParameter.TEXT))


        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendReportPemakai(), currentDate, Config.NO_CODE, currentUtc, params)
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
        Toast.makeText(this@InputPemakaianActivity, message,Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(this@InputPemakaianActivity, DetailPemakaianUlpYantekActivity::class.java)
                .putExtra("noTransaksi", noTransaksi)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}