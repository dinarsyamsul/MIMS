package dev.iconpln.mims

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailMonitoring : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_monitoring)
    }

    companion object{
        const val EXTRA_SN = "extra_sn"
    }
}