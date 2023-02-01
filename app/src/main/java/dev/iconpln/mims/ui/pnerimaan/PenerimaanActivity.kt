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
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaanDao
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ActivityPenerimaanBinding
import dev.iconpln.mims.ui.pnerimaan.detail_penerimaan.DetailPenerimaanActivity


class PenerimaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenerimaanBinding
    private val viewModel: PenerimaanViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PenerimaanAdapter
    private var noDo: String = ""
    private var statusPenerimaan: String = ""
    private lateinit var listPenerimaan:  List<TPosPenerimaan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        listPenerimaan = daoSession.tPosPenerimaanDao.queryBuilder().list()

        viewModel.getPenerimaan(daoSession, listPenerimaan)

        adapter = PenerimaanAdapter(arrayListOf(), object : PenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosPenerimaan) {
                val listDetailPenerimaan = daoSession.tPosDetailPenerimaanDao.queryBuilder()
                    .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(po.noDoSmar)).list()

                if (po.isChecked == 1){
                    Toast.makeText(this@PenerimaanActivity, "Anda sudah melakukan penerimaan", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.insertDetailPenerimaan(daoSession,po.noDoSmar,listDetailPenerimaan)

                    startActivity(Intent(this@PenerimaanActivity, DetailPenerimaanActivity::class.java)
                        .putExtra("do", po.noDoSmar))
                }
            }

        })

        viewModel.penerimaanResponse.observe(this){
            adapter.setPoList(listPenerimaan)
        }

        with(binding){
            rvPenerimaan.adapter = adapter
            rvPenerimaan.setHasFixedSize(true)
            rvPenerimaan.layoutManager = LinearLayoutManager(this@PenerimaanActivity, LinearLayoutManager.VERTICAL, false)

            val statusArray = arrayOf(
                "DIKIRIM",
                "REJECTED",
                "PROCESSED",
                "APPROVED"
            )
            val adapterStatus = ArrayAdapter(this@PenerimaanActivity, android.R.layout.simple_dropdown_item_1line, statusArray)
            dropdownStatusPenerimaan.setAdapter(adapterStatus)
            dropdownStatusPenerimaan.setOnItemClickListener { parent, view, position, id ->
                statusPenerimaan = statusArray[position]
                if (noDo.isNotEmpty()){
                    val filter = listPenerimaan.filter { it.noDoSmar.lowercase().contains(noDo.lowercase())
                            && it.doStatus.lowercase().contains(statusPenerimaan.lowercase())}
                    adapter.setPoList(filter)
                }else{
                    val filter = listPenerimaan.filter { it.doStatus.lowercase().contains(statusArray[position].lowercase()) }
                    adapter.setPoList(filter)
                }
            }

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
                    if (statusPenerimaan.isNotEmpty()){
                        val filter = listPenerimaan.filter { it.noDoSmar.lowercase().contains(s.toString().lowercase())
                                && it.doStatus.lowercase().contains(statusPenerimaan.lowercase())}
                        adapter.setPoList(filter)
                    }else{
                        val filter = listPenerimaan.filter { it.noDoSmar.lowercase().contains(s.toString().lowercase()) }
                        adapter.setPoList(filter)
                    }
                }

            })

            viewModel.isLoading.observe(this@PenerimaanActivity){
                when(it){
                    true -> progressBar.visibility = View.VISIBLE
                    false -> progressBar.visibility = View.GONE
                }
            }
        }
    }
}