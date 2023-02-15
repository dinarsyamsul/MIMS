package dev.iconpln.mims.ui.pemeriksaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ActivityPemeriksaanBinding
import dev.iconpln.mims.ui.pemeriksaan.input_petugas_penerimaan.InputPetugasPenerimaanActivity
import dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail.PemeriksaanDetailActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanAdapter
import dev.iconpln.mims.ui.pnerimaan.PenerimaanViewModel
import dev.iconpln.mims.ui.rating.RatingActivity

class PemeriksaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanBinding
    private val viewModel: PemeriksaanViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PemeriksaanAdapter
    private var noDo: String = ""
    private var statusPemeriksaan: String = ""
    private lateinit var listPemeriksaan:  List<TPemeriksaan>
    private lateinit var dataPemeriksaanDetail: TPemeriksaanDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        listPemeriksaan = daoSession.tPemeriksaanDao.queryBuilder().list()


        viewModel.setPemeriksaan(daoSession,listPemeriksaan)

        adapter = PemeriksaanAdapter(arrayListOf(), object : PemeriksaanAdapter.OnAdapterListener{
            override fun onClick(po: TPemeriksaan) {
                if (po.isDone == 1){
                    Toast.makeText(this@PemeriksaanActivity, "Kamu sudah menyelesaikan DO ini", Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@PemeriksaanActivity, InputPetugasPenerimaanActivity::class.java)
                        .putExtra("noDo", po.noDoSmar))
                }
            }

        }, object: PemeriksaanAdapter.OnAdapterListenerDoc{
            override fun onClick(po: TPemeriksaan) {
                if (po.isDone == 1){
                    Toast.makeText(this@PemeriksaanActivity, "Kamu sudah menyelesaikan DO ini", Toast.LENGTH_SHORT).show()
                }else{
                    if (po.state != 2){
                        Toast.makeText(this@PemeriksaanActivity, "Kamu belum bisa melakukan laporan dokumen", Toast.LENGTH_SHORT).show()
                    }else{
                        startActivity(Intent(this@PemeriksaanActivity, PemeriksaanDetailActivity::class.java)
                            .putExtra("noPemeriksaan", po.noPemeriksaan)
                            .putExtra("noDo", po.noDoSmar))
                    }
                }
            }

        }, object : PemeriksaanAdapter.OnAdapterListenerRate {
            override fun onClick(po: TPemeriksaan) {
                if (po.isDone == 1){
                    Toast.makeText(this@PemeriksaanActivity, "Kamu sudah menyelesaikan DO ini", Toast.LENGTH_SHORT).show()
                }else{
                    if (po.state != 2){
                        Toast.makeText(this@PemeriksaanActivity, "Kamu belum bisa melakukan laporan dokumen", Toast.LENGTH_SHORT).show()
                    }else{
                        startActivity(Intent(this@PemeriksaanActivity, RatingActivity::class.java)
                            .putExtra("noPemeriksaan", po.noPemeriksaan)
                            .putExtra("noDo", po.noDoSmar))
                    }
                }
//                startActivity(Intent(this@PemeriksaanActivity, RatingActivity::class.java)
//                        .putExtra("noPemeriksaan", po.noPemeriksaan)
//                        .putExtra("noDo", po.noDoSmar))
            }

        })

        viewModel.pemeriksaanResponse.observe(this){
            adapter.setPeList(it)
        }

        viewModel.isLoading.observe(this){
            when(it){
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            rvPemeriksaan.adapter = adapter
            rvPemeriksaan.setHasFixedSize(true)
            rvPemeriksaan.layoutManager = LinearLayoutManager(this@PemeriksaanActivity,LinearLayoutManager.VERTICAL, false)

            srcNomorPoDo.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noDo = s.toString()
                    val filter = listPemeriksaan.filter {
                        it.noDoSmar.lowercase().contains(s.toString().lowercase())
                    }
                    adapter.setPeList(filter)
                }

            })

            val statusArray = arrayOf(
                "TERBARU","TERLAMA"
            )
            val adapterStatus = ArrayAdapter(this@PemeriksaanActivity, android.R.layout.simple_dropdown_item_1line, statusArray)
            dropdownUrutkan.setAdapter(adapterStatus)
            dropdownUrutkan.setOnItemClickListener { parent, view, position, id ->
                statusPemeriksaan = statusArray[position]
                if (noDo.isNotEmpty()){
                    val filter = listPemeriksaan.filter { it.noDoSmar.lowercase().contains(noDo.lowercase())
                            && it.doStatus.lowercase().contains(statusPemeriksaan.lowercase())}
                    adapter.setPeList(filter)
                }else{
                    val filter = listPemeriksaan.filter { it.doStatus.lowercase().contains(statusArray[position].lowercase()) }
                    adapter.setPeList(filter)
                }
            }
        }
    }
}