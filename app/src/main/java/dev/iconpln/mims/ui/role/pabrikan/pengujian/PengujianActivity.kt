package dev.iconpln.mims.ui.role.pabrikan.pengujian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPengujian
import dev.iconpln.mims.databinding.ActivityPengujianBinding
import java.util.*
import kotlin.collections.ArrayList

class PengujianActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPengujianBinding
    private val pengujianViewModel: PengujianViewModel by viewModels()
    private lateinit var adapter: PengujianAdapter
    private lateinit var daoSession: DaoSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengujianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        adapter = PengujianAdapter(arrayListOf(), object : PengujianAdapter.OnAdapterListener{
            override fun onClick(pengujian: TPengujian) {}

        })

        fetchLocal()

        with(binding){
            rvPengujian.adapter = adapter
            rvPengujian.setHasFixedSize(true)
            rvPengujian.layoutManager = LinearLayoutManager(this@PengujianActivity, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun fetchLocal() {
        val listPengujian = daoSession.tPengujianDao.queryBuilder().list()
        adapter.setPengujianList(listPengujian)
    }

}