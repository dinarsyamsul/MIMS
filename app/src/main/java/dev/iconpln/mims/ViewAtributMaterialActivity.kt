package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.databinding.ActivityTrackingBinding
import dev.iconpln.mims.databinding.ActivityViewAtributMaterialBinding
import dev.iconpln.mims.ui.role.pabrikan.tracking.DetailTrackingHistoryActivity
import dev.iconpln.mims.ui.role.pabrikan.tracking.TrackingHistoryViewModel

class ViewAtributMaterialActivity : AppCompatActivity() {
    private val trackingViewModel: TrackingHistoryViewModel by viewModels()
    private lateinit var binding: ActivityViewAtributMaterialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAtributMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        val sn = extras?.getString(EXTRA_SN)

        if (sn != null) {
            trackingViewModel.getTrackingHistory(sn, this)
        }

        trackingViewModel.trackingResponse.observe(this){
//            Toast.makeText(this, "cek jumlah data : ${it.datas.size}", Toast.LENGTH_SHORT).show().also {
//                startActivity(Intent(this@TrackingHistoryActivity, DetailTrackingHistoryActivity::class.java))
//            }
            binding.apply {
                it.datas.forEach { data ->
//                    tvBatch.text = data.
//                    tvNoMaterial.text = data
                }
            }
        }
    }

    companion object {
        const val EXTRA_SN = "extra_sn"
    }
}