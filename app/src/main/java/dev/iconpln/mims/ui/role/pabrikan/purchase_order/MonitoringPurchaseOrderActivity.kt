package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.ListTanggalAdapter
import dev.iconpln.mims.R
import dev.iconpln.mims.TanggalFilter
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderPabrikanBinding
import java.util.*

@AndroidEntryPoint
class MonitoringPurchaseOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringPurchaseOrderPabrikanBinding
    private val monitoringPOViewModel: MonitoringPOViewModel by viewModels()
    private lateinit var rvAdapter: ListNoPoAdapter
    private val list = ArrayList<TanggalFilter>()
    private var noPO: String? = ""
    private var urut: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPurchaseOrderPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val materialDateBuilderStart: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilderStart.setTitleText("Pilih Tanggal Awal")

        val materialDatePickerStart = materialDateBuilderStart.build()

        val materialDateBuilderEnd: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilderStart.setTitleText("Pilih Tanggal Akhir")

        val materialDatePickerEnd = materialDateBuilderEnd.build()

        binding.calStart.setOnClickListener {
            materialDatePickerStart.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        materialDatePickerStart.addOnPositiveButtonClickListener {
            binding.tvCalStart.text = materialDatePickerStart.headerText
        }

        binding.calEnd.setOnClickListener {
            materialDatePickerEnd.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        materialDatePickerEnd.addOnPositiveButtonClickListener {
            binding.tvCalEnd.text = materialDatePickerEnd.headerText
        }

        val berdasarkan = resources.getStringArray(R.array.berdasarkan)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, berdasarkan)
        binding.urutBerdasarkan.setAdapter(arrayAdapter)

        list.addAll(getListTanggal())

        rvAdapter = ListNoPoAdapter()

        binding.apply {
            rvNoPo.layoutManager = LinearLayoutManager(this@MonitoringPurchaseOrderActivity)
            rvNoPo.adapter = rvAdapter
        }

        monitoringPOViewModel.getMonitoringPO(noPO, urut)

        monitoringPOViewModel.monitoringPOResponse.observe(this) {
            rvAdapter.setData(it.data)
        }

        binding.srcNomorBatch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val mQuery = query.uppercase(Locale.ROOT)
                    noPO = mQuery
                    monitoringPOViewModel.getMonitoringPO(noPO, urut)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    noPO = newText.uppercase(Locale.ROOT)
                    monitoringPOViewModel.getMonitoringPO(noPO, urut)
                }
                return false
            }
        })

        binding.urutBerdasarkan.setOnItemClickListener { _, _, _, _ ->
            urut = binding.urutBerdasarkan.text.toString()
            monitoringPOViewModel.getMonitoringPO(noPO, urut)
        }
    }

    private fun getListTanggal(): ArrayList<TanggalFilter> {
        val tanggalFill = resources.getStringArray(R.array.data_tanggal)
        val listTanggalfil = ArrayList<TanggalFilter>()
        for (i in tanggalFill.indices) {
            val tanggal = TanggalFilter(tanggalFill[i])
            listTanggalfil.add(tanggal)
        }
        return listTanggalfil
    }
}