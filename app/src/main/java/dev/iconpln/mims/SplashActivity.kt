package dev.iconpln.mims

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.ui.auth.LoginBiometricActivity
import dev.iconpln.mims.ui.pnerimaan.approval.ApprovalActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.SessionManager

class SplashActivity : AppCompatActivity() {
    private lateinit var sessions: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessions = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            sessions.session_activity.asLiveData().observe(this){ session ->
                when(session){
                    Config.IS_LOGIN -> {
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
                        checkingLogin(session)
                    }else -> {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }, 1500)
    }

    private fun checkingLogin(session: String) {
        sessions.is_login_biometric.asLiveData().observe(this){
            Log.d("checkSession", it.toString())
            when(it){
                1 -> {
                    if(session == Config.IS_LOGIN){
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@SplashActivity, LoginBiometricActivity::class.java))
                        finish()
                    }
                }else -> {
                    if(session == Config.IS_LOGIN){
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}