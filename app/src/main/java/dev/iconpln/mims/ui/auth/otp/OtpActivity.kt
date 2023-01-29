package dev.iconpln.mims.ui.auth.otp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.databinding.ActivityOtpBinding
import dev.iconpln.mims.ui.auth.AuthViewModel
import dev.iconpln.mims.ui.auth.change_password.ChangePasswordActivity
import dev.iconpln.mims.utils.Helper

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val viewModel: AuthViewModel by viewModels()
    private var username: String = ""
    private var password: String = ""
    private var androidId: String = ""
    private var deviceData: String = ""
    private var otpFrom: String = ""
    private var otpInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("username").toString()
        otpFrom = intent.getStringExtra("otpFrom").toString()
        androidId = Helper.getAndroidId(this)
        deviceData = Helper.deviceData

        viewModel.loginResponse.observe(this){
            when (it.message) {
                "DO LOGIN" -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                "OTP Not Found" -> {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.checkOtpForgotPassword.observe(this){
            when (it.message) {
                "OTP tervalidasi" -> {
                    startActivity(Intent(this, ChangePasswordActivity::class.java)
                        .putExtra("username", username))
                }
                "OTP Not Found" -> {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        with(binding){
            btnSubmitotp.setOnClickListener {
                otpInput = edtotp1.text.toString() +
                        edtotp2.text.toString() +
                        edtotp3.text.toString() +
                        edtotp4.text.toString() +
                        edtotp5.text.toString() +
                        edtotp6.text.toString()
                when(otpFrom){
                    "login" -> viewModel.checkOtp(this@OtpActivity,username,otpInput,androidId,deviceData)
                    "forgotPassword" -> viewModel.checkOtpForgotPassword(this@OtpActivity,username,otpInput)
                }
            }
        }

        viewModel.isLoading.observe(this) {
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input1.text.toString().isNotEmpty()) {
                    input1.clearFocus()
                    input2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtotp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input2.text.toString().isNotEmpty()) {
                    input2.clearFocus()
                    input3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtotp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input3.text.toString().isNotEmpty()) {
                    input3.clearFocus()
                    input4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtotp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input4.text.toString().isNotEmpty()) {
                    input4.clearFocus()
                    input5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtotp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (input5.text.toString().isNotEmpty()) {
                    input5.clearFocus()
                    input6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}