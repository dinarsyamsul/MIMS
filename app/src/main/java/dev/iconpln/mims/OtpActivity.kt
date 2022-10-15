package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class OtpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSubmitotp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        btnSubmitotp = findViewById(R.id.btnSubmitotp)
        btnSubmitotp.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnSubmitotp -> run {
                val intentOtp = Intent(this@OtpActivity, DashboardActivity::class.java)
                startActivity(intentOtp)
            }
        }

    }
}