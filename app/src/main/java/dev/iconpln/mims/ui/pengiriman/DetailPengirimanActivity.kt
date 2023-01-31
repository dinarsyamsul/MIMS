package dev.iconpln.mims.ui.pengiriman

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDao
import dev.iconpln.mims.data.local.database.TPosSns
import dev.iconpln.mims.data.local.database.TPosSnsDao
import dev.iconpln.mims.databinding.ActivityDetailPengirimanBinding

class DetailPengirimanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPengirimanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var rvAdapter: ListDetailPengirimanAdapter
    private var noSn=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!

        val extras = intent.extras

        val noPengiriman = extras?.getString(EXTRA_NO_PENGIRIMAN)

        val getByNoPengiriman = daoSession.tPosDao.queryBuilder().where(TPosDao.Properties.NoDoSmar.eq(noPengiriman)).list()
        binding.apply {
            getByNoPengiriman.forEach { data ->
                txtIsiNopo.text = data.poSapNo
                txtIsiNotlsk.text = data.tlskNo
                txtIsiUnit.text = data.plantName
                txtIsiPlant.text = data.planCodeNo
                txtStorelocpengiriman.text = data.storLoc
                txtIsiDo.text = data.noDoSmar
                txtIsiTglpengiriman.text = data.createdDate
                txtIsiPetugasPengiriman.text = data.courierPersonName
                txtIsiEkspedisi.text = data.expeditions
            }
        }

        rvAdapter = ListDetailPengirimanAdapter(arrayListOf(), object: ListDetailPengirimanAdapter.OnAdapterListener{
            override fun onClick(po: TPosSns) {}
        })

        binding.rvDetailMaterial.apply {
            adapter = rvAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailPengirimanActivity)
        }
        binding.srcSnMaterial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                noSn = s.toString()
                fetchDataLocal()
            }
        })

        binding.btnClose.setOnClickListener { finish() }

        fetchDataLocal()
    }

    private fun fetchDataLocal() {
        var listDetailPengiriman = daoSession.tPosSnsDao.queryBuilder().where(TPosSnsDao.Properties.NoSerial.like("%" + noSn + "%")).list()
        rvAdapter.setDetailPengirimanList(listDetailPengiriman)
    }

    companion object{
        const val EXTRA_NO_PENGIRIMAN = "extra_no_pengiriman"
    }
}