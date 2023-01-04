package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import dev.iconpln.mims.ui.login.LoginActivity
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import dev.iconpln.mims.ui.scan.CustomScanActivity
import dev.iconpln.mims.ui.scan.ScannerActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, DashboardPabrikanActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}