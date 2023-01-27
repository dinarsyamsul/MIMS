package dev.iconpln.mims.ui.role.up3.pemeriksaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ActivityPemeriksaanBinding
import dev.iconpln.mims.ui.role.up3.pnerimaan.PenerimaanAdapter

class PemeriksaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanBinding
    private lateinit var adapter: PenerimaanAdapter
    private lateinit var daoSession: DaoSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        adapter = PenerimaanAdapter(arrayListOf(), object: PenerimaanAdapter.OnAdapterListener{
            override fun onClick(po: TPosPenerimaan) {}

        })

        fetchLocal()

        with(binding){
            rvPemeriksaan.adapter = adapter
            rvPemeriksaan.setHasFixedSize(true)
            rvPemeriksaan.layoutManager = LinearLayoutManager(this@PemeriksaanActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchLocal() {
        val list = daoSession.tPosPenerimaanDao.queryBuilder().list()
        adapter.setPoList(list)
    }
}