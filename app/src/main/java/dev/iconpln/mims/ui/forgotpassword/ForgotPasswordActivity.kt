package dev.iconpln.mims.ui.forgotpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityForgotPasswordBinding
import dev.iconpln.mims.ui.OtpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.Helper

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(forgotPasswordBinding.root)

        with(forgotPasswordBinding){
            btnKirim.setOnClickListener{
                tvMsgError.visibility = View.GONE
                validateEmail()
            }
        }
    }

    private fun validateEmail() {
        val email = forgotPasswordBinding.edtEmail.text.toString()
        with(forgotPasswordBinding){

            if (email.isNullOrEmpty()){
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
        startActivity(Intent(this, OtpActivity::class.java)
            .putExtra("extra_username", forgotPasswordBinding.edtEmail.text.toString())
            .putExtra("android_id", Helper.getAndroidId(this))
            .putExtra("device_data", Helper.deviceData)
            .putExtra("otp_type", Config.OTP_TYPE_FORGOT_PASSWORD))
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}