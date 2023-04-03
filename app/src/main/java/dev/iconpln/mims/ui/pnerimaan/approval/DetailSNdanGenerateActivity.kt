package dev.iconpln.mims.ui.pnerimaan.approval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.response.DataItemMaterialRegistrasiByDate
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailSndanGenerateBinding
import dev.iconpln.mims.utils.ViewModelFactory

class DetailSNdanGenerateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSndanGenerateBinding
    private lateinit var viewModel: ApprovalMaterialViewModel
    private lateinit var rvAdapter: DetailSNdanGenerateAdapter
    private var filter = "PROCESSED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSndanGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(apiService))[ApprovalMaterialViewModel::class.java]

        val tglRegis = intent.getStringExtra(EXTRA_TGL_REGISTRASI)

        if (tglRegis != null) {
            viewModel.getMaterialRegistrasiDetailByDate(tglRegis, filter)
        }

        viewModel.detailMaterialRegistrasiResponse.observe(this){
            rvAdapter.setListMaterialByDate(it.data)
        }

        binding.btnPilih.setOnClickListener {
            rvAdapter.isSelectionMode = !rvAdapter.isSelectionMode
            binding.btnKomplain.visibility = if (rvAdapter.isSelectionMode) View.VISIBLE else View.GONE
            binding.btnTerima.visibility = if (rvAdapter.isSelectionMode) View.VISIBLE else View.GONE
            rvAdapter.notifyDataSetChanged()
        }

        binding.btnTerima.setOnClickListener {
            val cek = rvAdapter.getCheckedItems()
            val dataApprove = ArrayList<String>()
            cek.forEach {
                dataApprove.add(it.serialNumber)
            }
            viewModel.setAktivasiMaterial(dataApprove)
            rvAdapter.notifyDataSetChanged()
            Log.d("DetailSNActivity", "aaah : $dataApprove")
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        rvAdapter = DetailSNdanGenerateAdapter()
        binding.rvDetailSnGenerate.apply {
            layoutManager = LinearLayoutManager(this@DetailSNdanGenerateActivity)
            setHasFixedSize(true)
            adapter = rvAdapter
        }
    }

    companion object {
        const val EXTRA_TGL_REGISTRASI = "extra_tgl_registrasi"
    }
}