package dev.iconpln.mims.ui.role.up3.pnerimaan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ActivityPenerimaanBinding
import dev.iconpln.mims.ui.role.up3.pnerimaan.detail_penerimaan.DetailPenerimaanActivity


class PenerimaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenerimaanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PenerimaanAdapter
    private var noDo: String = ""
    private var statusPenerimaan: String = ""

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        adapter = PenerimaanAdapter(arrayListOf(), object : PenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosPenerimaan) {
                startActivity(Intent(this@PenerimaanActivity, DetailPenerimaanActivity::class.java))
            }

        })

        fetchLocal()

        with(binding){
            rvPenerimaan.adapter = adapter
            rvPenerimaan.setHasFixedSize(true)
            rvPenerimaan.layoutManager = LinearLayoutManager(this@PenerimaanActivity, LinearLayoutManager.VERTICAL, false)

            val statusArray = arrayOf(
                "TERIMA",
                "PROSES PENGIRIMAN",
                "BELUM DIKIRIM"
            )
            val adapterStatus = ArrayAdapter(this@PenerimaanActivity, android.R.layout.simple_dropdown_item_1line, statusArray)
            dropdownStatusPenerimaan.setAdapter(adapterStatus)
            statusPenerimaan = dropdownStatusPenerimaan.text.toString()
            dropdownStatusPenerimaan.setOnItemClickListener { parent, view, position, id ->
                doSearch()
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
                    doSearch()
                }

            })
        }
    }

    private fun doSearch() {
        val list = daoSession.tPosPenerimaanDao.queryBuilder().list()
        if (noDo.isNotEmpty()){
            val listFilter = list.filter { it.noDoSmar.trim().toLowerCase().contains(noDo.toLowerCase()) }
            adapter.setPoList(listFilter)
        }
        else if (statusPenerimaan.isNotEmpty()){
            val listFilter = list.filter { it.doStatus == statusPenerimaan }
            adapter.setPoList(listFilter)
        }else if (noDo.isNotEmpty() && statusPenerimaan.isNotEmpty()) {
            val listFilter = list.filter {
                it.doStatus.trim().toLowerCase().contains(statusPenerimaan.toLowerCase())
                && it.noDoSmar.trim().toLowerCase().contains(noDo.toLowerCase())
            }
            adapter.setPoList(listFilter)
        }else if (noDo.isNullOrEmpty() && statusPenerimaan.isNullOrEmpty()){
            fetchLocal()
        }
    }

    private fun fetchLocal() {
        val list = daoSession.tPosPenerimaanDao.queryBuilder().list()
        adapter.setPoList(list)
    }
}