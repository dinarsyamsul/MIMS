package dev.iconpln.mims.ui.pemakaian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.databinding.ActivityDetailPemakaianUlpMaximoBinding

class DetailPemakaianUlpMaximoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPemakaianUlpMaximoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemakaianUlpMaximoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}