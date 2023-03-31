package dev.iconpln.mims.ui.auth.otp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.databinding.ActivityOtpBinding
import dev.iconpln.mims.ui.auth.AuthViewModel
import dev.iconpln.mims.ui.auth.change_password.ChangePasswordActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.Helper
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var session: SessionManager
    private var username: String = ""
    private var password: String = ""
    private var androidId: String = ""
    private var deviceData: String = ""
    private var otpFrom: String = ""
    private var otpInput: String = ""
    private var isForgetPassword:Boolean=false

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countdownTimer.start()

        dialog = Dialog(this@OtpActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        daoSession = (application as MyApplication).daoSession!!
        session = SessionManager(this)

        username = intent.getStringExtra("username").toString()
        otpFrom = intent.getStringExtra("otpFrom").toString()
        isForgetPassword=otpFrom.equals("forgotPassword")
        Log.i("varIsForgetPassOTP","${isForgetPassword}")

        androidId = Helper.getAndroidId(this)
        deviceData = Helper.deviceData

        viewModel.loginResponse.observe(this){
            when (it.message) {
                "DO LOGIN" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        session.sessionActivity(Config.IS_LOGIN)
                        session.saveAuthToken(
                            it.token.toString(),
                            it.user?.roleId.toString(),
                            it.user?.mail.toString(),
                            it.user?.kdUser.toString()
                        )
                        SharedPrefsUtils.setStringPreference(this@OtpActivity,"jwt", it.token!!)
                        SharedPrefsUtils.setStringPreference(this@OtpActivity, "username", username)
                        SharedPrefsUtils.setStringPreference(this@OtpActivity, "email", it.user?.mail!!)
                        SharedPrefsUtils.setStringPreference(this@OtpActivity, "password", password)
                        SharedPrefsUtils.setStringPreference(this@OtpActivity, "plant", it.user?.plant!!)
                        SharedPrefsUtils.setStringPreference(this@OtpActivity, "storloc", it.user?.storloc!!)

                        withContext(Dispatchers.Main){
                            val intentToHome = Intent(this@OtpActivity, HomeActivity::class.java)
                            startActivity(intentToHome)
                            finish()
                        }
                    }
                }
                "OTP NOT FOUND" -> {
                    Toast.makeText(this,"OTP salah atau tidak di temukan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.checkOtpForgotPassword.observe(this){
            when (it.message) {
                "OTP tervalidasi" -> {
                    startActivity(Intent(this, ChangePasswordActivity::class.java)
                        .putExtra("username", username)
                        .putExtra("isForgetPassword", isForgetPassword.compareTo(false)))
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
                    "login" -> viewModel.checkOtp(this@OtpActivity,username,otpInput,androidId,deviceData,daoSession)
                    "forgotPassword" -> viewModel.checkOtpForgotPassword(this@OtpActivity,username,otpInput)
                }
            }
        }

        viewModel.isLoading.observe(this) {
            if (it == true) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }

        autoNextInput()
    }

    val countdownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished / 1000
            binding.txtBelumMenerimaKode.text = "Meminta OTP kembali dalam: $secondsRemaining detik"
        }

        override fun onFinish() {
            binding.txtBelumMenerimaKode.text = "Minta OTP kembali"
        }
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