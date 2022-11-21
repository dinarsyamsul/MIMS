package dev.iconpln.mims

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.databinding.ActivityUploadDataMaterialBinding

class UploadDataMaterial : AppCompatActivity() {

    private lateinit var binding: ActivityUploadDataMaterialBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDataMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnupload1.setOnClickListener {
            val i = Intent(this@UploadDataMaterial, MonitoringPurchaseOrder::class.java)
            startActivity(i)
        }
    }
}


