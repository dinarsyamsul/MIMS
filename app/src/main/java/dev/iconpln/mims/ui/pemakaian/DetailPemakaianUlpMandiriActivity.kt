package dev.iconpln.mims.ui.pemakaian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.PemakaianUlpData
import dev.iconpln.mims.data.remote.PemakaianUlpMandiriData
import dev.iconpln.mims.databinding.ActivityDetailPemakaianUlpMandiriBinding

class DetailPemakaianUlpMandiriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPemakaianUlpMandiriBinding
    private var noReservasi: String = ""
    private var listItem: ArrayList<PemakaianUlpMandiriData> = ArrayList()
    private lateinit var adapter: PemakaianUlpAp2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemakaianUlpMandiriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noReservasi = intent.getStringExtra("noReservasi")!!

        adapter = PemakaianUlpAp2Adapter(arrayListOf(), object: PemakaianUlpAp2Adapter.OnAdapterListener{
            override fun onClick(pengujian: PemakaianUlpMandiriData) {}

        })

        initDataDummy()

        with(binding){
            txtNoReservasi.text = noReservasi

            rvPemakaianUlp.adapter = adapter
            rvPemakaianUlp.setHasFixedSize(true)
            rvPemakaianUlp.layoutManager = LinearLayoutManager(this@DetailPemakaianUlpMandiriActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initDataDummy() {
        listItem = ArrayList()
        listItem.add(PemakaianUlpMandiriData("202300900129203","Kwh Meter","Iron Electrical","Unit",
            "5","1002292928762782","5"))
        listItem.add(PemakaianUlpMandiriData("202300900129204","Kwh Meter","Iron Electrical","Unit",
            "3","1002292928762783","3"))
        adapter.setPengujianList(listItem)
    }
}