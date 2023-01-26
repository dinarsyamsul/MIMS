package dev.iconpln.mims.ui.role.pabrikan.arttribute_material.detail_attribute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMaterialDetail
import dev.iconpln.mims.databinding.ActivityDetailDataAtributeMaterialBinding
import dev.iconpln.mims.ui.role.pabrikan.arttribute_material.MaterialViewModel
import java.util.*

class DetailDataAtributeMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDataAtributeMaterialBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: DetailDataAttributeAdapter
    private var sn: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataAtributeMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!
        adapter = DetailDataAttributeAdapter(arrayListOf(), object : DetailDataAttributeAdapter.OnAdapterListener{
            override fun onClick(po: TMaterialDetail) {}

        })

        fetchLocal()

        with(binding){
            rvDetailMaterial.adapter = adapter
            rvDetailMaterial.setHasFixedSize(true)
            rvDetailMaterial.layoutManager = LinearLayoutManager(this@DetailDataAtributeMaterialActivity, LinearLayoutManager.VERTICAL, false)

            srcSnMaterial.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    sn = s.toString()
                    doSearch()
                }

            })

            btnClose.setOnClickListener { onBackPressed() }
        }
    }

    private fun doSearch() {
        val listFilter = daoSession.tMaterialDetailDao.queryBuilder().list().filter { it.noProduksi.contains(sn) }
        adapter.setMaterialList(listFilter)
    }

    private fun fetchLocal() {
        val list = daoSession.tMaterialDetailDao.queryBuilder().list()
        adapter.setMaterialList(list)
    }
}