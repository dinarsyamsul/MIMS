package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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