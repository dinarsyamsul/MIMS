package dev.iconpln.mims.ui.auth.change_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityChangePasswordBinding
import dev.iconpln.mims.ui.auth.AuthViewModel
import dev.iconpln.mims.ui.auth.LoginActivity

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("username").toString()

        viewModel.changePassword.observe(this){
            when(it.message){
                "update password berhasil" -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "silahkan login dengan password baru anda", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else -> Toast.makeText(this, "ada kesalahan saat mengganti password", Toast.LENGTH_SHORT).show()
            }
        }

        with(binding){
            btnKirim.setOnClickListener{
                tvMsgError.visibility = View.GONE
                val password = edtPasswordBaru.text.toString()
                val konfPassword = edtPassKonfirmasi.text.toString()

                when{
                    password.isNullOrEmpty() -> {
                        tvMsgError.visibility = View.VISIBLE
                        tvMsgError.text = "Password baru tidak boleh kosong"
                    }
                    konfPassword.isNullOrEmpty() -> {
                        tvMsgError.visibility = View.VISIBLE
                        tvMsgError.text = "Silahkan masukkan konfirmasi password"
                    }
                    else -> viewModel.changePassword(this@ChangePasswordActivity, username, password)
                }
            }
        }
    }
}