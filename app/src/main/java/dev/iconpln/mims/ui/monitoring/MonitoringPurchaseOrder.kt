package dev.iconpln.mims.ui.monitoring


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

@AndroidEntryPoint
class MonitoringPurchaseOrder : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringPurchaseOrderBinding
    private var muncul = false

    private val monitoringPOViewModel: MonitoringPOViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPurchaseOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Terbaru", "Terlama")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.autoCompleteMonitoring.setAdapter(adapter)

//        binding.img1.setOnClickListener {
//            if (muncul == true) {
//                binding.img1.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        this,
//                        R.drawable.ic_baseline_keyboard_arrow_up_24
//                    )
//                )
//                binding.const2.visibility = View.VISIBLE
//                muncul = !muncul
//            } else {
//                binding.img1.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        this,
//                        R.drawable.ic_baseline_keyboard_arrow_down_24
//                    )
//                )
//                binding.const2.visibility = View.GONE
//                muncul = !muncul
//            }
//        }
//        binding.btn1.setOnClickListener {
//            val dialogBinding = layoutInflater.inflate(R.layout.activity_popdialog, null)
//
//            val myDialog = Dialog(this)
//            myDialog.setContentView(dialogBinding)
//
//            myDialog.setCancelable(true)
//            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            myDialog.show()
//        }

        monitoringPOViewModel.getMonitoringPO("PO1", "TERBARU", 1, 4)
        monitoringPOViewModel.monitoringPOResponse.observe(this) {
            it.data.forEach { data ->
                binding.noPo.text = data.noPurchaseOrder
                binding.vendor.text = data.vendor
                binding.plant.text = data.plant
                binding.storeLoc.text = data.storLoc
                binding.unit.text = data.unit
                binding.leadTime.text = data.leadTime
                binding.qty.text = data.qtyPo
                binding.deskripsi.text = data.deskripsi
            }
            Log.d("MonitoringPurchaseOrder", "Cek Monitoting data ${it.data}")
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@MonitoringPurchaseOrder, DashboardPabrikanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}