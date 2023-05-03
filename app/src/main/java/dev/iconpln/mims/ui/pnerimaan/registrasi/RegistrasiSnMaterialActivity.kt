package dev.iconpln.mims.ui.pnerimaan.registrasi

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityRegisterSnMaterialBinding
import dev.iconpln.mims.utils.ViewModelFactory

class RegistrasiSnMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterSnMaterialBinding
    private lateinit var viewModel: RegistrasiMaterialViewModel
    private lateinit var rvAdapter: ListRegisSnMaterialAdapter
    private var status = "PROCESSED"
    private var sn = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSnMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegis.setOnClickListener {
            showInputSnDialogBox()
        }

        val apiService = ApiConfig.getApiService(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(apiService)
        )[RegistrasiMaterialViewModel::class.java]

        viewModel.getMonitoringMaterial(status)
        viewModel.monitAktivMaterial.observe(this) {
            if (it != null) {
                rvAdapter.setListRegisSn(it.data)
            }
        }

        viewModel.insertMaterialRegistrasi.observe(this) {
            if (it != null) {
                if (it.message == "Success") {
                    showSuccessInputDialogBox()
                }
            }
        }

        viewModel.insertMaterialRegistrasiByScan.observe(this) {
            if (it != null) {
                if (it.message == "Success") {
                    showSuccessInputDialogBoxByScan()
                }
            }
        }

        viewModel.isLoading.observe(this){
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) {
            if (it != null) {
//                rvAdapter.setListRegisSn(arrayListOf())
                viewModel.getMonitoringMaterial(status)
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object :
            com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                if (tab?.text == "PROCESSED") {
                    status = tab.text.toString()
                    viewModel.getMonitoringMaterial(status)
                } else if (tab?.text == "APPROVED") {
                    status = tab.text.toString()
                    viewModel.getMonitoringMaterial(status)
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

        })

        binding.srcSerialNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                sn = s.toString()
                viewModel.getMonitoringMaterial(sn = sn, status = status)
            }

        })

        setRecyclerView()

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun showInputSnDialogBox() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.input_dialog_registrasi, null)
        val btnClose = view.findViewById<Button>(R.id.btn_close)
        val btnRegis = view.findViewById<AppCompatButton>(R.id.btn_regis)
        val btnScan = view.findViewById<AppCompatButton>(R.id.btn_scan)
        val inputSn = view.findViewById<TextInputEditText>(R.id.edt_sn)
        builder.setView(view)
        btnClose.setOnClickListener {
            builder.dismiss()
        }
        btnRegis.setOnClickListener {
            viewModel.setInsertMaterialRegistrasi(inputSn.text.toString().trim(), "text")
            sn = inputSn.text.toString()
        }
        btnScan.setOnClickListener {
            openScanner()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun showSuccessInputDialogBoxByScan() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.alert_dialog_berhasil_regis_by_scan, null)
        val tvSN = view.findViewById<TextView>(R.id.textView22)
        val tvMessage = view.findViewById<TextView>(R.id.textView23)
        val btnSelesai = view.findViewById<Button>(R.id.btn_berhasil_regis)
        val btnScanLagi = view.findViewById<Button>(R.id.btn_scan_again)
        builder.setView(view)
        tvSN.text = "SN material $sn"
        tvMessage.text = "Telah sesuai SPLN dan berhasil di registrasi."
        btnSelesai.setOnClickListener {
            builder.dismiss()
            viewModel.getMonitoringMaterial(status)
        }
        btnScanLagi.setOnClickListener {
            openScanner()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun showSuccessInputDialogBox() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.alert_dialog_berhasil_regis, null)
        val tvSN = view.findViewById<TextView>(R.id.textView22)
        val tvMessage = view.findViewById<TextView>(R.id.textView23)
        val btnSelesai = view.findViewById<Button>(R.id.btn_berhasil_regis)
        builder.setView(view)
        tvSN.text = "SN material $sn"
        tvMessage.text = "Telah sesuai SPLN dan berhasil di registrasi."
        btnSelesai.setOnClickListener {
            builder.dismiss()
            viewModel.getMonitoringMaterial(status)
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun openScanner() {
        val scan = ScanOptions()
        scan.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        scan.setCameraId(0)
        scan.setBeepEnabled(true)
        scan.setBarcodeImageEnabled(true)
        scan.captureActivity = CustomScanActivity::class.java
        barcodeLauncher.launch(scan)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        try {
            if(!result.contents.isNullOrEmpty()){
                sn=result.contents
                Log.i("hit Api","${sn}")
                viewModel.setInsertMaterialRegistrasi(sn, "scan")
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    private fun setRecyclerView() {
        rvAdapter = ListRegisSnMaterialAdapter()
        binding.rvRegisSn.apply {
            layoutManager = LinearLayoutManager(this@RegistrasiSnMaterialActivity)
            setHasFixedSize(true)
            adapter = rvAdapter
        }
    }
}