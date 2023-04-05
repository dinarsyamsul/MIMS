package dev.iconpln.mims.ui.pnerimaan.approval

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityDetailSndanGenerateBinding
import dev.iconpln.mims.utils.ViewModelFactory

class DetailApprovalMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSndanGenerateBinding
    private lateinit var viewModel: ApprovalMaterialViewModel
    private lateinit var rvAdapter: ListDetailApprovalMaterialAdapter
    private var tglRegis = ""
    private var sn = ""
    private var noMaterial = ""
    private var status = "PROCESSED"

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

        viewModel.getMaterialRegistrasiDetailByDate(tglRegis, status)
        viewModel.getNomorMaterialForAktivasi()

        viewModel.nomorMaterialResponse.observe(this){
            if (it != null){
                val materialData = it.data
                val noMaterialItem = materialData.map { "No. Material : ${it.nomorMaterial}\nDeskripsi : ${it.materialDesc}" }.toTypedArray()
                val adapterDropdown = CustomDropdownAdapter(this, R.layout.dropdown_material_layout, noMaterialItem)
                binding.dropdownNoMaterial.setAdapter(adapterDropdown)
                binding.dropdownNoMaterial.setOnItemClickListener { parent, _, position, _ ->
                    val selectedItem = parent.getItemAtPosition(position) as String
                    noMaterial = selectedItem.split("\n")[0]
                    viewModel.getMaterialRegistrasiDetailByDate(tgl_registrasi = tglRegis, status = status, sn = sn, no_material = noMaterial)
                }
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object :
            com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                if (tab?.text == "PROCESSED") {
                    status = tab.text.toString()
                    viewModel.getMaterialRegistrasiDetailByDate(tglRegis, status)
                    viewModel.getNomorMaterialForAktivasi()
                    binding.btnPilih.visibility = View.VISIBLE
                } else if (tab?.text == "APPROVED") {
                    status = tab.text.toString()
                    rvAdapter.isSelectionMode = false
                    viewModel.getMaterialRegistrasiDetailByDate(tglRegis, status)
                    viewModel.getNomorMaterialForAktivasi()
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

        binding.srcSnMaterial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                sn = s.toString()
                viewModel.getMaterialRegistrasiDetailByDate(tgl_registrasi = tglRegis, status = status, sn = sn, no_material = noMaterial)
            }

        })

        binding.btnTerima.setOnClickListener {
            confirmApprovalDialog()
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        rvAdapter = ListDetailApprovalMaterialAdapter()
        binding.rvDetailSnGenerate.apply {
            layoutManager = LinearLayoutManager(this@DetailApprovalMaterialActivity)
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
            viewModel.getMaterialRegistrasiDetailByDate(tgl_registrasi = tglRegis, status = status, sn = sn, no_material = noMaterial)
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    companion object {
        const val EXTRA_TGL_REGISTRASI = "extra_tgl_registrasi"
    }
}

class CustomDropdownAdapter(
    context: Context,
    resource: Int,
    objects: Array<String>
) : ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.dropdown_material_layout, parent, false)
        }
        val itemName = view?.findViewById<TextView>(R.id.item_name)
        val itemDescription = view?.findViewById<TextView>(R.id.item_description)
        val item = getItem(position)
        if (itemName != null && itemDescription != null && item != null) {
            val parts = item.split("\n")
            if (parts.size > 1) {
                itemName.text = parts[0]
                itemDescription.text = parts[1]
            } else {
                itemName.text = item
            }
        }
        return view ?: super.getView(position, convertView, parent)
    }
}