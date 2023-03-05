package dev.iconpln.mims.ui.monitoring_permintaan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMonitoringPermintaan
import dev.iconpln.mims.databinding.ActivityMonitoringPermintaanBinding
import dev.iconpln.mims.ui.monitoring_permintaan.monitoring_permintaan_detail.MonitoringPermintaanDetailActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonitoringPermintaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringPermintaanBinding
    private lateinit var daoSession: DaoSession
    private val viewModel: MonitoringPermintaanViewModel by viewModels()
    private lateinit var adapter: MonitoringPermintaanAdapter
    private var srcNoPermintaanText: String = ""
    private var srcStatusPengeluaranText: String = ""
    private var srcGudangAsalText: String = ""
    private var srcTglPermintaanText: String = ""
    private lateinit var cal: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPermintaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()

        viewModel.getMonitoringPermintaan(daoSession)

        adapter = MonitoringPermintaanAdapter(arrayListOf(), object : MonitoringPermintaanAdapter.OnAdapterListener{
            override fun onClick(mp: TMonitoringPermintaan) {
                startActivity(Intent(this@MonitoringPermintaanActivity, MonitoringPermintaanDetailActivity::class.java)
                    .putExtra("noPermintaan", mp.noPermintaan))
            }

        })

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            rvMonitoringPermintaan.adapter = adapter
            rvMonitoringPermintaan.setHasFixedSize(true)
            rvMonitoringPermintaan.layoutManager = LinearLayoutManager(this@MonitoringPermintaanActivity, LinearLayoutManager.VERTICAL, false)

            srcNoPermintaan.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    srcNoPermintaanText = s.toString()
                    viewModel.search(daoSession,srcNoPermintaanText,srcStatusPengeluaranText,srcGudangAsalText,srcTglPermintaanText)
                }

            })

            setStatusPengeluaran()
            setGudangAsal()
            setDatePicker()
        }

        viewModel.monitoringPermintaanResponse.observe(this){
            adapter.setMpList(it)
        }


    }

    private fun setGudangAsal() {
        val list = daoSession.tMonitoringPermintaanDao.queryBuilder().list()
        val listKategori: ArrayList<String> = ArrayList()
        for (i in list){
            listKategori.add(i.storLocAsalName)
        }

        val adapterKategori = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listKategori.distinct())
        binding.dropdownGudangAsal.setAdapter(adapterKategori)
        binding.dropdownGudangAsal.setOnItemClickListener { parent, view, position, id ->
            srcGudangAsalText = binding.dropdownGudangAsal.text.toString()
            viewModel.search(daoSession,srcNoPermintaanText,srcStatusPengeluaranText,srcGudangAsalText,srcTglPermintaanText)
        }
    }

    private fun setStatusPengeluaran() {
        val list = daoSession.tMonitoringPermintaanDao.queryBuilder().list()
        val listKategori: ArrayList<String> = ArrayList()
        for (i in list){
            when(i.kodePengeluaran){
                "1" -> listKategori.add("Permintaan")
                "2" -> listKategori.add("Pengeluaran")
                "3" -> listKategori.add("All")
            }
        }

        val adapterKategori = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listKategori.distinct())
        binding.dropdownStatusPengeluaran.setAdapter(adapterKategori)
        binding.dropdownStatusPengeluaran.setOnItemClickListener { parent, view, position, id ->
            when(binding.dropdownStatusPengeluaran.text.toString()){
                "Permintaan" -> srcStatusPengeluaranText = "1"
                "Pengeluaran" -> srcStatusPengeluaranText = "2"
            }
            viewModel.search(daoSession,srcNoPermintaanText,srcStatusPengeluaranText,srcGudangAsalText,srcTglPermintaanText)
        }
    }

    private fun setDatePicker() {
        val dateSetListenerPermintaan = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.txtTglMulai.text = sdf.format(cal.time)
            srcTglPermintaanText = sdf.format(cal.time)
            viewModel.search(daoSession,srcNoPermintaanText,srcStatusPengeluaranText,srcGudangAsalText,srcTglPermintaanText)

        }

        binding.cvTglPermintaan.setOnClickListener {
            DatePickerDialog(this@MonitoringPermintaanActivity, dateSetListenerPermintaan,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}