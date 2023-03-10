package dev.iconpln.mims.ui.pnerimaan

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ActivityPenerimaanBinding
import dev.iconpln.mims.ui.pnerimaan.detail_penerimaan.DetailPenerimaanActivity
import dev.iconpln.mims.ui.pnerimaan.input_petugas.InputPetugasPenerimaanActivity
import dev.iconpln.mims.ui.rating.RatingActivity

class PenerimaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenerimaanBinding
    private val viewModel: PenerimaanViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PenerimaanAdapter
    private var filter : String = ""
    private var srcNoDo: String = ""
    private lateinit var penerimaans:  List<TPosPenerimaan>
    private lateinit var penerimaanDet: List<TPosDetailPenerimaan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        penerimaans = daoSession.tPosPenerimaanDao.queryBuilder()
            .where(TPosPenerimaanDao.Properties.KodeStatusDoMims.eq("102")).list()

        penerimaanDet = daoSession.tPosDetailPenerimaanDao.queryBuilder()
            .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0))
            .where(TPosDetailPenerimaanDao.Properties.IsChecked.eq(0)).list()

        viewModel.getPenerimaan(daoSession,penerimaans)
        viewModel.insertDetailPenerimaan(daoSession,penerimaanDet)

        adapter = PenerimaanAdapter(arrayListOf(), object : PenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosPenerimaan) {
                if(po.petugasPenerima.isNullOrEmpty()){
                    startActivity(Intent(this@PenerimaanActivity, InputPetugasPenerimaanActivity::class.java)
                        .putExtra("noDo", po.noDoSmar))
                }else{
                    Toast.makeText(this@PenerimaanActivity, "Kamu sudah melakukan input data penerimaan", Toast.LENGTH_SHORT).show()
                }

            }

        }, object: PenerimaanAdapter.OnAdapterListenerDoc{
            override fun onClick(po: TPosPenerimaan) {
                if (po.tanggalDiterima.isNullOrEmpty()){
                    Toast.makeText(this@PenerimaanActivity, "Kamu belum melakukan input data penerimaan", Toast.LENGTH_SHORT).show()
                }else{
                    val penerimaans = daoSession.tPosDetailPenerimaanDao.queryBuilder()
                        .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(po.noDoSmar))
                        .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0)).list()

                    if (penerimaans.isNullOrEmpty()){
                        Toast.makeText(this@PenerimaanActivity, "Kamu sudah melakukan pemeriksaan dokumen di DO ini", Toast.LENGTH_SHORT).show()
                    }else{
                        startActivity(Intent(this@PenerimaanActivity, DetailPenerimaanActivity::class.java)
                            .putExtra("noDo", po.noDoSmar))
                    }
                }
            }

        }, object : PenerimaanAdapter.OnAdapterListenerRate {
            override fun onClick(po: TPosPenerimaan) {
                if (po.isDone == 1){
                    Toast.makeText(this@PenerimaanActivity, "Kamu sudah melakukan rating di DO ini", Toast.LENGTH_SHORT).show()
                }else{
                    if (po.isRating == 1){
                        startActivity(Intent(this@PenerimaanActivity, RatingActivity::class.java)
                            .putExtra("noDo", po.noDoSmar))
                    }else{
                        Toast.makeText(this@PenerimaanActivity, "Kamu belum bisa melakukan rating", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        },daoSession)

        viewModel.penerimaanResponse.observe(this){
            adapter.setData(it)
        }

        viewModel.isLoading.observe(this){
            when(it){
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            setRecyclerView(false)

            srcNomorPoDo.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    srcNoDo = s.toString()
                    val filter = penerimaans.filter {
                        it.noDoSmar.lowercase().contains(s.toString().lowercase())
                    }
                    adapter.setData(filter)
                }

            })

            val statusArray = arrayOf(
                "TERBARU","TERLAMA"
            )
            val adapterStatus = ArrayAdapter(this@PenerimaanActivity, android.R.layout.simple_dropdown_item_1line, statusArray)
            dropdownUrutkan.setAdapter(adapterStatus)
            dropdownUrutkan.setOnItemClickListener { parent, view, position, id ->
                filter = statusArray[position]
                if (filter == "TERBARU"){
                    setRecyclerView(false)
                }else{
                    setRecyclerView(true)
                }
            }
        }
    }

    private fun setRecyclerView(reverse: Boolean) {
        binding.rvPenerimaan.adapter = adapter
        binding.rvPenerimaan.setHasFixedSize(true)
        val lm = LinearLayoutManager(this@PenerimaanActivity)
        lm.stackFromEnd = reverse
        lm.orientation = LinearLayoutManager.VERTICAL
        lm.reverseLayout = reverse
        binding.rvPenerimaan.layoutManager = lm

    }
}