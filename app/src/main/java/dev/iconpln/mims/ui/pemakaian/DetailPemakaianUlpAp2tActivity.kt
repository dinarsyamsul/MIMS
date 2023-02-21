package dev.iconpln.mims.ui.pemakaian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.databinding.ActivityDetailPemakaianUlpAp2tBinding

class DetailPemakaianUlpAp2tActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPemakaianUlpAp2tBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemakaianUlpAp2tBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}