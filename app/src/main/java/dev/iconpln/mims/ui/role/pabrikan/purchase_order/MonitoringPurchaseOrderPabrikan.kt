package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderPabrikanBinding
import dev.iconpln.mims.utils.NoPoSerial
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MonitoringPurchaseOrderPabrikan : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringPurchaseOrderPabrikanBinding
    private val monitoringPOViewModel: MonitoringPOViewModel by viewModels()
    private lateinit var rvAdapter: ListNoPoAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPurchaseOrderPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvAdapter = ListNoPoAdapter()

        binding.apply {
            rvNoPo.layoutManager = LinearLayoutManager(this@MonitoringPurchaseOrderPabrikan)
            rvNoPo.adapter = rvAdapter
        }

        monitoringPOViewModel.getMonitoringPO("", "")

        monitoringPOViewModel.monitoringPOResponse.observe(this) {
            rvAdapter.setData(it.data)
        }

        binding.srcNomorBatch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    val mQuery = query.uppercase(Locale.ROOT)
                    monitoringPOViewModel.getMonitoringPO(mQuery, "")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val mNewText = newText.uppercase(Locale.ROOT)
                    monitoringPOViewModel.getMonitoringPO(mNewText, "")
                }
                return false
            }
        })
    }
}