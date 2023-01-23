package dev.iconpln.mims.ui.role.pabrikan.arttribute_material

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.R
import dev.iconpln.mims.TanggalFilter
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ActivityDataAtributMaterialBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DataAtributMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataAtributMaterialBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var rvAdapter: ListMaterialAdapter
    private val list = ArrayList<TanggalFilter>()
    private var kategori: String? = ""
    private var tahun: String? = ""
    private var snOrNoProd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAtributMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val materialDateBuilderStart: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilderStart.setTitleText("Pilih Tanggal Awal")

        val materialDatePickerStart = materialDateBuilderStart.build()

        val materialDateBuilderEnd: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilderStart.setTitleText("Pilih Tanggal Akhir")

        val materialDatePickerEnd = materialDateBuilderEnd.build()

        binding.lyCalStart.setOnClickListener {
            materialDatePickerStart.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        materialDatePickerStart.addOnPositiveButtonClickListener {
            binding.tvCalStart.text = materialDatePickerStart.headerText
        }

        binding.lyCalEnd.setOnClickListener {
            materialDatePickerEnd.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        materialDatePickerEnd.addOnPositiveButtonClickListener {
            binding.tvCalEnd.text = materialDatePickerEnd.headerText
        }

        val kategoriMenu = resources.getStringArray(R.array.kategori_material)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, kategoriMenu)
        binding.dropdownKategori.setAdapter(arrayAdapter)

        val tahunMenu = resources.getStringArray(R.array.pilih_tahun)
        val arrayAdapterTahun = ArrayAdapter(this, R.layout.dropdown_item, tahunMenu)
        binding.dropdownTahun.setAdapter(arrayAdapterTahun)

        list.addAll(getListTanggal())

        rvAdapter = ListMaterialAdapter()

        binding.apply {
            rvSerial.layoutManager = LinearLayoutManager(this@DataAtributMaterialActivity)
            rvSerial.adapter = rvAdapter
        }

        materialViewModel.getAllMaterial(kategori,tahun,snOrNoProd)

        materialViewModel.materialResponse.observe(this) {
            rvAdapter.setData(it.data)
        }

        binding.srcNomorBatch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    val mQuery = query.uppercase(Locale.ROOT)
                    snOrNoProd = mQuery
                    materialViewModel.getAllMaterial(kategori, tahun, snOrNoProd)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    snOrNoProd = newText.uppercase(Locale.ROOT)
                    materialViewModel.getAllMaterial(kategori, tahun, snOrNoProd)
                }
                return false
            }
        })

        binding.dropdownKategori.setOnItemClickListener { _, _, _, _ ->
            val isiData = binding.dropdownKategori.text.toString()
            when(isiData){
                "kWh Meter" -> kategori = "0219"
                "Trafo Distribusi" -> kategori = "0103"
                "Mini Circuit Breaker" -> kategori = "0325"
                "Current Transformer" -> kategori = "0205"
            }
            materialViewModel.getAllMaterial(kategori, tahun, snOrNoProd)
        }

        binding.dropdownTahun.setOnItemClickListener { _, _, _, _ ->
            tahun = binding.dropdownTahun.text.toString()
            materialViewModel.getAllMaterial(kategori, tahun, snOrNoProd)
        }

        materialViewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.btnBack.setOnClickListener {
            val intent =
                Intent(this@DataAtributMaterialActivity, DashboardPabrikanActivity::class.java)
            startActivity(intent)
        }

        showSelectedMaterial()
    }

    private fun showSelectedMaterial() {
        rvAdapter.setOnItemClickCallback(object : ListMaterialAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemMaterial) {
                val toDetailMaterial =
                    Intent(this@DataAtributMaterialActivity, DetailDataAtributeMaterialActivity::class.java)
                toDetailMaterial.putExtra(DetailDataAtributeMaterialActivity.EXTRA_SN, data.nomorMaterial)
                startActivity(toDetailMaterial)
            }
        })
    }

    private fun getListTanggal(): ArrayList<TanggalFilter>{
        val tanggalFill = resources.getStringArray(R.array.data_tanggal)
        val listTanggalfil = kotlin.collections.ArrayList<TanggalFilter>()
        for (i in tanggalFill.indices){
            val tanggal = TanggalFilter(tanggalFill[i])
            listTanggalfil.add(tanggal)
        }
        return listTanggalfil
    }
}
