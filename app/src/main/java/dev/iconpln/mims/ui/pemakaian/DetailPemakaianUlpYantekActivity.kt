package dev.iconpln.mims.ui.pemakaian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.databinding.ActivityDetailPemakaianUlpYantekBinding

class DetailPemakaianUlpYantekActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPemakaianUlpYantekBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemakaianUlpYantekBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}