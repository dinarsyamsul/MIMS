package dev.iconpln.mims.ui.tracking


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.response.DatasItem
import dev.iconpln.mims.data.remote.response.HistorisItem
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivitySpecMaterialTrackingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

import org.json.JSONObject




class SpecMaterialActivity : AppCompatActivity() {
    private lateinit var rvAdapter: SpecAdapter
    private lateinit var rvHistoryAdapter: HistorySpecAdapter

    private lateinit var binding: ActivitySpecMaterialTrackingBinding
    private val viewModel: TrackingHistoryViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecMaterialTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val sn = extras?.getString(DataMaterialTrackingActivity.EXTRA_SN)
        Log.d("Activity", "cek sn masuk : $sn")
        if (sn != null) {
            viewModel.getTrackingHistory(sn, this)
            binding.txtSerialNumber.text=sn
        }

        rvAdapter = SpecAdapter(arrayListOf(), object: SpecAdapter.OnAdapterListener {
            override fun onClick(data: DatasItem) {

            }
        })

        rvHistoryAdapter= HistorySpecAdapter(arrayListOf(), object: HistorySpecAdapter.OnAdapterListener {
            override fun onClick(data: HistorisItem) {
                showPopUp(data)
            }
        })

        binding.rvSpec.apply {
            adapter = rvAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@SpecMaterialActivity,2)
        }

        binding.rvHistory.apply {
            adapter = rvHistoryAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SpecMaterialActivity, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.trackingResponse.observe(this) {
            binding.apply {
                if (it.datas.isNullOrEmpty()){
                    Toast.makeText(this@SpecMaterialActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    if(it.datas.size>0){
                        Log.i("varI","${it.datas.size}")
                        fetchDataLocal(it.datas)
                    }
                    if(it.historis.size>0){
                        Log.i("varI","${it.historis.size}")
                        fetchDataLocalHistory(it.historis)
                    }
                }
            }
        }
        binding.btnBack.setOnClickListener { onBackPressed() }

    }

    private fun showPopUp(data: HistorisItem) {
        val dialog = Dialog(this@SpecMaterialActivity)
        dialog.setContentView(R.layout.pop_up_tracking_history_detail)
        val btnOk = dialog.findViewById(R.id.btn_ok) as Button
        val content = dialog.findViewById(R.id.txt_content) as TextView

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService(this@SpecMaterialActivity).getDetailTrackingHistory(
                data.serialNumber,data.nomorTransaksi,data.status)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        if (response.body()?.status == "success"){

                            val gson = GsonBuilder().setPrettyPrinting().create()
                            var prettyJsonString = gson.toJson(response.body()?.datas)
                            prettyJsonString = prettyJsonString.replace("}","")
                            prettyJsonString = prettyJsonString.replace("{","")
                            prettyJsonString = prettyJsonString.replace(",","")
                            prettyJsonString = prettyJsonString.replace("\"","")

                            content.text = prettyJsonString
                        }else{
                            content.text = "Data tidak Ditemukan"
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    content.text = "Data tidak Ditemukan"
                    Toast.makeText(this@SpecMaterialActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        val window : Window = dialog.window!!
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setWindowAnimations(R.style.DialogUpDown)
        dialog.show()
    }

    fun formatString(text: String): String? {
        val json = StringBuilder()
        var indentString = ""
        var space = "\n"
        for (i in 0 until text.length) {
            val letter = text[i]
            when (letter) {
                '{', '[' -> {
                    json.append(
                        """
                        
                        $indentString$letter
                        
                        """.trimIndent()
                    )
                    indentString = indentString + "\t"
                    json.append(indentString)
                }
                '}', ']' -> {
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append(
                        """
                        
                        $indentString$letter
                        """.trimIndent()
                    )
                }
                ',' -> json.append(
                    """
                    $letter
                    $indentString
                    """.trimIndent()
                )
                else -> json.append(letter)
            }
        }
        return json.toString()
    }

    private fun fetchDataLocal(datas:List<DatasItem>) {
        rvAdapter.setData(datas)
    }
    private fun fetchDataLocalHistory(datas:List<HistorisItem>) {
        rvHistoryAdapter.setData(datas)
    }

    companion object{
        const val EXTRA_NO_DOMIMS = "extra_no_do_mims"
    }


}