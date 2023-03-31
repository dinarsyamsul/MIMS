package dev.iconpln.mims.ui.auth.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityForgotPasswordBinding
import dev.iconpln.mims.ui.auth.AuthViewModel
import dev.iconpln.mims.ui.auth.otp.OtpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.Helper

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnKirim.setOnClickListener{
                tvMsgError.visibility = View.GONE
                validateEmail()
            }
        }

        viewModel.isLoading.observe(this){
            when(it){
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun validateEmail() {
        username = binding.edtEmail.text.toString()
        with(binding){

            if (username.isNullOrEmpty()){
                tvMsgError.visibility = View.VISIBLE
                tvMsgError.text = "Email tidak boleh kosong"
                return
            }

//            if(email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                tvMsgError.visibility = View.VISIBLE
//                tvMsgError.text = "Format email yang dimasukkan salah"
//                return
//            }

            sendRequest()
        }
    }

    private fun sendRequest() {
        viewModel.sendOtpForgotPassword(this, username )
        startActivity(
            Intent(this, OtpActivity::class.java)
            .putExtra("username", username)
            .putExtra("android_id", Helper.getAndroidId(this))
            .putExtra("device_data", Helper.deviceData)
            .putExtra("otpFrom", "forgotPassword"))
    }
}