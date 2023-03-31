package dev.iconpln.mims

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class RegisterSnMaterialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_sn_material)

        val btnKlik: AppCompatButton = findViewById(R.id.btn_regis)

        btnKlik.setOnClickListener {
            showCustomDialogBox()
        }
    }

    private fun showCustomDialogBox() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.activity_alert_dialog_registrasi, null)
        val btnClose = view.findViewById<Button>(R.id.btn_close)
        val btnRegis = view.findViewById<AppCompatButton>(R.id.btn_regis1)
        builder.setView(view)
        btnClose.setOnClickListener {
            builder.dismiss()
        }
        btnRegis.setOnClickListener {
            Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}