package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.databinding.ActivityDataAtributMaterialPabrikanBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

@AndroidEntryPoint
class DataAtributMaterialPabrikan : AppCompatActivity() {
    private lateinit var binding: ActivityDataAtributMaterialPabrikanBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var rvAdapter: ListSerialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAtributMaterialPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvAdapter = ListSerialAdapter()

        binding.apply {
            rvSerial.layoutManager = LinearLayoutManager(this@DataAtributMaterialPabrikan)
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
                Intent(this@DataAtributMaterialPabrikan, DashboardPabrikanActivity::class.java)
            startActivity(intent)
        }
    }

}
