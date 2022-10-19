package dev.iconpln.mims.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import dev.iconpln.mims.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnLogin -> run {
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }
}