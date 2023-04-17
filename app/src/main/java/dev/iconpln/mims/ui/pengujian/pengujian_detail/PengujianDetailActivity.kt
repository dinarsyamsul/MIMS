package dev.iconpln.mims.ui.role.pabrikan.pengujian.pengujian_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPengujianDetails
import dev.iconpln.mims.data.local.database.TPengujianDetailsDao
import dev.iconpln.mims.data.scan.CustomScanActivity
import dev.iconpln.mims.databinding.ActivityPengujianDetailBinding
import dev.iconpln.mims.ui.pengujian.pengujian_detail.PengujianDetailAdapter

class PengujianDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPengujianDetailBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PengujianDetailAdapter
    private var serNumb: String = ""
    private var filter: String = ""
    private var noPengujian: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengujianDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noPengujian = intent.getStringExtra("noPengujian")!!

        daoSession = (application as MyApplication).daoSession!!

        adapter = PengujianDetailAdapter(arrayListOf(),object: PengujianDetailAdapter.OnAdapterListener{
            override fun onClick(pengujian: TPengujianDetails) {}

        })

        fetchLocalData(noPengujian)

        with(binding){

            btnScan.setOnClickListener { openScanner() }

            btnClose.setOnClickListener {
                onBackPressed()
            }

            rvPengujianDetail.adapter = adapter
            rvPengujianDetail.setHasFixedSize(true)
            rvPengujianDetail.layoutManager = LinearLayoutManager(this@PengujianDetailActivity, LinearLayoutManager.VERTICAL, false)

            srcSerialNumber.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    serNumb = s.toString()
                    doSearch()
                }

            })

            tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                    if (tab?.text == "All"){
                        filter = tab?.text.toString().uppercase()
                        doSearch()
                    }else if (tab?.text == "Lolos"){
                        filter = tab?.text.toString().uppercase()
                        doSearch()
                    }else if (tab?.text == "Tidak Lolos"){
                        filter = tab?.text.toString().uppercase()
                        doSearch()
                    }else if (tab?.text == "Rusak"){
                        filter = tab?.text.toString().uppercase()
                        doSearch()
                    }
                }

                override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

                override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}

            })
        }
    }

    private fun doSearch() {
        if (filter == "ALL"){
            val list = daoSession.tPengujianDetailsDao.queryBuilder()
                .where(TPengujianDetailsDao.Properties.NoPengujian.eq(noPengujian))
                .where(TPengujianDetailsDao.Properties.SerialNumber.like("%"+serNumb+"%")).list()

            adapter.setPengujianList(list)
        }else{
            val list = daoSession.tPengujianDetailsDao.queryBuilder()
                .where(TPengujianDetailsDao.Properties.NoPengujian.eq(noPengujian))
                .where(TPengujianDetailsDao.Properties.SerialNumber.like("%"+serNumb+"%"))
                .where(TPengujianDetailsDao.Properties.StatusUji.eq(filter)).list()

            adapter.setPengujianList(list)
        }

    }

    private fun fetchLocalData(noPengujian: String) {
        val list = daoSession.tPengujianDetailsDao.queryBuilder()
            .where(TPengujianDetailsDao.Properties.NoPengujian.eq(noPengujian)).list()
        adapter.setPengujianList(list)
    }

    private fun openScanner() {
        val scan = ScanOptions()
        scan.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        scan.setCameraId(0)
        scan.setBeepEnabled(true)
        scan.setBarcodeImageEnabled(true)
        scan.captureActivity = CustomScanActivity::class.java
        barcodeLauncher.launch(scan)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        try {
            if(!result.contents.isNullOrEmpty()){
                binding.srcSerialNumber.setText(result.contents)
            }
        }catch (e: Exception){
            Log.e("exception", e.toString())
        }
    }
}