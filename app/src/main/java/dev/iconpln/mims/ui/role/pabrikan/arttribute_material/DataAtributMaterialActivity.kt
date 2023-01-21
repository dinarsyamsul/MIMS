package dev.iconpln.mims.ui.role.pabrikan.arttribute_material

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.ListTanggalAdapter
import dev.iconpln.mims.R
import dev.iconpln.mims.TanggalFilter
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ActivityDataAtributMaterialBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import dev.iconpln.mims.ui.scan.ResponseScanActivity

@AndroidEntryPoint
class DataAtributMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataAtributMaterialBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var rvAdapter: ListMaterialAdapter
    private val list = ArrayList<TanggalFilter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAtributMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTanggal.setHasFixedSize(true)

        list.addAll(getListTanggal())
        showRecyclerList()

        rvAdapter = ListMaterialAdapter()

        binding.apply {
            rvSerial.layoutManager = LinearLayoutManager(this@DataAtributMaterialActivity)
            rvSerial.adapter = rvAdapter
        }

        materialViewModel.getAllMaterial("","","")

        materialViewModel.materialResponse.observe(this) {
            rvAdapter.setData(it.data)
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
                    Intent(this@DataAtributMaterialActivity, ResponseScanActivity::class.java)
                toDetailMaterial.putExtra(ResponseScanActivity.EXTRA_SN, data.serialNumber)
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

    private fun showRecyclerList(){
        binding.rvTanggal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listTanggalAdapter = ListTanggalAdapter(list)
        binding.rvTanggal.adapter = listTanggalAdapter
    }
}
