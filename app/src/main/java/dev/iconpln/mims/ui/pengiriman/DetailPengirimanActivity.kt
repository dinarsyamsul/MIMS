package dev.iconpln.mims.ui.pengiriman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDao
import dev.iconpln.mims.data.local.database.TPosSns
import dev.iconpln.mims.databinding.ActivityDetailPengirimanBinding

class DetailPengirimanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPengirimanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var rvAdapter: ListDetailPengirimanAdapter

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
//                txtIsiTglpengiriman.text = data.
//                txtIsiPetugasPengiriman.text = data.
//                txtIsiEkspedisi.text = data.
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

        fetchDataLocal()
    }

    private fun fetchDataLocal() {
        val listDataDetailPengiriman = daoSession.tPosSnsDao.queryBuilder().list()
        rvAdapter.setDetailPengirimanList(listDataDetailPengiriman)
    }

    companion object{
        const val EXTRA_NO_PENGIRIMAN = "extra_no_pengiriman"
    }
}