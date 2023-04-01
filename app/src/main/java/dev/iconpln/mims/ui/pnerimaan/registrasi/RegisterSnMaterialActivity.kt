package dev.iconpln.mims.ui.pnerimaan.registrasi

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityRegisterSnMaterialBinding
import dev.iconpln.mims.utils.SharedPrefsUtils
import dev.iconpln.mims.utils.ViewModelFactory

class RegisterSnMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterSnMaterialBinding
    private lateinit var viewModel: RegistrasiMaterialViewModel
    private lateinit var rvAdapter: ListRegisSnMaterialAdapter
    private var filter = "PROCESSED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSnMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnKlik: AppCompatButton = findViewById(R.id.btn_regis)

        btnKlik.setOnClickListener {
            showInputSnDialogBox()
        }

        val token = SharedPrefsUtils.getStringPreference(this,"jwt","")
        Log.d("RegisterSnActivity", "cek token: $token")

        val apiService = ApiConfig.getApiService(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(apiService))[RegistrasiMaterialViewModel::class.java]

        viewModel.getMonitoringMaterial(filter)
        viewModel.monitAktivMaterial.observe(this){
            rvAdapter.setListRegisSn(it.data)
        }

        viewModel.insertMaterialRegistrasi.observe(this){
            if (it.message == "Success"){
                showSuccessInputDialogBox()
            }
        }
        
        viewModel.errorMessage.observe(this){
            if (it != null){
                Toast.makeText(this, "Terdapat error : $it", Toast.LENGTH_SHORT).show()
                Log.d("RegisterSnMaterial", "cehe e $it")
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                if (tab?.text == "PROCESSED"){
                    filter = tab.text.toString()
                    viewModel.getMonitoringMaterial(filter)
                }else if (tab?.text == "APPROVED"){
                    filter = tab.text.toString()
                    viewModel.getMonitoringMaterial(filter)
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

        })

        setRecyclerView()
    }

    private fun showInputSnDialogBox() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.activity_alert_dialog_registrasi, null)
        val btnClose = view.findViewById<Button>(R.id.btn_close)
        val btnRegis = view.findViewById<AppCompatButton>(R.id.btn_regis1)
        val inputSn = view.findViewById<TextInputEditText>(R.id.edt_sn)
        builder.setView(view)
        btnClose.setOnClickListener {
            builder.dismiss()
        }
        btnRegis.setOnClickListener {
            viewModel.setInsertMaterialRegistrasi(inputSn.text.toString().trim())
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun showSuccessInputDialogBox() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.alert_dialog_berhasil_regis, null)
        val btnOk = view.findViewById<Button>(R.id.btn_berhasil_regis)
        builder.setView(view)
        btnOk.setOnClickListener {
            builder.dismiss()
            viewModel.getMonitoringMaterial(filter)
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun setRecyclerView() {
        rvAdapter = ListRegisSnMaterialAdapter()
        binding.rvRegisSn.apply {
            layoutManager = LinearLayoutManager(this@RegisterSnMaterialActivity)
            setHasFixedSize(true)
            adapter = rvAdapter
        }
    }
}