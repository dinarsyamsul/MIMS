package dev.iconpln.mims.ui.pnerimaan.approval

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailSndanGenerateBinding
import dev.iconpln.mims.utils.ViewModelFactory

class DetailSNdanGenerateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSndanGenerateBinding
    private lateinit var viewModel: ApprovalMaterialViewModel
    private lateinit var rvAdapter: DetailSNdanGenerateAdapter
    private var tglRegis: String = ""
    private var filter = "PROCESSED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSndanGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(apiService)
        )[ApprovalMaterialViewModel::class.java]

        tglRegis = intent.getStringExtra(EXTRA_TGL_REGISTRASI).toString()

        viewModel.getMaterialRegistrasiDetailByDate(tglRegis, filter)

        binding.tabLayout.addOnTabSelectedListener(object :
            com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                if (tab?.text == "PROCESSED") {
                    filter = tab.text.toString()
                    viewModel.getMaterialRegistrasiDetailByDate(tglRegis, filter)
                    binding.btnPilih.visibility = View.VISIBLE
                } else if (tab?.text == "APPROVED") {
                    filter = tab.text.toString()
                    rvAdapter.isSelectionMode = false
                    viewModel.getMaterialRegistrasiDetailByDate(tglRegis, filter)
                    binding.btnPilih.visibility = View.GONE
                    binding.btnTerima.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

        })


        viewModel.detailMaterialRegistrasiResponse.observe(this) {
            if (it != null) {
                rvAdapter.setListMaterialByDate(it.data)
            }
        }

        viewModel.aktivMaterialResponse.observe(this) {
            if (it != null) {
                if (it.message == "Success") {
                    popupApproveOk()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) {
            if (it != null) {
                rvAdapter.setListMaterialByDate(arrayListOf())
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnPilih.setOnClickListener {
            rvAdapter.isSelectionMode = !rvAdapter.isSelectionMode
            binding.btnTerima.visibility =
                if (rvAdapter.isSelectionMode) View.VISIBLE else View.GONE
            rvAdapter.notifyDataSetChanged()
        }

        binding.btnTerima.setOnClickListener {
            confirmApprovalDialog()
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

    private fun confirmApprovalDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.popup_validate_approve_sn, null)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val btnSave = view.findViewById<Button>(R.id.btn_approve)
        builder.setView(view)
        btnCancel.setOnClickListener {
            builder.dismiss()
        }
        btnSave.setOnClickListener {
            val checkedItems = rvAdapter.getCheckedItems()
            val dataApprove = ArrayList<String>()
            checkedItems.forEach {
                dataApprove.add(it.serialNumber)
            }
            viewModel.setAktivasiMaterial(dataApprove)
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun popupApproveOk() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.popup_approve_ok, null)
        val btnOK = view.findViewById<Button>(R.id.btn_approve_ok)
        builder.setView(view)
        btnOK.setOnClickListener {
            viewModel.getMaterialRegistrasiDetailByDate(tglRegis, filter)
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    companion object {
        const val EXTRA_TGL_REGISTRASI = "extra_tgl_registrasi"
    }
}