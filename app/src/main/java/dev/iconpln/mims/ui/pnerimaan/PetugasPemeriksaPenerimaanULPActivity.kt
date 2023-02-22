package dev.iconpln.mims.ui.pnerimaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPetugasPemeriksaPenerimaanUlpBinding
import dev.iconpln.mims.databinding.ActivityPetugasPenerimaanUlpBinding

class PetugasPemeriksaPenerimaanULPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetugasPemeriksaPenerimaanUlpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetugasPemeriksaPenerimaanUlpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kepalaGudangArray = arrayOf(
            "Kepala  Gudang 1", "Kepala Gudang 2"
        )
        val adapterStatus = ArrayAdapter(
            this@PetugasPemeriksaPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            kepalaGudangArray
        )
        binding.dropdownKepalaGudang.setAdapter(adapterStatus)
        binding.dropdownKepalaGudang.setOnItemClickListener { parent, view, position, id ->
            kepalaGudangArray[position]
        }

        val pemeriksaArray = arrayOf(
            "Pemeriksa 1", "Pemeriksa 2"
        )
        val pemeriksaAdapterStatus = ArrayAdapter(
            this@PetugasPemeriksaPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            pemeriksaArray
        )
        binding.dropdownPetugasPemeriksa.setAdapter(pemeriksaAdapterStatus)
        binding.dropdownPetugasPemeriksa.setOnItemClickListener { parent, view, position, id ->
            pemeriksaArray[position]
        }

        val pejabatPemeriksaArray = arrayOf(
            "Pejabat Pemeriksa 1", "Pejabat Pemeriksa 2"
        )
        val pejabatPemeriksaAdapterStatus = ArrayAdapter(
            this@PetugasPemeriksaPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            pejabatPemeriksaArray
        )
        binding.dropdownNamaPejabatPemeriksa.setAdapter(pejabatPemeriksaAdapterStatus)
        binding.dropdownNamaPejabatPemeriksa.setOnItemClickListener { parent, view, position, id ->
            pejabatPemeriksaArray[position]
        }

        val pejabatArray = arrayOf(
            "Pejabat 1", "Pejabat 2"
        )
        val pejabatAdapterStatus = ArrayAdapter(
            this@PetugasPemeriksaPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            pejabatArray
        )
        binding.dropdownJabatan.setAdapter(pejabatAdapterStatus)
        binding.dropdownJabatan.setOnItemClickListener { parent, view, position, id ->
            pejabatArray[position]
        }
        binding.dropdownJabatan2.setAdapter(pejabatAdapterStatus)
        binding.dropdownJabatan2.setOnItemClickListener { parent, view, position, id ->
            pejabatArray[position]
        }

        binding.btnLanjut.setOnClickListener {
            startActivity(Intent(this, PenerimaanPemeriksaanULPActivity::class.java))
        }
    }
}