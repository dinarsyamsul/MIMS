package dev.iconpln.mims.ui.pnerimaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPenerimaanSetelahPetugasPenerimaanUlpactivityBinding
import dev.iconpln.mims.databinding.ActivityPenerimaanUlpactivityBinding

class PenerimaanSetelahPetugasPenerimaanULPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenerimaanSetelahPetugasPenerimaanUlpactivityBinding
    private val list = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanSetelahPetugasPenerimaanUlpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list.addAll(arrayListOf("1", "2", "3", "4", "5", "6", "7", "8"))
        showRecyclerView()
    }

    private fun showRecyclerView() {
        binding.rvPenerimaanpemeriksaan.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = PenerimaanULPAdapter(list)
        binding.rvPenerimaanpemeriksaan.adapter = listDataAdapter
        listDataAdapter.setOnItemClickCallback(object : PenerimaanULPAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                startActivity(
                    Intent(
                        this@PenerimaanSetelahPetugasPenerimaanULPActivity,
                        PetugasPenerimaanULPActivity::class.java
                    )
                )
            }
        })
    }
}