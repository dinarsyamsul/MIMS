package dev.iconpln.mims.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityOtpBinding
import dev.iconpln.mims.ui.login.LoginViewModel
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.TokenManager
import dev.iconpln.mims.utils.ViewModelFactory

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = TokenManager(this)
        val apiService = ApiConfig.getApiService()
        val networkStatusTracker = NetworkStatusTracker(this)
        loginViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(session, apiService, networkStatusTracker)
            )[LoginViewModel::class.java]

        val username = intent.extras?.getString(EXTRA_USERNAME)
        binding.apply {

            btnSubmitotp.setOnClickListener {
                val otpInput = edtotp1.text.toString() +
                        edtotp2.text.toString() +
                        edtotp3.text.toString() +
                        edtotp4.text.toString() +
                        edtotp5.text.toString() +
                        edtotp6.text.toString()
                Log.d("OtpActivity", "cek inputan otp: $otpInput")
                if (username != null) {
                    loginViewModel.sendTokenOtp(otpInput, username)
                }
            }
        }

        loginViewModel.verifyTokenResponse.observe(this) {
            it.data.forEach {
                when (it.msg) {
                    "TOKEN SALAH" -> {
                        Toast.makeText(this, "Token salah", Toast.LENGTH_SHORT).show()
                    }
                    "LOGIN BERHASIL" -> {
                        startActivity(Intent(this@OtpActivity, DashboardActivity::class.java))
                    }
                }
            }
        }

        binding.txtotp4.setOnClickListener {
            loginViewModel.hitEmail(username.toString())
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}