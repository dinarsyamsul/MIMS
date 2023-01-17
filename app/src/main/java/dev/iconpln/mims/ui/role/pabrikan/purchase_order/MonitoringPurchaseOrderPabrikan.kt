package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.ListTanggalAdapter
import dev.iconpln.mims.R
import dev.iconpln.mims.TanggalFilter
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderPabrikanBinding
import java.util.*

@AndroidEntryPoint
class MonitoringPurchaseOrderPabrikan : AppCompatActivity() {

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

        val berdasarkan = resources.getStringArray(R.array.berdasarkan)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, berdasarkan)
        binding.autoCompleteTextview.setAdapter(arrayAdapter)

        binding.rvTanggal.setHasFixedSize(true)

        list.addAll(getListTanggal())
        showRecyclerList()

        rvAdapter = ListNoPoAdapter()

        binding.apply {
            rvNoPo.layoutManager = LinearLayoutManager(this@MonitoringPurchaseOrderPabrikan)
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

        binding.autoCompleteTextview.setOnItemClickListener(object :
            AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                urut = binding.autoCompleteTextview.text.toString()
                monitoringPOViewModel.getMonitoringPO(noPO, urut)
            }
        })
    }

    private fun getListTanggal(): kotlin.collections.ArrayList<TanggalFilter> {
        val tanggalFill = resources.getStringArray(R.array.data_tanggal)
        val listTanggalfil = kotlin.collections.ArrayList<TanggalFilter>()
        for (i in tanggalFill.indices) {
            val tanggal = TanggalFilter(tanggalFill[i])
            listTanggalfil.add(tanggal)
        }
        return listTanggalfil
    }

    private fun showRecyclerList() {
        binding.rvTanggal.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listTanggalAdapter = ListTanggalAdapter(list)
        binding.rvTanggal.adapter = listTanggalAdapter
    }
}