package dev.iconpln.mims.ui.role.pabrikan.purchase_order.detail_purchase_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDetail
import dev.iconpln.mims.databinding.ActivityDetailPurchaseOrderBinding

class DetailPurchaseOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPurchaseOrderBinding
    private lateinit var adapter: DetailPurchaseOrderAdapter
    private lateinit var daoSession: DaoSession
    private var noMaterial: String = ""
    private var noPackaging: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPurchaseOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        adapter = DetailPurchaseOrderAdapter(arrayListOf(), object : DetailPurchaseOrderAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetail) {}

        })

        fetchDataLocal()

        with(binding){
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@DetailPurchaseOrderActivity,LinearLayoutManager.VERTICAL, false)

            btnClose.setOnClickListener { onBackPressed() }

        }

        doSearching()
    }

    private fun doSearching() {
        with(binding){
            srcNomorMaterial.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noMaterial = s.toString()
                    doSearch()
                }

            })

            srcNomorPackaging.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noPackaging = s.toString()
                    doSearch()
                }

            })
        }
    }

    private fun doSearch() {
        val listDataPo = daoSession.tPosDetailDao.queryBuilder().list()

        if (noMaterial.isNullOrEmpty()){
            var list = listDataPo.filter { it.noPackaging.toLowerCase().contains(noPackaging.toLowerCase()) }
            adapter.setPoList(list)
        }
        else if (noPackaging.isNullOrEmpty()){
            var list = listDataPo.filter { it.noMatSap.toLowerCase().contains(noMaterial.toLowerCase()) }
            adapter.setPoList(list)
        }else if (!noMaterial.isNullOrEmpty() && !noPackaging.isNullOrEmpty()){
            var list = listDataPo.filter {
                it.noMatSap.toLowerCase().contains(noMaterial.toLowerCase()) &&
                        it.noPackaging.toLowerCase().contains(noPackaging.toLowerCase()) }
            adapter.setPoList(list)

        }else{
            fetchDataLocal()
        }
    }

    private fun fetchDataLocal() {
        val listDataPo = daoSession.tPosDetailDao.queryBuilder().list()
        adapter.setPoList(listDataPo)
    }
}