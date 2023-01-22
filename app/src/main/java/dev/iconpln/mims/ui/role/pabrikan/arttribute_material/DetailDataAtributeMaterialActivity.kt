package dev.iconpln.mims.ui.role.pabrikan.arttribute_material

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ActivityDetailDataAtributeMaterialBinding
import dev.iconpln.mims.ui.scan.ResponseScanActivity
import java.util.*

@AndroidEntryPoint
class DetailDataAtributeMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDataAtributeMaterialBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var rvAdapter: ListDetailMaterialAdapter
    private var noProduksi: String? = ""
    private var noMaterial: String? = ""
    private var serialNumber: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataAtributeMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvAdapter = ListDetailMaterialAdapter()

        binding.apply {
            rvDetailMaterial.layoutManager = LinearLayoutManager(this@DetailDataAtributeMaterialActivity)
            rvDetailMaterial.adapter = rvAdapter
        }

        val data = intent.extras
        if (data != null) {
            Log.d("DetailAtribut", "cek kiriman data: $data")
        }

        val noMaterial = data?.getString(EXTRA_SN)
        if (noMaterial != null) {
            materialViewModel.getDetailMaterial("",noMaterial,serialNumber)
        }

        materialViewModel.detailMaterialResponse.observe(this){
            rvAdapter.setData(it.data)
        }

        binding.srcDetaildataatributematerial.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    serialNumber = query.uppercase(Locale.ROOT)
                    materialViewModel.getDetailMaterial("","", serialNumber)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    serialNumber = newText.uppercase(Locale.ROOT)
                    materialViewModel.getDetailMaterial("","", serialNumber)
                }
                return false
            }
        })

        showSelectedMaterial()
    }

    private fun showSelectedMaterial() {
        rvAdapter.setOnItemClickCallback(object : ListDetailMaterialAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemMaterial) {
                val toDetailMaterial =
                    Intent(this@DetailDataAtributeMaterialActivity, ResponseScanActivity::class.java)
                toDetailMaterial.putExtra(ResponseScanActivity.EXTRA_SN, data.serialNumber)
                startActivity(toDetailMaterial)
            }
        })
    }

    companion object {
        const val EXTRA_SN = "no_material"
    }
}