package dev.iconpln.mims.ui.tracking


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.data.remote.response.DatasItem
import dev.iconpln.mims.data.remote.response.HistorisItem
import dev.iconpln.mims.databinding.ActivitySpecMaterialTrackingBinding

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
            layoutManager = GridLayoutManager(this@SpecMaterialActivity,3)
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