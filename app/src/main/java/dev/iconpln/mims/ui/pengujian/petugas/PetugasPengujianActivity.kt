package dev.iconpln.mims.ui.pengujian.petugas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPetugasPengujian
import dev.iconpln.mims.data.local.database.TPetugasPengujianDao
import dev.iconpln.mims.databinding.ActivityPengujianBinding
import dev.iconpln.mims.databinding.ActivityPetugasPengujianBinding

class PetugasPengujianActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetugasPengujianBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listPetugas: List<TPetugasPengujian>
    private lateinit var adapter: PetugasPengujianAdapter
    private var noPengujian = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetugasPengujianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        noPengujian = intent.getStringExtra("noPengujian")!!

        listPetugas = daoSession.tPetugasPengujianDao.queryBuilder()
            .where(TPetugasPengujianDao.Properties.NoPengujian.eq(noPengujian)).list()

        adapter = PetugasPengujianAdapter(arrayListOf())

        adapter.setPengujianList(listPetugas)

        with(binding){
            btnClose.setOnClickListener { onBackPressed() }
            txtNoPengujian.text = noPengujian

            rvListPenguji.adapter = adapter
            rvListPenguji.setHasFixedSize(true)
            rvListPenguji.layoutManager = LinearLayoutManager(this@PetugasPengujianActivity,LinearLayoutManager.VERTICAL,false)
        }
    }
}