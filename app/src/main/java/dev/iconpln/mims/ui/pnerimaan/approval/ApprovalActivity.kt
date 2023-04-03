package dev.iconpln.mims.ui.pnerimaan.approval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private var filter = "BELUM SELESAI"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(apiService))[ApprovalMaterialViewModel::class.java]

        viewModel.getMaterialAktivasi(filter)
        viewModel.getMaterialAktivasiResponse.observe(this){
            rvAdapter.setListMaterialAktivasi(it.data)
        }

        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                if (tab?.text == "PROCESSED"){
                    filter = "BELUM SELESAI"
                    viewModel.getMaterialAktivasi(filter)
                }else if (tab?.text == "APPROVED"){
                    filter = "SELESAI"
                    viewModel.getMaterialAktivasi(filter)
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

        })

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