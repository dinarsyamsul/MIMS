package dev.iconpln.mims.ui.role.pabrikan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.BottomsheetTrackingActivity
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityTrackingBinding

class TrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        val bottomSheetDialogFragment = BottomsheetTrackingActivity()

        binding.btnScan.setOnClickListener {
            bottomSheetDialogFragment.show(supportFragmentManager, "Silahkan Scan Barcode dan QR Code")
        }
    }
}