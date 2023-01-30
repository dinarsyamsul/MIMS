package dev.iconpln.mims.ui.role.pabrikan.pengujian.pengujian_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPengujianDetails
import dev.iconpln.mims.data.local.database.TPengujianDetailsDao
import dev.iconpln.mims.databinding.ActivityPengujianDetailBinding
import dev.iconpln.mims.ui.pengujian.pengujian_detail.PengujianDetailAdapter

class PengujianDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPengujianDetailBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PengujianDetailAdapter
    private var serNumb: String = ""
    private var filter: String = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengujianDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!

        adapter = PengujianDetailAdapter(arrayListOf(),object: PengujianDetailAdapter.OnAdapterListener{
            override fun onClick(pengujian: TPengujianDetails) {}

        })
        fetchLocalData()

        with(binding){
            rvPengujianDetail.adapter = adapter
            rvPengujianDetail.setHasFixedSize(true)
            rvPengujianDetail.layoutManager = LinearLayoutManager(this@PengujianDetailActivity, LinearLayoutManager.VERTICAL, false)

            srcSerialNumber.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    serNumb = s.toString()
                    doSearch()
                }

            })

            tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                    if (tab?.text == "ALL"){
                        filter = tab?.text.toString()
                        doSearch()
                    }else if (tab?.text == "LOLOS"){
                        filter = tab?.text.toString()
                        doSearch()
                    }else if (tab?.text == "TIDAK LOLOS"){
                        filter = tab?.text.toString()
                        doSearch()
                    }
                }

                override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

                override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

            })
        }
    }

    private fun doSearch() {
        if (serNumb.isNotEmpty()){
            val list = daoSession.tPengujianDetailsDao.queryBuilder().list()
            val listFilter = list.filter { it.serialNumber.toLowerCase().contains(serNumb.toLowerCase()) }
            adapter.setPengujianList(listFilter)
        }else{
            val list = daoSession.tPengujianDetailsDao.queryBuilder().where(TPengujianDetailsDao.Properties.StatusUji.eq(filter)).list()
            adapter.setPengujianList(list)
        }
    }

    private fun fetchLocalData() {
        val list = daoSession.tPengujianDetailsDao.queryBuilder().list()
        adapter.setPengujianList(list)
    }
}