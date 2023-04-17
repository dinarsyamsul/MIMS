package dev.iconpln.mims.ui.pemeriksaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDao
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.databinding.ActivityPemeriksaanBinding
import dev.iconpln.mims.ui.pemeriksaan.input_data_pemeriksa.InputDataPemeriksaActivity
import dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail.PemeriksaanDetailActivity

class PemeriksaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanBinding
    private val viewModel: PemeriksaanViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PemeriksaanAdapter
    private var noDo: String = ""
    private var statusPemeriksaan: String = ""
    private lateinit var listPemeriksaan:  List<TPemeriksaan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        viewModel.getPemeriksaan(daoSession)

        adapter = PemeriksaanAdapter(arrayListOf(), object : PemeriksaanAdapter.OnAdapterListener{
            override fun onClick(po: TPemeriksaan) {
                if (po.namaKetua.isNotEmpty()){
                    Toast.makeText(this@PemeriksaanActivity, "Kamu sudah menyelesaikan DO ini", Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@PemeriksaanActivity, InputDataPemeriksaActivity::class.java)
                        .putExtra("noPemeriksaan", po.noPemeriksaan))
                }
            }

        }, object: PemeriksaanAdapter.OnAdapterListenerDoc{
            override fun onClick(po: TPemeriksaan) {
                if (po.namaKetua.isNotEmpty()){
                    if (po.isDone == 1){
                        Toast.makeText(this@PemeriksaanActivity, "Kamu sudah menyelesaikan DO ini", Toast.LENGTH_SHORT).show()
                    }else{
                        var listPemDetail = daoSession.tPemeriksaanDetailDao.queryBuilder()
                            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(po.noPemeriksaan))
                            .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
                            .where(TPemeriksaanDetailDao.Properties.StatusPemeriksaan.eq("BELUM DIPERIKSA"))
                            .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
                            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.notEq(""))
                            .list()
                        if (listPemDetail.size == 0){
                            Toast.makeText(this@PemeriksaanActivity, "DO ini sudah selesai diperiksa", Toast.LENGTH_SHORT).show()
                        }else{
                            startActivity(Intent(this@PemeriksaanActivity, PemeriksaanDetailActivity::class.java)
                                .putExtra("noPemeriksaan", po.noPemeriksaan)
                                .putExtra("noDo", po.noDoSmar))
                        }
                    }
                }else{
                    Toast.makeText(this@PemeriksaanActivity, "Kamu belum input petugas pemeriksa", Toast.LENGTH_SHORT).show()
                }
            }

        },daoSession)

        viewModel.pemeriksaanResponse.observe(this){
            adapter.setPeList(it)
            listPemeriksaan = it
            binding.tvTotalData.text = "Total: ${it.size} data"
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
                    val search = daoSession.tPemeriksaanDao.queryBuilder()
                        .whereOr(TPemeriksaanDao.Properties.NoDoSmar.like("%"+noDo+"%")
                        ,TPemeriksaanDao.Properties.PoSapNo.like("%"+noDo+"%")).list()
                    rvPemeriksaan.adapter = null
                    rvPemeriksaan.layoutManager = null

                    rvPemeriksaan.adapter = adapter
                    rvPemeriksaan.setHasFixedSize(true)
                    rvPemeriksaan.layoutManager = LinearLayoutManager(this@PemeriksaanActivity,LinearLayoutManager.VERTICAL, false)
                    adapter.setPeList(search)
                }

            })

            val statusArray = arrayOf(
                "TERBARU","TERLAMA"
            )
            val adapterStatus = ArrayAdapter(this@PemeriksaanActivity, android.R.layout.simple_dropdown_item_1line, statusArray)
            dropdownUrutkan.setAdapter(adapterStatus)
            dropdownUrutkan.setOnItemClickListener { parent, view, position, id ->
                statusPemeriksaan = statusArray[position]
                if (statusPemeriksaan == "TERBARU"){
                    val search = daoSession.tPemeriksaanDao.queryBuilder()
                        .whereOr(TPemeriksaanDao.Properties.NoDoSmar.like("%"+noDo+"%")
                            ,TPemeriksaanDao.Properties.PoSapNo.like("%"+noDo+"%"))
                        .orderDesc(TPemeriksaanDao.Properties.CreatedDate).list()
                    rvPemeriksaan.adapter = null
                    rvPemeriksaan.layoutManager = null

                    rvPemeriksaan.adapter = adapter
                    rvPemeriksaan.setHasFixedSize(true)
                    rvPemeriksaan.layoutManager = LinearLayoutManager(this@PemeriksaanActivity,LinearLayoutManager.VERTICAL, false)
                    adapter.setPeList(search)
                }else{
                    val search = daoSession.tPemeriksaanDao.queryBuilder()
                        .whereOr(TPemeriksaanDao.Properties.NoDoSmar.like("%"+noDo+"%")
                            ,TPemeriksaanDao.Properties.PoSapNo.like("%"+noDo+"%"))
                        .orderAsc(TPemeriksaanDao.Properties.CreatedDate).list()
                    rvPemeriksaan.adapter = null
                    rvPemeriksaan.layoutManager = null

                    rvPemeriksaan.adapter = adapter
                    rvPemeriksaan.setHasFixedSize(true)
                    rvPemeriksaan.layoutManager = LinearLayoutManager(this@PemeriksaanActivity,LinearLayoutManager.VERTICAL, false)
                    adapter.setPeList(search)
                    }

            }
        }
    }
}