package dev.iconpln.mims.ui.arttribute_material

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMaterial
import dev.iconpln.mims.data.local.database.TMaterialDao
import dev.iconpln.mims.databinding.ActivityDataAtributMaterialBinding
import dev.iconpln.mims.ui.arttribute_material.detail_attribute.DetailDataAtributeMaterialActivity
import dev.iconpln.mims.utils.Config
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataAtributMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataAtributMaterialBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var adapter: DataAttributeAdapter
    private lateinit var daoSession: DaoSession
    private var kategori: String = ""
    private var tahun: String = ""
    private var snBatch: String = ""
    private var startDate = ""
    private var startDateAdjust = ""
    private var endDate = ""
    private lateinit var cal: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAtributMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cal = Calendar.getInstance()

        daoSession = (application as MyApplication).daoSession!!

        adapter = DataAttributeAdapter(arrayListOf(), object : DataAttributeAdapter.OnAdapterListener{
            override fun onClick(po: TMaterial) {
                startActivity(Intent(this@DataAtributMaterialActivity, DetailDataAtributeMaterialActivity::class.java)
                    .putExtra("noMaterial", po.nomorMaterial))
            }

        })

        fetchLocal()

        with(binding){
            rvSerial.adapter = adapter
            rvSerial.setHasFixedSize(true)
            rvSerial.layoutManager = LinearLayoutManager(this@DataAtributMaterialActivity, LinearLayoutManager.VERTICAL, false)

            setCategoryDropdown()
            setCategoryTahun()
            setBatcSn()
            setDatePicker()

            btnBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun setDatePicker() {
        val dateSetListenerStart = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.txtTglMulai.text = sdf.format(cal.time)

            var startDateAdjust = sdf.format(cal.time) // Start date
            var c = Calendar.getInstance()
            c.time = sdf.parse(startDateAdjust)
            c.add(Calendar.DATE, -1) // number of days to add

            startDate = startDateAdjust
            Toast.makeText(this@DataAtributMaterialActivity, startDate, Toast.LENGTH_SHORT).show()
            doSearch()

        }

        val dateSetListenerEnd = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.txtTglSelesai.text = sdf.format(cal.time)
            endDate = sdf.format(cal.time)
            doSearch()

        }

        binding.cvTanggalMulai.setOnClickListener {
            DatePickerDialog(this@DataAtributMaterialActivity, dateSetListenerStart,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.cvTanggalSelesai.setOnClickListener {
            DatePickerDialog(this@DataAtributMaterialActivity, dateSetListenerEnd,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setBatcSn() {
        binding.srcBatchSnMaterial.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                snBatch = s.toString()
                doSearch()
            }

        })
    }

    private fun setCategoryTahun() {
        val listTahun = arrayOf("2020", "2021", "2022", "2023")

        val adapterTahun = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, listTahun)
        binding.dropdownTahun.setAdapter(adapterTahun)
        binding.dropdownTahun.setOnItemClickListener { parent, view, position, id ->
            tahun = binding.dropdownTahun.text.toString()
            doSearch()
        }
    }

    private fun setCategoryDropdown() {
        val list = daoSession.tMaterialGroupsDao.queryBuilder().list()
        val listKategori: ArrayList<String> = ArrayList()
        for (i in list){
            listKategori.add(i.namaKategoriMaterial)
        }

        val adapterKategori = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, listKategori.distinct())
        binding.dropdownKategori.setAdapter(adapterKategori)
        binding.dropdownKategori.setOnItemClickListener { parent, view, position, id ->
            kategori = binding.dropdownKategori.text.toString()
            doSearch()
        }
    }

    private fun doSearch() {
        var endDateAdjust = endDate
        if (endDate.isNullOrEmpty()){
            endDateAdjust = LocalDateTime.now().toString(Config.DATE)
        }

        val listMaterial = daoSession.tMaterialDao.queryBuilder()
            .where(TMaterialDao.Properties.NamaKategoriMaterial.like("%"+ kategori + "%"))
            .where(TMaterialDao.Properties.Tahun.like("%"+ tahun + "%"))
            .whereOr(TMaterialDao.Properties.NoProduksi.like("%"+ snBatch + "%"), TMaterialDao.Properties.NomorMaterial.like("%"+ snBatch + "%"))
            .where(TMaterialDao.Properties.TglProduksi.between(startDateAdjust,endDateAdjust))
            .list()
        adapter.setMaterialList(listMaterial)
    }

    private fun fetchLocal() {
        val listMaterial = daoSession.tMaterialDao.queryBuilder().list()
        adapter.setMaterialList(listMaterial)
    }
}
