package dev.iconpln.mims.ui.role.pabrikan.arttribute_material

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.HasilScan
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ActivityDataAtributMaterialPabrikanBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

@AndroidEntryPoint
class DataAtributMaterialPabrikan : AppCompatActivity() {
    private lateinit var binding: ActivityDataAtributMaterialPabrikanBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var rvAdapter: ListMaterialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAtributMaterialPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvAdapter = ListMaterialAdapter()

        binding.apply {
            rvSerial.layoutManager = LinearLayoutManager(this@DataAtributMaterialPabrikan)
            rvSerial.adapter = rvAdapter
        }

        materialViewModel.getAllMaterial()

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
                Intent(this@DataAtributMaterialPabrikan, DashboardPabrikanActivity::class.java)
            startActivity(intent)
        }

        showSelectedStory()
    }

    private fun showSelectedStory() {
        rvAdapter.setOnItemClickCallback(object : ListMaterialAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemMaterial) {
                val toDetailMaterial =
                    Intent(this@DataAtributMaterialPabrikan, HasilScan::class.java)
                toDetailMaterial.putExtra(HasilScan.EXTRA_SN, data.serialNumber)
                startActivity(toDetailMaterial)
            }
        })
    }
}
