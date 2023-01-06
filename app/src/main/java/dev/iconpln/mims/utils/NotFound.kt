package dev.iconpln.mims.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.R
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

class NotFound : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_found)
    }

    override fun onBackPressed() {
        super.getOnBackPressedDispatcher()
        val intent = Intent(this@NotFound, DashboardPabrikanActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}