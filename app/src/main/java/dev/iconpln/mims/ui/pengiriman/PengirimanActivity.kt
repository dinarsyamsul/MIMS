package dev.iconpln.mims.ui.pengiriman

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
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
    private var kodeStatus=""

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

        val statusArray = arrayOf(
            "ALL","DITERIMA","DITOLAK","ONPROGRESS","DALAM PERJALANAN","DIKIRIM"
        )
        val adapterStatus = ArrayAdapter(this@PengirimanActivity, R.layout.simple_dropdown_item_1line, statusArray)
        binding.dropdownStatus.setAdapter(adapterStatus)
        binding.dropdownStatus.setOnItemClickListener { parent, view, position, id ->
            val status = statusArray[position]
            when (status) {
                "ALL" -> {
                    kodeStatus = ""
                    fetchDataLocal()
                }
                "DITERIMA" -> {
                    kodeStatus = "105"
                    fetchDataLocal()
                }
                "ONPROGRESS" -> {
                    kodeStatus = "103"
                    fetchDataLocal()
                }
                "DALAM PERJALANAN" -> {
                    kodeStatus = "102"
                    fetchDataLocal()
                }
                "DITOLAK" -> {
                    kodeStatus = "104"
                    fetchDataLocal()
                }
                "DIKIRIM" -> {
                    kodeStatus = "100"
                    fetchDataLocal()
                }
            }
        }
        binding.btnBack.setOnClickListener { onBackPressed() }

        fetchDataLocal()
    }

    private fun fetchDataLocal() {
        var listDataPengiriman = daoSession.tPosDao.queryBuilder()
            .where(TPosDao.Properties.NoDoSmar.like("%" + noDo + "%"),
            TPosDao.Properties.PoSapNo.like("%" + noPo + "%"),
            TPosDao.Properties.KodeStatusDoMims.like("%"+kodeStatus+"%")).list()
        binding.rvPengiriman.apply {
            adapter = null
            layoutManager = null

            adapter = rvAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PengirimanActivity)
        }

        rvAdapter.setPengirimanList(listDataPengiriman)
    }
}