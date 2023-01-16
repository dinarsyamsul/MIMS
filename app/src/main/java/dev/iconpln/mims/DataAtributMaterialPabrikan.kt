package dev.iconpln.mims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.databinding.ActivityDataAtributMaterialPabrikanBinding
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity

class DataAtributMaterialPabrikan : AppCompatActivity() {
    private lateinit var binding: ActivityDataAtributMaterialPabrikanBinding
    private lateinit var rvSerial: RecyclerView
    private val list = ArrayList<BatchSerial>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAtributMaterialPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

            rvSerial = findViewById(R.id.rv_serial)
            rvSerial.setHasFixedSize(true)

            list.addAll(getListSerial())
            showRecyclerList()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this@DataAtributMaterialPabrikan, DashboardPabrikanActivity::class.java)
            startActivity(intent)
        }
        }

        private fun getListSerial(): ArrayList<BatchSerial>{
            val dataBatch = resources.getStringArray(R.array.data_batch)
            val dataExcel = resources.getStringArray(R.array.data_excel)
            val dataSerial = resources.getStringArray(R.array.data_serial)
            val listSerial = ArrayList<BatchSerial>()
            for(i in dataBatch.indices){
                val serial = BatchSerial(dataBatch[i], dataExcel[i], dataSerial[i])
                listSerial.add(serial)
            }
            return listSerial
        }

        private fun showRecyclerList(){
            rvSerial.layoutManager = LinearLayoutManager(this)
            val listSerialAdapter = ListSerialAdapter(list)
            rvSerial.adapter = listSerialAdapter
        }
    }
