package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityUploadDataMaterialBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

class UploadDataMaterial : AppCompatActivity() {

    private lateinit var binding: ActivityUploadDataMaterialBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDataMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Terbaru", "Terlama")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.autoCompleteDataAtribute.setAdapter(adapter)


//        binding.btnupload1.setOnClickListener {
//            val i = Intent(this@UploadDataMaterial, MonitoringPurchaseOrder::class.java)
//            startActivity(i)
//            finish()
//        }

        binding.back.setOnClickListener {
            val intent = Intent(this@UploadDataMaterial, DashboardPabrikanActivity::class.java)
            startActivity(intent)
            finish()
        }

//        binding.bell.setOnClickListener {
//            val dialogBinding = layoutInflater.inflate(R.layout.activity_popup_notifikasi, null)
//
//            val myDialog = Dialog(this)
//            myDialog.setContentView(dialogBinding)
//
//            myDialog.setCancelable(true)
//            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            myDialog.show()
//        }

        binding.btnupload.setOnClickListener {
            Toast.makeText(this, "Under Maintenance", Toast.LENGTH_SHORT).show()
        }
    }
}


