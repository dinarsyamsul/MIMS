package dev.iconpln.mims.ui.role.pusertif

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityDashboardPusertifBinding
import dev.iconpln.mims.ui.login.LoginActivity
import dev.iconpln.mims.utils.TokenManager
import kotlinx.coroutines.launch

class DashboardPusertifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardPusertifBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardPusertifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = TokenManager(this)

        binding.btnLogout.setOnClickListener {
            val onLogout = Intent(this@DashboardPusertifActivity, LoginActivity::class.java)
            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

            lifecycleScope.launch {
                session.clearUserToken()
            }
            session.user_token.asLiveData().observe(this) {
                Log.d("MainActivity", "cek token : $it")
            }
            onLogout.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(onLogout)
            finish()
        }
    }
}