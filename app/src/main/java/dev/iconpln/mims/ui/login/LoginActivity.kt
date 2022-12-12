package dev.iconpln.mims.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityLoginBinding
import dev.iconpln.mims.ui.DashboardActivity
import dev.iconpln.mims.ui.OtpActivity
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import dev.iconpln.mims.ui.role.pusertif.DashboardPusertifActivity
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.TokenManager
import dev.iconpln.mims.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var session: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = TokenManager(this)
        val apiService = ApiConfig.getApiService()
        val networkStatusTracker = NetworkStatusTracker(this)

        session.user_token.asLiveData().observe(this) { token ->
            Log.d("LoginActivity", "cek user token: $token")
            if (token != null) {
                session.role_id.asLiveData().observe(this) { roleId ->
                    when (roleId) {
                        "1" -> {
                            Intent(this@LoginActivity, DashboardPabrikanActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        }
                        "2" -> {
                            Intent(this@LoginActivity, DashboardPusertifActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        }
                        "9" -> {
                            Intent(this@LoginActivity, DashboardActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        }
                    }
                }
            }
        }
        session.device_token.asLiveData().observe(this) { token ->
            Log.d("LoginActivity", "cek device token: $token")
        }

        loginViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(session, apiService, networkStatusTracker)
            )[LoginViewModel::class.java]

        loginViewModel.loginResponse.observe(this) { result ->
            when (result.message) {
                "VERIFIKASI DEVICE" -> {
                    result.data.forEach {
                        loginViewModel.hitEmail(it.userName)
                        startActivity(Intent(this@LoginActivity, OtpActivity::class.java).apply {
                            putExtra(OtpActivity.EXTRA_USERNAME, it.userName)
                        })
                        Toast.makeText(
                            this,
                            "Kode OTP segera dikirimkan ke email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                "LOGIN BERHASIL" -> {
                    result.data.forEach { login ->
                        if (login.roleId == "1") {
                            Intent(this@LoginActivity, DashboardPabrikanActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        } else if (login.roleId == "2") {
                            Intent(this@LoginActivity, DashboardPusertifActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        } else if (login.roleId == "9") {
                            Intent(this@LoginActivity, DashboardActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        }
                    }
                }
                "LOGIN GAGAL" -> {
                    Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        loginViewModel.agoLoginResponse.observe(this) {
            it.data.forEach { result ->
                if (result.id != null) {
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    Toast.makeText(
                        this,
                        "Login berhasil",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Login gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginViewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        loginViewModel.state.observe(this) { state ->
            if (state == MyState.Error) {
                Toast.makeText(
                    this,
                    "Pastikan koneksi internet terhubung.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
//                    startActivity(Intent(this, OtpActivity::class.java))
        }
    }

    private fun loginUser() {
        with(binding) {
            val username = edtUsername.text.toString().trim()
            val password = edtPass.text.toString().trim()

            var isInvalidFields = false

            if (username.isEmpty()) {
                isInvalidFields = true
                edtUsername.error = "Email tidak boleh kosong"
            }

            if (password.length < 5) {
                isInvalidFields = true
                edtPass.error = "Password minimal terdiri dari 5 karakter"
            }

            if (password.isEmpty()) {
                isInvalidFields = true
                edtPass.error = "Password tidak boleh kosong"
            }

            if (!isInvalidFields) {
                session.device_token.asLiveData().observe(this@LoginActivity) {
                    loginViewModel.getLogin(username, password, it.toString())
                }

//                loginViewModel.getAgoLogin(username, password) //skema login ago
            }
        }
    }
}