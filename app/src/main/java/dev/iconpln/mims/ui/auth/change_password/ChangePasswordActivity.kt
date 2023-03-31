package dev.iconpln.mims.ui.auth.change_password

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityChangePasswordBinding
import dev.iconpln.mims.ui.auth.AuthViewModel
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.utils.SharedPrefsUtils

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var username: String
    private lateinit var dialog : Dialog
    private var usernameFp = ""
    private var isForgetPassword=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this@ChangePasswordActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        username = SharedPrefsUtils.getStringPreference(this,"username","")!!
        usernameFp = intent.getStringExtra("username").toString()
        isForgetPassword= intent.getIntExtra("isForgetPassword",0)==1
        Log.i("varIsForgetPass","${isForgetPassword}")

        viewModel.isLoading.observe(this){
            if (it) dialog.show() else dialog.dismiss()
        }

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
                val passwordBaru = edtPasswordBaru.text.toString()
                val konfPasswordBaru = edtKonfirmasiPasswordBaru.text.toString()
                val passwordSaatIni = edtPasswordSaatIni.text.toString()

                if(isForgetPassword){
                    when{
                        passwordBaru.isNullOrEmpty() -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Password baru tidak boleh kosong"
                        }
                        konfPasswordBaru.isNullOrEmpty() -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Silahkan masukkan konfirmasi password"
                        }
                        passwordBaru != konfPasswordBaru -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Password baru tidak cocok dengan konfirmasi password"
                        }
                        else -> viewModel.forgotPassword(this@ChangePasswordActivity, username, passwordBaru)
                    }
                }
                else{
                    when{
                        passwordSaatIni.isNullOrEmpty() -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Password saat ini tidak boleh kosong"
                        }
                        passwordBaru.isNullOrEmpty() -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Password baru tidak boleh kosong"
                        }
                        konfPasswordBaru.isNullOrEmpty() -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Silahkan masukkan konfirmasi password"
                        }
                        passwordBaru != konfPasswordBaru -> {
                            tvMsgError.visibility = View.VISIBLE
                            tvMsgError.text = "Password baru tidak cocok dengan konfirmasi password"
                        }
                        else -> viewModel.changePasswordProfile(this@ChangePasswordActivity, username, passwordBaru,passwordSaatIni)
                    }
                }


            }
        }
        initForgetPassword()
    }

    private fun initForgetPassword(){
        if(isForgetPassword){
            with(binding){
                textView2.visibility=View.GONE
                textInputLayout.visibility=View.GONE
                lblPasswordSaatIni.visibility=View.GONE
            }
        }
    }
}