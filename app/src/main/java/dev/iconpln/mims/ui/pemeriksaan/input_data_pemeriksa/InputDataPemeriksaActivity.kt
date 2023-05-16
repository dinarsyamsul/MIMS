package dev.iconpln.mims.ui.pemeriksaan.input_data_pemeriksa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPegawaiUp3
import dev.iconpln.mims.data.local.database.TPegawaiUp3Dao
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
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import kotlin.collections.ArrayList

class InputDataPemeriksaActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityInputDataPemeriksaBinding
    private lateinit var daoSession: DaoSession
    private lateinit var pemeriksaan: TPemeriksaan
    private var noPem : String = ""
    private lateinit var ketua: TPegawaiUp3
    private lateinit var manager : TPegawaiUp3
    private lateinit var sekretaris : TPegawaiUp3
    private lateinit var anggota : TPegawaiUp3
    private var plant = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataPemeriksaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noPem = intent.getStringExtra("noPemeriksaan")!!
        plant = SharedPrefsUtils.getStringPreference(this@InputDataPemeriksaActivity, "plant", "")!!


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

        setKetua()
        setManager()
        setSekretaris()
        setAnggota()
    }

    private fun setAnggota() {
        val listName = ArrayList<String>()

        val anggotas = daoSession.tPegawaiUp3Dao
            .queryBuilder()
            .where(TPegawaiUp3Dao.Properties.KodeJabatan.eq("60"))
            .where(TPegawaiUp3Dao.Properties.Plant.eq(plant)).list()

        for (i in anggotas){
            listName.add(i.namaPegawai)
        }

        val adapterStatus = ArrayAdapter(this@InputDataPemeriksaActivity, android.R.layout.simple_dropdown_item_1line, listName)
        binding.dropdownAnggota.setAdapter(adapterStatus)
        binding.dropdownAnggota.setOnItemClickListener { parent, view, position, id ->
            anggota = anggotas[position]
        }
    }

    private fun setSekretaris() {
        val listName = ArrayList<String>()

        val sekretariss = daoSession.tPegawaiUp3Dao
            .queryBuilder()
            .where(TPegawaiUp3Dao.Properties.KodeJabatan.eq("30"))
            .where(TPegawaiUp3Dao.Properties.Plant.eq(plant))
            .where(TPegawaiUp3Dao.Properties.IsActive.eq(true)).list()

        for (i in sekretariss){
            listName.add(i.namaPegawai)
        }

        val adapterStatus = ArrayAdapter(this@InputDataPemeriksaActivity, android.R.layout.simple_dropdown_item_1line, listName)
        binding.dropdownSekretaris.setAdapter(adapterStatus)
        binding.dropdownSekretaris.setOnItemClickListener { parent, view, position, id ->
            sekretaris = sekretariss[position]
        }
    }

    private fun setManager() {
        val listName = ArrayList<String>()

        val managers = daoSession.tPegawaiUp3Dao
            .queryBuilder()
            .where(TPegawaiUp3Dao.Properties.KodeJabatan.eq("10"))
            .where(TPegawaiUp3Dao.Properties.Plant.eq(plant))
            .where(TPegawaiUp3Dao.Properties.IsActive.eq(true)).list()

        for (i in managers){
            listName.add(i.namaPegawai)
        }

        val adapterStatus = ArrayAdapter(this@InputDataPemeriksaActivity, android.R.layout.simple_dropdown_item_1line, listName)
        binding.dropdownManager.setAdapter(adapterStatus)
        binding.dropdownManager.setOnItemClickListener { parent, view, position, id ->
            manager = managers[position]
        }
    }

    private fun setKetua() {
        val listName = ArrayList<String>()
        val ketuas = daoSession.tPegawaiUp3Dao
            .queryBuilder()
            .where(TPegawaiUp3Dao.Properties.KodeJabatan.eq("20"))
            .where(TPegawaiUp3Dao.Properties.Plant.eq(plant))
            .where(TPegawaiUp3Dao.Properties.IsActive.eq(true)).list()

        for (i in ketuas){
            listName.add(i.namaPegawai)
        }

        val adapterStatus = ArrayAdapter(this@InputDataPemeriksaActivity, android.R.layout.simple_dropdown_item_1line, listName)
        binding.dropdownKetua.setAdapter(adapterStatus)
        binding.dropdownKetua.setOnItemClickListener { parent, view, position, id ->
            ketua = ketuas[position]
        }
    }

    private fun validation() {
        with(binding){
            val namaManager = manager.namaPegawai
            val namaAnggota = anggota.namaPegawai
            val namaKetua = ketua.namaPegawai
            val namaSekretaris = sekretaris.namaPegawai
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

            submitForm()
        }
    }

    private fun submitForm() {
        pemeriksaan.namaManager = manager.namaPegawai
        pemeriksaan.namaAnggota = anggota.namaPegawai
        pemeriksaan.namaKetua = ketua.namaPegawai
        pemeriksaan.namaSekretaris = sekretaris.namaPegawai
        pemeriksaan.namaAnggotaBaru = ""
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
        params.add(ReportParameter("2", reportId, "ketua", ketua.namaPegawai, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "jabatan_ketua", ketua.namaJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "nip_ketua", ketua.nip, ReportParameter.TEXT))
        params.add(ReportParameter("5", reportId, "kd_jabatan_ketua", ketua.kodeJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("6", reportId, "manager", manager.namaPegawai, ReportParameter.TEXT))
        params.add(ReportParameter("7", reportId, "jabatan_manager", manager.namaJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("8", reportId, "nip_manager", manager.nip, ReportParameter.TEXT))
        params.add(ReportParameter("9", reportId, "kd_jabatan_manager", manager.kodeJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("10", reportId, "sekretaris", sekretaris.namaPegawai, ReportParameter.TEXT))
        params.add(ReportParameter("11", reportId, "jabatan_sekretaris", sekretaris.namaJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("12", reportId, "nip_sekretaris", sekretaris.nip, ReportParameter.TEXT))
        params.add(ReportParameter("13", reportId, "kd_jabatan_sekretaris", sekretaris.kodeJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("14", reportId, "anggota", anggota.namaPegawai, ReportParameter.TEXT))
        params.add(ReportParameter("15", reportId, "jabatan_angota", anggota.namaJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("16", reportId, "nip_anggota", anggota.nip, ReportParameter.TEXT))
        params.add(ReportParameter("17", reportId, "kd_jabatan_anggota", anggota.kodeJabatan, ReportParameter.TEXT))
        params.add(ReportParameter("18", reportId, "anggota_baru", "", ReportParameter.TEXT))

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