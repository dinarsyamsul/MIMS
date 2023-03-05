package dev.iconpln.mims.ui.monitoring_permintaan.monitoring_permintaan_detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMonitoringPermintaan
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDao
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDetail
import dev.iconpln.mims.databinding.ActivityMonitoringDetailBinding
import dev.iconpln.mims.databinding.ActivityMonitoringPermintaanDetailBinding
import dev.iconpln.mims.ui.monitoring_permintaan.MonitoringPermintaanViewModel
import dev.iconpln.mims.ui.monitoring_permintaan.input_sn_monitoring.InputSnMonitoringPermintaanActivity

class MonitoringPermintaanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringPermintaanDetailBinding
    private lateinit var daoSession: DaoSession
    private val viewModel: MonitoringPermintaanViewModel by viewModels()
    private lateinit var adapter: MonitoringPermintaanDetailAdapter
    private var noPermintaan: String = ""
    private var srcNoMaterialTxt: String = ""
    private lateinit var monitoringPenerimaan: TMonitoringPermintaan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPermintaanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        noPermintaan = intent.getStringExtra("noPermintaan")!!

        monitoringPenerimaan = daoSession.tMonitoringPermintaanDao.queryBuilder()
            .where(TMonitoringPermintaanDao.Properties.NoPermintaan.eq(noPermintaan)).limit(1).unique()

        viewModel.getMonitoringPermintaanDetail(daoSession, noPermintaan)

        adapter = MonitoringPermintaanDetailAdapter(arrayListOf(), object : MonitoringPermintaanDetailAdapter.OnAdapterListener{
            override fun onClick(mpd: TMonitoringPermintaanDetail) {
                startActivity(Intent(this@MonitoringPermintaanDetailActivity, InputSnMonitoringPermintaanActivity::class.java)
                    .putExtra("noPermintaan", mpd.nomorMaterial))
            }

        })

        viewModel.monitoringPermintaanDetailResponse.observe(this){
            adapter.setMpList(it)
        }

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            txtNoPermintaan.text = noPermintaan
            txtNoPackaging.text = monitoringPenerimaan.noRepackaging
            txtGudangTujuan.text = monitoringPenerimaan.storLocTujuanName

            rvMonitoringPermintaanDetail.adapter = adapter
            rvMonitoringPermintaanDetail.layoutManager = LinearLayoutManager(this@MonitoringPermintaanDetailActivity, LinearLayoutManager.VERTICAL,false)
            rvMonitoringPermintaanDetail.setHasFixedSize(true)

            srcNoMaterial.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    srcNoMaterialTxt = s.toString()
                    viewModel.searchDetail(daoSession, srcNoMaterialTxt)
                }

            })
        }

    }
}