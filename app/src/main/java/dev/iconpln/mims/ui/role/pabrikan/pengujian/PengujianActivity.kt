package dev.iconpln.mims.ui.role.pabrikan.pengujian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPengujianBinding
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PengujianActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPengujianBinding
    private val pengujianViewModel: PengujianViewModel by viewModels()
    private lateinit var rvAdapter: ListPengujianAdapter
    private var noPengujian: String? = ""
    private var status: String? = "BELUM UJI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengujianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kategori = resources.getStringArray(R.array.kategori_pengujian)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, kategori)
        binding.statusKategori.setAdapter(arrayAdapter)

        rvAdapter = ListPengujianAdapter()

//        list.addAll(getListTanggalUji())
//        showRecyclerList()

        binding.apply {
            rvPengujian.layoutManager = LinearLayoutManager(this@PengujianActivity)
            rvPengujian.adapter = rvAdapter
        }

        pengujianViewModel.getPengujian(noPengujian, status)

        pengujianViewModel.pengujianResponse.observe(this){
//            Log.d("PengujianActivity", "${it.data}")
            Toast.makeText(this@PengujianActivity, "cek ${it.data}", Toast.LENGTH_SHORT).show()
            rvAdapter.setData(it.data)
        }

        binding.srcNoPengujian.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    val mQuery = query.uppercase(Locale.ROOT)
                    noPengujian = mQuery
                    pengujianViewModel.getPengujian(noPengujian, status)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    noPengujian = newText.uppercase(Locale.ROOT)
                    pengujianViewModel.getPengujian(noPengujian, status)
                }
                return false
            }
        })

        binding.statusKategori.setOnItemClickListener { _, _, _, _ ->
            status = binding.statusKategori.text.toString()
            pengujianViewModel.getPengujian(noPengujian,status)
        }
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

//    private fun showRecyclerList(){
//        binding.rvTanggalUji.layoutManager = LinearLayoutManager(this)
//        val listPengujianAdapter = ListPengujianAdapter(list)
//        binding.rvTanggalUji.adapter = listPengujianAdapter
//    }
}