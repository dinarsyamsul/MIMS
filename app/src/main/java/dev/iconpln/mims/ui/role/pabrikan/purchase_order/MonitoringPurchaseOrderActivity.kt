package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dev.iconpln.mims.ListTanggalAdapter
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.TanggalFilter
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosDao
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderPabrikanBinding
import dev.iconpln.mims.ui.role.pabrikan.purchase_order.detail_purchase_order.DetailPurchaseOrderActivity
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList

class MonitoringPurchaseOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringPurchaseOrderPabrikanBinding
    private val monitoringPOViewModel: MonitoringPOViewModel by viewModels()
    private lateinit var adapterPo : MonitoringPurchaseOrderAdapter
    private lateinit var daoSession: DaoSession
    private var noPo: String = ""
    private var noDo: String = ""
    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringPurchaseOrderPabrikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daoSession = (application as MyApplication).daoSession!!
        adapterPo = MonitoringPurchaseOrderAdapter(arrayListOf(), object: MonitoringPurchaseOrderAdapter.OnAdapterListener{
            override fun onClick(po: TPos) {
                startActivity(Intent(this@MonitoringPurchaseOrderActivity, DetailPurchaseOrderActivity::class.java))
            }

        })

        fetchDataLocal()

        with(binding){
            rvNoPo.adapter = adapterPo
            rvNoPo.setHasFixedSize(true)
            rvNoPo.layoutManager = LinearLayoutManager(this@MonitoringPurchaseOrderActivity, LinearLayoutManager.VERTICAL, false)
        }

        searching()
    }

    private fun searching() {
        with(binding){
            srcNomorPo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noPo = s.toString()
                    Toast.makeText(this@MonitoringPurchaseOrderActivity, noPo, Toast.LENGTH_SHORT).show()
                    doSearch()
                }

            })

            srcNomorDo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noDo = s.toString()
                    Toast.makeText(this@MonitoringPurchaseOrderActivity, noDo, Toast.LENGTH_SHORT).show()
                    doSearch()
                }

            })

            cvTanggalMulai.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(this@MonitoringPurchaseOrderActivity, { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    txtTglMulai.text = "${year}-${monthOfYear}-${dayOfMonth}"
                    startDate = "${year}-${monthOfYear + 1}-${dayOfMonth}"

                }, year, month, day)

                dpd.show()
            }

            cvTanggalSelesai.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(this@MonitoringPurchaseOrderActivity, { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    txtTglSelesai.text = "${year}-${monthOfYear}-${dayOfMonth}"
                    endDate = "${year}-${monthOfYear + 1}-${dayOfMonth}"

                }, year, month, day)

                dpd.show()
            }
        }
    }

    private fun doSearch() {
        val listDataPao = daoSession.tPosDao.queryBuilder().list()
//        val filterList = listDataPao.filter { it.createdDate > startDate && it.createdDate < endDate }

        if (noDo.isNullOrEmpty()){
            val filterListPo = listDataPao.filter { it.poMpNo.toLowerCase().contains(noPo.toLowerCase()) }
            adapterPo.setPoList(filterListPo)
        }else if (noPo.isNullOrEmpty()){
            val filterList = listDataPao.filter { it.noDoSmar.toLowerCase().contains(noDo.toLowerCase()) }
            adapterPo.setPoList(filterList)
        }else if (!noPo.isNullOrEmpty() && !noDo.isNullOrEmpty()){
            val filterList = listDataPao.filter {
                it.noDoSmar.toLowerCase().contains(noDo.toLowerCase()) && it.poMpNo.toLowerCase().contains(noPo.toLowerCase())
            }
            adapterPo.setPoList(filterList)
        }else{
            fetchDataLocal()
        }
    }

    private fun fetchDataLocal() {
        val listDataPo = daoSession.tPosDao.queryBuilder().list()
        adapterPo.setPoList(listDataPo)
    }
}