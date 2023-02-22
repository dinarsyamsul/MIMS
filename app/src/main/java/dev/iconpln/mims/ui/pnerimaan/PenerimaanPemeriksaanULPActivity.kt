package dev.iconpln.mims.ui.pnerimaan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPenerimaanPemeriksaanUlpactivityBinding
import dev.iconpln.mims.databinding.ActivityPenerimaanUlpactivityBinding
import java.text.SimpleDateFormat
import java.util.*

class PenerimaanPemeriksaanULPActivity : AppCompatActivity() {

    private lateinit var cal: Calendar
    private lateinit var binding: ActivityPenerimaanPemeriksaanUlpactivityBinding
    private val list = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanPemeriksaanUlpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cal = Calendar.getInstance()

        list.addAll(arrayListOf("1", "2", "3", "4", "5", "6", "7", "8"))
        showRecyclerView()
    }

    private fun showRecyclerView() {
        binding.rvPenerimaanpemeriksaan.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = PenerimaanULPAdapter(list)
        binding.rvPenerimaanpemeriksaan.adapter = listDataAdapter
    }
}