package dev.iconpln.mims.ui.monitoring.monitoring_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDetail
import dev.iconpln.mims.data.local.database.TPosDetailDao
import dev.iconpln.mims.databinding.ActivityMonitoringBinding
import dev.iconpln.mims.databinding.ActivityMonitoringDetailBinding
import dev.iconpln.mims.ui.monitoring.MonitoringViewModel

class MonitoringDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonitoringDetailBinding
    private val viewModel: MonitoringViewModel by viewModels()
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: MonitoringDetailAdapter
    private lateinit var listDetailMontioring: List<TPosDetail>
    private var noDo: String? = ""
    private var noMat: String? = ""
    private var noPackaging: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        listDetailMontioring = daoSession.tPosDetailDao.queryBuilder().list()

        noDo = intent.getStringExtra("no_do")

        viewModel.getPoByDo(listDetailMontioring, noDo!!)

        adapter = MonitoringDetailAdapter(arrayListOf(), object : MonitoringDetailAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetail) {}

        })

        viewModel.detailMonitoringPOResponse.observe(this){
            adapter.setPoList(it)
        }

        with(binding){
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@MonitoringDetailActivity, LinearLayoutManager.VERTICAL, false)

            srcNomorMaterial.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noMat = s.toString()
                    viewModel.getFileterPoDetail(listDetailMontioring,noMat!!,noPackaging!!)
                }

            })

            srcNomorPackaging.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    noPackaging = s.toString()
                    viewModel.getFileterPoDetail(listDetailMontioring,noMat!!,noPackaging!!)
                }

            })

            btnClose.setOnClickListener { onBackPressed() }
        }

    }
}