package dev.iconpln.mims.ui.pemakaian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPemakaianBinding

class PemakaianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPemakaianBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemakaianBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}