package dev.iconpln.mims


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

class MonitoringPurchaseOrder : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringPurchaseOrderBinding
    private var muncul = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPurchaseOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.img1.setOnClickListener {
            if (muncul == true) {
                binding.img1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_keyboard_arrow_up_24
                    )
                )
                binding.const2.visibility = View.VISIBLE
                muncul = !muncul
            } else {
                binding.img1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_keyboard_arrow_down_24
                    )
                )
                binding.const2.visibility = View.GONE
                muncul = !muncul
            }
        }
        binding.btn1.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.activity_popdialog, null)

            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@MonitoringPurchaseOrder, DashboardPabrikanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}