package dev.iconpln.mims.ui.monitoring

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.databinding.ActivityMonitoringBinding
import dev.iconpln.mims.ui.monitoring.monitoring_detail.MonitoringDetailActivity
import java.text.SimpleDateFormat
import java.util.*

class MonitoringActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringBinding
    private lateinit var daoSession: DaoSession
    private val viewModel: MonitoringViewModel by viewModels()
    private lateinit var adapter: MonitoringAdapter
    private lateinit var listMonitoring: List<TPos>
    private var noPo: String? = ""
    private var noDo: String? = ""
    private var startDate: String? = ""
    private var endDate: String? = ""
    private lateinit var cal: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        listMonitoring = daoSession.tPosDao.queryBuilder().list()

        cal = Calendar.getInstance()


        adapter = MonitoringAdapter(arrayListOf(), object : MonitoringAdapter.OnAdapterListener{
            override fun onClick(po: TPos) {
                val intent = Intent(this@MonitoringActivity, MonitoringDetailActivity::class.java)
                intent.putExtra("no_do", po.noDoSmar)
                startActivity(intent)
            }

        })

        viewModel.getPo(listMonitoring)

        viewModel.monitoringPOResponse.observe(this){
            adapter.setPoList(it)
        }

        viewModel.isLoading.observe(this){
            when(it){
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }

        with(binding){
            rvNoPo.adapter = adapter
            rvNoPo.setHasFixedSize(true)
            rvNoPo.layoutManager = LinearLayoutManager(this@MonitoringActivity, LinearLayoutManager.VERTICAL, false)

            btnBack.setOnClickListener { onBackPressed() }

            srcNomorPo.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noPo = s.toString()
                    viewModel.getPoFilter(listMonitoring,noPo!!, noDo!!,startDate!!,endDate!!)
                }

            })

            srcNomorDo.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noDo = s.toString()
                    viewModel.getPoFilter(listMonitoring,noPo!!, noDo!!,startDate!!,endDate!!)
                }

            })
        }

        setDatePicker()
    }

    private fun setDatePicker() {
        val dateSetListenerStart = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.txtTglMulai.text = sdf.format(cal.time)
            startDate = sdf.format(cal.time)
            viewModel.getPoFilter(listMonitoring,noPo!!,noDo!!,startDate!!,endDate!!)
        }

        val dateSetListenerEnd = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.txtTglSelesai.text = sdf.format(cal.time)
            endDate = sdf.format(cal.time)
            viewModel.getPoFilter(listMonitoring,noPo!!,noDo!!,startDate!!,endDate!!)
        }

        binding.cvTanggalMulai.setOnClickListener {
            DatePickerDialog(this@MonitoringActivity, dateSetListenerStart,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.cvTanggalSelesai.setOnClickListener {
            DatePickerDialog(this@MonitoringActivity, dateSetListenerEnd,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}