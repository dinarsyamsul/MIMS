package dev.iconpln.mims

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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


