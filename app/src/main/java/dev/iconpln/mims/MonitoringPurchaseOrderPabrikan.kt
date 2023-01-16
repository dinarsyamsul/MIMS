package dev.iconpln.mims

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.utils.NoPoSerial

class MonitoringPurchaseOrderPabrikan : AppCompatActivity() {

    private lateinit var rvNoPo: RecyclerView
    private val list = ArrayList<NoPoSerial>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring_purchase_order_pabrikan)

        rvNoPo = findViewById(R.id.rv_noPo)
        rvNoPo.setHasFixedSize(true)

        list.addAll(getListNoPo())
        showRecyclerList()
    }

    private fun getListNoPo(): ArrayList<NoPoSerial>{
        val dataNoPo = resources.getStringArray(R.array.data_Po)
        val listPo = ArrayList<NoPoSerial>()
        for(i in dataNoPo.indices){
            val serialPo = NoPoSerial(dataNoPo[i])
            listPo.add(serialPo)
        }
        return listPo
    }

    private fun showRecyclerList(){
        rvNoPo.layoutManager = LinearLayoutManager(this)
        val listNoPoAdapter = ListNoPoAdapter(list)
        rvNoPo.adapter = listNoPoAdapter
    }
}