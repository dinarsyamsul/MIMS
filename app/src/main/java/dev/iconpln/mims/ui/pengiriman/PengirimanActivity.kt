package dev.iconpln.mims.ui.pengiriman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosDao
import dev.iconpln.mims.databinding.ActivityPengirimanBinding

class PengirimanActivity : AppCompatActivity() {
    private lateinit var rvAdapter: ListPengirimanAdapter
    private lateinit var binding: ActivityPengirimanBinding
    private lateinit var daoSession: DaoSession
    private var noDo=""
    private var noPo=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!

        rvAdapter = ListPengirimanAdapter(this@PengirimanActivity,arrayListOf(), object: ListPengirimanAdapter.OnAdapterListener {
            override fun onClick(po: TPos) {
                val intent = Intent(this@PengirimanActivity, DetailPengirimanActivity::class.java)
                intent.putExtra(DetailPengirimanActivity.EXTRA_NO_PENGIRIMAN, po.noDoSmar)
                startActivity(intent)
            }
        })

        binding.rvPengiriman.apply {
            adapter = rvAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PengirimanActivity)
        }

        binding.txtPo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                noPo = s.toString()
                fetchDataLocal()
            }
        })

        binding.txtDo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                noDo = s.toString()
                fetchDataLocal()
            }
        })

        binding.btnBack.setOnClickListener { onBackPressed() }

        fetchDataLocal()
    }

    private fun fetchDataLocal() {
        var listDataPengiriman = daoSession.tPosDao.queryBuilder().where(TPosDao.Properties.NoDoSmar.like("%" + noDo + "%"),
            TPosDao.Properties.PoSapNo.like("%" + noPo + "%")).list()
        rvAdapter.setPengirimanList(listDataPengiriman)
    }
}