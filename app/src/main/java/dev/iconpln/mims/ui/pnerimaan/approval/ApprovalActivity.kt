package dev.iconpln.mims.ui.pnerimaan.approval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityApprovalBinding
import dev.iconpln.mims.ui.pnerimaan.registrasi.ListRegisSnMaterialAdapter
import dev.iconpln.mims.ui.pnerimaan.registrasi.RegistrasiMaterialViewModel
import dev.iconpln.mims.utils.ViewModelFactory

class ApprovalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApprovalBinding
    private lateinit var viewModel: ApprovalMaterialViewModel
    private lateinit var rvAdapter: ListApprovalMaterialAdapter
    private var filter = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(apiService))[ApprovalMaterialViewModel::class.java]

        viewModel.getMaterialAktivasi(filter)
        viewModel.getMaterialAktivasiResponse.observe(this){
            if (it != null) {
                rvAdapter.setListMaterialAktivasi(it.data)
            }
        }

        viewModel.isLoading.observe(this){
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this){
            if (it != null){
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }
        }

        val statusItem = listOf("ALL", "BELUM SELESAI", "SELESAI")
        val adapterDropdown = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusItem)
        binding.dropdownStatus.setAdapter(adapterDropdown)
        binding.dropdownStatus.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            filter = selectedItem
            viewModel.getMaterialAktivasi(filter)
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        rvAdapter = ListApprovalMaterialAdapter()
        binding.rvApprovalMaterial.apply {
            layoutManager = LinearLayoutManager(this@ApprovalActivity)
            setHasFixedSize(true)
            adapter = rvAdapter
        }
    }
}