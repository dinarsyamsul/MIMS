package dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.database_local.ReportParameter
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailPemeriksaanBinding
import dev.iconpln.mims.databinding.ActivityPetugasPemeriksaanBinding
import dev.iconpln.mims.tasks.Loadable
import dev.iconpln.mims.tasks.TambahReportTask
import dev.iconpln.mims.ui.monitoring_permintaan.MonitoringPermintaanActivity
import dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail.DetailPemeriksaanAdapter
import dev.iconpln.mims.ui.ulp.penerimaan.PenerimaanUlpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.DateTimeUtils
import dev.iconpln.mims.utils.SharedPrefsUtils
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.w3c.dom.Text
import java.util.ArrayList

class DetailPemeriksaanActivity : AppCompatActivity(),Loadable {
    private lateinit var binding: ActivityDetailPemeriksaanBinding
    private lateinit var daoSession: DaoSession
    private var noRepackaging: String = ""
    private var noPengiriman: String = ""
    private lateinit var detailPemeriksaans: List<TTransPenerimaanDetailUlp>
    private lateinit var penerimaans: TTransPenerimaanUlp
    private lateinit var adapter: DetailPemeriksaanUlpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        noRepackaging = intent.getStringExtra("noRepackaging")!!
        noPengiriman = intent.getStringExtra("noPengiriman").toString()

        penerimaans = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .where(TTransPenerimaanUlpDao.Properties.NoPengiriman.eq(noPengiriman)).list()[0]

        detailPemeriksaans = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
            .where(TTransPenerimaanDetailUlpDao.Properties.NoRepackaging.eq(noRepackaging)).list()

        adapter = DetailPemeriksaanUlpAdapter(arrayListOf(), object : DetailPemeriksaanUlpAdapter.OnAdapterListener{
            override fun onClick(po: TTransPenerimaanDetailUlp) {
                if (po.isDone == 1){
                    Toast.makeText(this@DetailPemeriksaanActivity, "Kamu sudah menyelesaikan material ini", Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@DetailPemeriksaanActivity, InputSnPemeriksaanActivity::class.java)
                        .putExtra("noMaterial", po.noMaterial)
                        .putExtra("noRepackaging", po.noRepackaging)
                        .putExtra("noTransaksi", po.noTransaksi))
                }
            }

        })

        adapter.setPenerimaanList(detailPemeriksaans)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            srcNoSn.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    detailPemeriksaans = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
                        .where(TTransPenerimaanDetailUlpDao.Properties.NoRepackaging.eq(noRepackaging))
                        .where(TTransPenerimaanDetailUlpDao.Properties.NoMaterial.like("%"+s.toString()+"%")).list()
                    adapter.setPenerimaanList(detailPemeriksaans)
                }

            })

            txtNoPengiriman.text = penerimaans.noPengiriman
            txtTanggalPemeriksaan.text = penerimaans.tanggalPemeriksaan
            txtNoPengiriman2.text = penerimaans.noPengiriman
            txtNoPermintaan.text = penerimaans.noPermintaan

            txtTotalData.text = "Total ${detailPemeriksaans.size} data"

            rvDetailPemeriksaan.adapter = adapter
            rvDetailPemeriksaan.layoutManager = LinearLayoutManager(this@DetailPemeriksaanActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailPemeriksaan.setHasFixedSize(true)

            btnLanjutpenerimaanulp.setOnClickListener { validation() }
        }
    }

    private fun validation() {
        for (i in detailPemeriksaans){
            if (i.isDone == 0){
                Toast.makeText(this@DetailPemeriksaanActivity, "Selesaikan semua no material", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val dialog = Dialog(this@DetailPemeriksaanActivity)
        dialog.setContentView(R.layout.popup_validation)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val icon = dialog.findViewById(R.id.imageView11) as ImageView
        val btnOk = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.message) as TextView
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        icon.setImageResource(R.drawable.ic_doc_diterima)
        message.text = "Selesai Periksa"
        txtMessage.text = "Apakah anda yakin ingin selesai?"


        btnTidak.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            dialog.dismiss();
            submitForm()
        }
        dialog.show();
    }

    private fun submitForm() {
        val reports = ArrayList<GenericReport>()
        val currentDate = LocalDateTime.now().toString(Config.DATE)
        val currentDateTime = LocalDateTime.now().toString(Config.DATETIME)
        val currentUtc = DateTimeUtils.currentUtc
        Log.i("datime","${currentDateTime}")

        penerimaans.isDonePemeriksaan = 1
        penerimaans.tempStatusPemeriksaan = "SUDAH DIPERIKSA"
        penerimaans.tanggalPemeriksaan = currentDate
        daoSession.tTransPenerimaanUlpDao.update(penerimaans)

        //region Add report visit to queue
        var jwt = SharedPrefsUtils.getStringPreference(this@DetailPemeriksaanActivity,"jwt","")
        var plant = SharedPrefsUtils.getStringPreference(this@DetailPemeriksaanActivity,"plant","")
        var storloc = SharedPrefsUtils.getStringPreference(this@DetailPemeriksaanActivity,"storloc","")
        var username = SharedPrefsUtils.getStringPreference(this@DetailPemeriksaanActivity, "username","14.Hexing_Electrical")
        val reportId = "temp_pemeriksaanUp3" + username + "_" + penerimaans.noPermintaan + "_" + DateTime.now().toString(
            Config.DATETIME)
        val reportName = "Update Data pemeriksaan up 3"
        val reportDescription = "$reportName: "+ " (" + reportId + ")"
        val params = ArrayList<ReportParameter>()

        params.add(ReportParameter("1", reportId, "no_transaksi", detailPemeriksaans[0].noTransaksi!!, ReportParameter.TEXT))
        params.add(ReportParameter("2", reportId, "user_plant", plant!!, ReportParameter.TEXT))
        params.add(ReportParameter("3", reportId, "user_loc", storloc!!, ReportParameter.TEXT))
        params.add(ReportParameter("4", reportId, "username",username!! , ReportParameter.TEXT))

        val report = GenericReport(reportId, jwt!!, reportName, reportDescription, ApiConfig.sendReportPenerimaanUlpSelesai(), currentDate, Config.NO_CODE, currentUtc, params)
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
        Toast.makeText(this@DetailPemeriksaanActivity, message,Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@DetailPemeriksaanActivity, PenerimaanUlpActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}