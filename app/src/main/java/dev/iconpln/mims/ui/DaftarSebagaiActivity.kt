package dev.iconpln.mims.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.databinding.ActivityDaftarSebagaiBinding

class DaftarSebagaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarSebagaiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarSebagaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPabrikan.setOnClickListener {
                val intentPabrikan = Intent(this@DaftarSebagaiActivity, OtpActivity::class.java)
                startActivity(intentPabrikan)
            }
            btnPusertif.setOnClickListener {
                val intentPusertif = Intent(this@DaftarSebagaiActivity, OtpActivity::class.java)
                startActivity(intentPusertif)
            }
        }
    }
}