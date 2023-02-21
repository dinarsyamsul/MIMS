package dev.iconpln.mims.ui.pemakaian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityDetailPemakaianUlpMandiriBinding

class DetailPemakaianUlpMandiriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPemakaianUlpMandiriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemakaianUlpMandiriBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}