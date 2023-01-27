package dev.iconpln.mims.ui.role.pabrikan.tracking

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.databinding.ActivityDataMaterialTrackingBinding

class DataMaterialTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataMaterialTrackingBinding
    private val trackingViewModel: TrackingHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataMaterialTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        val sn = extras?.getString(EXTRA_SN)
        Log.d("Activity", "cek sn masuk : $sn")
        if (sn != null) {
            trackingViewModel.getTrackingHistory(sn, this)
        }

        trackingViewModel.trackingResponse.observe(this) {
            binding.apply {
                it.datas.forEach { data ->
                    when (data.propertyName) {
                        "TIPE METER" -> {
                            tvIsiTipeMeter.text = it.datas[0].propertyValue
                        }
                        "SPLN" -> {
                            tvIsiSpln.text = it.datas[1].propertyValue
                        }
                        "CARA PENGAWATAN" -> {
                            tvIsiCaraPengawatan.text = it.datas[2].propertyValue
                        }
                        "JUMLAH SENSOR (S) DAN RELE *" -> {
                            tvIsiJumlahSensor.text = it.datas[3].propertyValue
                        }
                        "TEGANGAN PENGENAL" -> {
                            tvIsiTeganganPengenal.text = it.datas[4].propertyValue
                        }
                        "ARUS DASAR DAN ARUS MAKSIMUM" -> {
                            tvIsiArusDasar.text = it.datas[5].propertyValue
                        }
                        "FREKUENSI PENGENAL" -> {
                            tvIsiFrekueniPengenal.text = it.datas[6].propertyValue
                        }
                        "KONSTANTA METER DALAM SATUAN IMP/KWH" -> {
                            tvIsiNoIDMeter.text = it.datas[9].propertyValue
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val EXTRA_SN = "extra_sn"
    }
}
