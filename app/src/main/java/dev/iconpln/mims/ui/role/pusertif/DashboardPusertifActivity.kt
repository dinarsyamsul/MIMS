package dev.iconpln.mims.ui.role.pusertif

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.databinding.ActivityDashboardPusertifBinding

class DashboardPusertifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardPusertifBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardPusertifBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val session = TokenManager(this)
//
//        binding.btnLogout.setOnClickListener {
//            val onLogout = Intent(this@DashboardPusertifActivity, LoginActivity::class.java)
//            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//
//            lifecycleScope.launch {
//                session.clearUserToken()
//            }
//            session.user_token.asLiveData().observe(this) {
//                Log.d("MainActivity", "cek token : $it")
//            }
//            onLogout.flags = Intent.FLAG_AC   TIVITY_NEW_TASK
//            startActivity(onLogout)
//            finish()
    }
}
//}