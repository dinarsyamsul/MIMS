package dev.iconpln.mims.ui.monitoring_complaint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMonitoringComplaint
import dev.iconpln.mims.data.local.database.TMonitoringComplaintDao
import dev.iconpln.mims.data.local.database.TMonitoringComplaintDetailDao
import dev.iconpln.mims.databinding.ActivityMonitoringComplaintBinding
import dev.iconpln.mims.ui.monitoring_complaint.detail_monitoring_complaint.MonitoringComplaintDetailActivity
import dev.iconpln.mims.utils.SharedPrefsUtils

class MonitoringComplaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringComplaintBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listComplaint: List<TMonitoringComplaint>
    private lateinit var adapter: MonitoringComplaintAdapter
    private var subrole = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        subrole = SharedPrefsUtils.getIntegerPreference(this,"subroleId",0)

        listComplaint = daoSession.tMonitoringComplaintDao.queryBuilder()
            .orderAsc(TMonitoringComplaintDao.Properties.Status)
            .list()

        adapter = MonitoringComplaintAdapter(arrayListOf(), object : MonitoringComplaintAdapter.OnAdapterListener{
            override fun onClick(mp: TMonitoringComplaint) {
                if (subrole != 3){
                    val checkComplaintDetail = daoSession.tMonitoringComplaintDetailDao.queryBuilder()
                        .where(TMonitoringComplaintDetailDao.Properties.NoKomplain.eq(mp.noKomplain))
                        .where(TMonitoringComplaintDetailDao.Properties.Status.eq("SEDANG KOMPLAIN")).list().size
                    Log.d("checkComplaintDetail", checkComplaintDetail.toString())

                    if (checkComplaintDetail > 0){
                        startActivity(Intent(this@MonitoringComplaintActivity, MonitoringComplaintDetailActivity::class.java)
                            .putExtra("noKomplain", mp.noKomplain)
                            .putExtra("noDo", mp.noDoSmar)
                            .putExtra("status", mp.status))
                    }else{
                        Toast.makeText(this@MonitoringComplaintActivity, "Komplain ini sudah di selesai di periksa", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    startActivity(Intent(this@MonitoringComplaintActivity, MonitoringComplaintDetailActivity::class.java)
                        .putExtra("noKomplain", mp.noKomplain)
                        .putExtra("noDo", mp.noDoSmar)
                        .putExtra("status", mp.status))
                }
            }

        },daoSession,subrole)

        adapter.setComplaint(listComplaint)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            tvTotalData.text = "Total data ${listComplaint.size}"
            srcNomorPoDo.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val searchList = daoSession.tMonitoringComplaintDao.queryBuilder()
                        .whereOr(TMonitoringComplaintDao.Properties.NoDoSmar.like("%"+s.toString()+"%"),
                            TMonitoringComplaintDao.Properties.PoSapNo.like("%"+s.toString()+"%"))
                        .orderAsc(TMonitoringComplaintDao.Properties.Status)
                        .list()

                    binding.rvMonitoringKomplain.adapter = null
                    binding.rvMonitoringKomplain.layoutManager = null
                    binding.rvMonitoringKomplain.adapter = adapter
                    binding.rvMonitoringKomplain.setHasFixedSize(true)
                    binding.rvMonitoringKomplain.layoutManager = LinearLayoutManager(this@MonitoringComplaintActivity, LinearLayoutManager.VERTICAL, false)
                    adapter.setComplaint(searchList)
                }

            })

            val statusArray = arrayOf(
                "TERBARU","TERLAMA"
            )
            val adapterStatus = ArrayAdapter(this@MonitoringComplaintActivity, android.R.layout.simple_dropdown_item_1line, statusArray)
            dropdownUrutkan.setAdapter(adapterStatus)
            dropdownUrutkan.setOnItemClickListener { parent, view, position, id ->
                if (statusArray[position] == "TERBARU"){
                    val searchList = daoSession.tMonitoringComplaintDao.queryBuilder()
                        .orderDesc(TMonitoringComplaintDao.Properties.TanggalPO).list()

                    binding.rvMonitoringKomplain.adapter = null
                    binding.rvMonitoringKomplain.layoutManager = null
                    binding.rvMonitoringKomplain.adapter = adapter
                    binding.rvMonitoringKomplain.setHasFixedSize(true)
                    binding.rvMonitoringKomplain.layoutManager = LinearLayoutManager(this@MonitoringComplaintActivity, LinearLayoutManager.VERTICAL, false)
                    adapter.setComplaint(searchList)
                }else{
                    val searchList = daoSession.tMonitoringComplaintDao.queryBuilder()
                        .orderAsc(TMonitoringComplaintDao.Properties.TanggalPO).list()

                    binding.rvMonitoringKomplain.adapter = null
                    binding.rvMonitoringKomplain.layoutManager = null
                    binding.rvMonitoringKomplain.adapter = adapter
                    binding.rvMonitoringKomplain.setHasFixedSize(true)
                    binding.rvMonitoringKomplain.layoutManager = LinearLayoutManager(this@MonitoringComplaintActivity, LinearLayoutManager.VERTICAL, false)
                    adapter.setComplaint(searchList)
                }

            }

            rvMonitoringKomplain.adapter = adapter
            rvMonitoringKomplain.setHasFixedSize(true)
            rvMonitoringKomplain.layoutManager = LinearLayoutManager(this@MonitoringComplaintActivity, LinearLayoutManager.VERTICAL,false)
        }
    }
}