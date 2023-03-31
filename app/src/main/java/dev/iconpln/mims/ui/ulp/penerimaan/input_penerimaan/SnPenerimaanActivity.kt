package dev.iconpln.mims.ui.ulp.penerimaan.input_penerimaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanUlpBinding
import dev.iconpln.mims.databinding.ActivitySnPenerimaanBinding
import dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan.PemeriksaanUlpSnAdapter

class SnPenerimaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnPenerimaanBinding
    private lateinit var daoSession: DaoSession
    private var noMaterial: String = ""
    private var noRepackaging: String = ""
    private var noTransaksi: String = ""
    private lateinit var adapter: PenerimaanUlpSnAdapter
    private lateinit var lisSn : List<TListSnMaterialPenerimaanUlp>
    private lateinit var details: TTransPenerimaanDetailUlp
    private lateinit var penerimaan: TTransPenerimaanUlp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnPenerimaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noMaterial = intent.getStringExtra("noMaterial")!!
        noRepackaging = intent.getStringExtra("noRepackaging")!!
        noTransaksi = intent.getStringExtra("noTransaksi")!!

        lisSn = daoSession.tListSnMaterialPenerimaanUlpDao.queryBuilder()
            .where(TListSnMaterialPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging))
            .where(TListSnMaterialPenerimaanUlpDao.Properties.NoMaterial.eq(noMaterial)).list()

        details = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
            .where(TTransPenerimaanDetailUlpDao.Properties.NoTransaksi.eq(noTransaksi)).list().get(0)

        penerimaan = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .where(TTransPenerimaanUlpDao.Properties.NoRepackaging.eq(noRepackaging)).list().get(0)

        adapter = PenerimaanUlpSnAdapter(arrayListOf(), object : PenerimaanUlpSnAdapter.OnAdapterListener{
            override fun onClick(tms: TListSnMaterialPenerimaanUlp) {}

        })

        with(binding){
            txtGudangAsal.text = "PLN Area Cilacap"
            txtNoMaterial.text = noMaterial
            txtSpesifikasi.text = details.materialDesc

            rvListSn.adapter = adapter
            rvListSn.layoutManager = LinearLayoutManager(this@SnPenerimaanActivity,
                LinearLayoutManager.VERTICAL,false)
            rvListSn.setHasFixedSize(true)

            btnBack.setOnClickListener { onBackPressed() }
            btnSimpan.setOnClickListener {
                Toast.makeText(this@SnPenerimaanActivity, "Sn Berhasil Disimpan",Toast.LENGTH_SHORT)
                onBackPressed()
            }
        }

        adapter.setTmsList(lisSn)
    }
}