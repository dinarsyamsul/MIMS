package dev.iconpln.mims.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityOtpBinding
import dev.iconpln.mims.ui.forgotpassword.ForgotPasswordViewModel
import dev.iconpln.mims.ui.login.LoginViewModel
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import dev.iconpln.mims.utils.Config

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var dataFromIntent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataFromIntent = intent.getStringExtra("data").toString()

        val username = intent.extras?.getString(EXTRA_USERNAME)
        val androidId = intent.extras?.getString(ANDROID_ID)
        val deviceData = intent.extras?.getString(DEVICE_DATA)
        val otpType = intent.extras?.getString(OTP_TYPE)

        if (otpType == Config.OTP_TYPE_DO_LOGIN_OTP){
//            loginViewModel.hitEmail(username.toString())
        }else{
            forgotPasswordViewModel.getOtpPassword(username.toString())
        }

        Log.d("checkUsername", username.toString())

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
                    if (otpType == Config.OTP_TYPE_DO_LOGIN_OTP){
//                        loginViewModel.sendTokenOtp(otpInput, username,androidId!!,deviceData!!)
                    }
                }
            }
        }

        loginViewModel.verifyTokenResponse.observe(this) { result ->
            when (result.message) {
                "DO LOGIN" -> {
                    startActivity(Intent(this, DashboardPabrikanActivity::class.java))
                }
                "OTP Not Found" -> {
                    Toast.makeText(this,"OTP yang dimasukkan salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        loginViewModel.errorMessage.observe(this) {
//            if (it != null) {
//                Toast.makeText(this, "Token salah", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.txtBelumMenerimaKode.setOnClickListener {
//            loginViewModel.hitEmail(username.toString())
            Toast.makeText(this, "Kode OTP Di Kirim", Toast.LENGTH_SHORT).show()
        }

        loginViewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }

        autoNextInput()
    }

    private fun autoNextInput() {
        val input1 = binding.edtotp1
        val input2 = binding.edtotp2
        val input3 = binding.edtotp3
        val input4 = binding.edtotp4
        val input5 = binding.edtotp5
        val input6 = binding.edtotp6

        input1.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(input1, InputMethodManager.SHOW_IMPLICIT)

        binding.edtotp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input1.text.toString().isNotEmpty()) {
                    input1.clearFocus()
                    input2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }
        })

        binding.edtotp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input2.text.toString().isNotEmpty()) {
                    input2.clearFocus()
                    input3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }
        })

        binding.edtotp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input3.text.toString().isNotEmpty()) {
                    input3.clearFocus()
                    input4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }
        })

        binding.edtotp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input4.text.toString().isNotEmpty()) {
                    input4.clearFocus()
                    input5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }
        })

        binding.edtotp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input5.text.toString().isNotEmpty()) {
                    input5.clearFocus()
                    input6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }
        })
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val ANDROID_ID = "android_id"
        const val DEVICE_DATA = "device_data"
        const val OTP_TYPE = "otp_type"

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right)
    }
}