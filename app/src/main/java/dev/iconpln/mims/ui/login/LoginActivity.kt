package dev.iconpln.mims.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.ActivityLoginBinding
import dev.iconpln.mims.ui.DashboardActivity
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val apiService = ApiConfig.getApiService()
        val networkStatusTracker = NetworkStatusTracker(this)
        loginViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(apiService, networkStatusTracker)
            )[LoginViewModel::class.java]

//        loginViewModel.loginResponse.observe(this){ result ->
//            when(result.message){
//                "Berhasil Login" -> {
//                    Intent(this@LoginActivity, DashboardActivity::class.java).also {
//                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(it)
//                    }
//                }
//                else -> {
//                    Toast.makeText(this@LoginActivity, "Login Gagal!", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        loginViewModel.loginResponse.observe(this) { result ->
            result.data.forEach {
                if (it.id != null) {
                    Intent(this@LoginActivity, DashboardActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login Gagal!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Pastikan koneksi internet terhubung.", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.btnLogin.setOnClickListener {
                loginUser()
            }
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

            if (password.length < 6) {
                isInvalidFields = true
                edtPass.error = "Password minimal terdiri dari 8 karakter"
            }

            if (password.isEmpty()) {
                isInvalidFields = true
                edtPass.error = "Password tidak boleh kosong"
            }

            if (!isInvalidFields) {
                loginViewModel.getLogin(username, password)
            }
        }
    }
}