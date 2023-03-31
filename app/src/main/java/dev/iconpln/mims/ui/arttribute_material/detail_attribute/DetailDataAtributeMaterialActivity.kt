package dev.iconpln.mims.ui.arttribute_material.detail_attribute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMaterialDetail
import dev.iconpln.mims.data.local.database.TMaterialDetailDao
import dev.iconpln.mims.databinding.ActivityDetailDataAtributeMaterialBinding
import dev.iconpln.mims.ui.arttribute_material.MaterialViewModel

class DetailDataAtributeMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDataAtributeMaterialBinding
    private val materialViewModel: MaterialViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: DetailDataAttributeAdapter
    private var sn: String = ""
    private var noMaterial = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataAtributeMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noMaterial = intent.getStringExtra("noMaterial")!!

        daoSession = (application as MyApplication).daoSession!!
        adapter = DetailDataAttributeAdapter(arrayListOf(), object : DetailDataAttributeAdapter.OnAdapterListener{
            override fun onClick(po: TMaterialDetail) {}

        })

        fetchLocal()

        with(binding){
            btnClose.setOnClickListener { onBackPressed() }

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
        val listFilter = daoSession.tMaterialDetailDao.queryBuilder()
            .where(TMaterialDetailDao.Properties.SerialNumber.like("%"+sn+"%")).list()
        adapter.setMaterialList(listFilter)
    }

    private fun fetchLocal() {
        val list = daoSession.tMaterialDetailDao.queryBuilder()
            .where(TMaterialDetailDao.Properties.NomorMaterial.eq(noMaterial)).list()
        adapter.setMaterialList(list)
    }
}