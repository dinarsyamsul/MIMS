package dev.iconpln.mims

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.databinding.ActivityPengujianBinding

class Pengujian : AppCompatActivity() {

    private lateinit var binding: ActivityPengujianBinding
    private val list = ArrayList<PengujianRecycler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengujianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTanggalUji.setHasFixedSize(true)
        list.addAll(getListTanggalUji())
        showRecyclerList()
    }

    private fun getListTanggalUji(): ArrayList<PengujianRecycler>{
        val tanggalUji = resources.getStringArray(R.array.data_tahun)
        val listTanggalUji = ArrayList<PengujianRecycler>()
        for (i in tanggalUji.indices ){
            val tahun = PengujianRecycler(tanggalUji[i])
            listTanggalUji.add(tahun)
        }
        return listTanggalUji
    }

    private fun showRecyclerList(){
        binding.rvTanggalUji.layoutManager = LinearLayoutManager(this)
        val listPengujianAdapter = ListTanggalPengujianAdapter(list)
        binding.rvTanggalUji.adapter = listPengujianAdapter
    }
}