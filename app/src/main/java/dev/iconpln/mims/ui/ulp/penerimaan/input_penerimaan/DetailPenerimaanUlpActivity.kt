package dev.iconpln.mims.ui.ulp.penerimaan.input_penerimaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanBinding
import dev.iconpln.mims.databinding.ActivityDetailPenerimaanUlpBinding
import dev.iconpln.mims.ui.ulp.penerimaan.PenerimaanUlpActivity
import dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan.DetailPemeriksaanUlpAdapter
import dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan.InputSnPemeriksaanActivity

class DetailPenerimaanUlpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPenerimaanUlpBinding
    private lateinit var daoSession: DaoSession
    private var noRepackaging: String = ""
    private var noPengiriman: String = ""
    private lateinit var detailPemeriksaans: List<TTransPenerimaanDetailUlp>
    private lateinit var penerimaans: TTransPenerimaanUlp
    private lateinit var adapter: DetailPenerimaanUlpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenerimaanUlpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!

        noRepackaging = intent.getStringExtra("noRepackaging")!!
        noPengiriman = intent.getStringExtra("noPengiriman").toString()

        penerimaans = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .where(TTransPenerimaanUlpDao.Properties.NoPengiriman.eq(noPengiriman)).list()[0]

        detailPemeriksaans = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
            .where(TTransPenerimaanDetailUlpDao.Properties.NoRepackaging.eq(noRepackaging)).list()

        adapter = DetailPenerimaanUlpAdapter(arrayListOf(), object : DetailPenerimaanUlpAdapter.OnAdapterListener{
            override fun onClick(po: TTransPenerimaanDetailUlp) {
                startActivity(
                    Intent(this@DetailPenerimaanUlpActivity, SnPenerimaanActivity::class.java)
                    .putExtra("noMaterial", po.noMaterial)
                    .putExtra("noRepackaging", po.noRepackaging)
                    .putExtra("noTransaksi", po.noTransaksi))
            }

        })

        adapter.setPenerimaanList(detailPemeriksaans)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            srcNoSn.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    detailPemeriksaans = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
                        .where(TTransPenerimaanDetailUlpDao.Properties.NoRepackaging.eq(noRepackaging))
                        .where(TTransPenerimaanDetailUlpDao.Properties.NoMaterial.like("%"+s.toString()+"%")).list()
                    adapter.setPenerimaanList(detailPemeriksaans)
                }

            })

            txtNoPengiriman.text = penerimaans.noPengiriman
            txtNoPenerimaan.text = "PENERIMAAN01"
            txtTanggalPemeriksaan.text = penerimaans.tanggalPemeriksaan
            txtTanggalPenerimaan.text = penerimaans.noPermintaan
            txtTanggalPemeriksaan.text = penerimaans.tanggalPemeriksaan

            rvUlpPenerimaan.adapter = adapter
            rvUlpPenerimaan.layoutManager = LinearLayoutManager(this@DetailPenerimaanUlpActivity, LinearLayoutManager.VERTICAL, false)
            rvUlpPenerimaan.setHasFixedSize(true)

            btnLanjutpenerimaanulp.setOnClickListener {
                penerimaans.tempStatusPenerimaan = "SUDAH DITERIMA"
                penerimaans.isDone = 1
                daoSession.tTransPenerimaanUlpDao.update(penerimaans)

                Toast.makeText(this@DetailPenerimaanUlpActivity, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@DetailPenerimaanUlpActivity, PenerimaanUlpActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }
}