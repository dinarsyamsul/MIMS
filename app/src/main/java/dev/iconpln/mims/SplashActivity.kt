package dev.iconpln.mims

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.ui.auth.LoginBiometricActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.SessionManager

class SplashActivity : AppCompatActivity() {
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        session = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            session.session_activity.asLiveData().observe(this){ session ->
                when(session){
                    Config.IS_LOGIN -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }
            }

//            session.is_login_biometric.asLiveData().observe(this){
//                when(it){
//                    1 -> {
//                        val intent = Intent(this@SplashActivity, LoginBiometricActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                    else -> {
//                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//            }
        }, 1500)
    }
}