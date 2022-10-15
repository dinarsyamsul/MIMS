package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class DaftarSebagaiActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnPabrikan : ImageButton
    private lateinit var btnPusertif : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_sebagai)

        btnPabrikan = findViewById(R.id.btnPabrikan)
        btnPabrikan.setOnClickListener(this)

        btnPusertif = findViewById(R.id.btnPusertif)
        btnPusertif.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnPabrikan -> run {
                val intentPabrikan = Intent(this@DaftarSebagaiActivity, OtpActivity::class.java)
                startActivity(intentPabrikan)
            }
        }
    }
}