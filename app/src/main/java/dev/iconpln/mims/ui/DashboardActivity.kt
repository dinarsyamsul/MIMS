package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.databinding.ActivityDashboardBinding
import dev.iconpln.mims.databinding.ActivityScanBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanner.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
    }
}