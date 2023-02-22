package dev.iconpln.mims.ui.pnerimaan

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.R
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityInputSnmaterialPenerimaanPemeriksaanUlpactivityBinding

class InputSNMaterialPenerimaanPemeriksaanULPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputSnmaterialPenerimaanPemeriksaanUlpactivityBinding
    private val list = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputSnmaterialPenerimaanPemeriksaanUlpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanSnMaterial.setOnClickListener {
            openScanner()
        }

        binding.btnInputSnManual.setOnClickListener {
            customDialog()
        }
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
                val sn = result.contents
                list.addAll(arrayListOf("1", "2", "3"))
                showRecyclerView()
                Toast.makeText(this@InputSNMaterialPenerimaanPemeriksaanULPActivity, "sn : $sn", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }

    private fun showRecyclerView() {
        binding.rvNoPo.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = PenerimaanULPAdapter(list)
        binding.rvNoPo.adapter = listDataAdapter
        listDataAdapter.setOnItemClickCallback(object : PenerimaanULPAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                startActivity(
                    Intent(
                        this@InputSNMaterialPenerimaanPemeriksaanULPActivity,
                        PetugasPenerimaanULPActivity::class.java
                    )
                )
            }
        })
    }

    private fun customDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.activity_popdialog)

        dialog.show()
    }
}