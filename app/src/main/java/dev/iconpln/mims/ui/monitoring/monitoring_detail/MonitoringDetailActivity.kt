package dev.iconpln.mims.ui.monitoring.monitoring_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
        noDo = intent.getStringExtra("no_do")

        listDetailMontioring = daoSession.tPosDetailDao.queryBuilder()
            .where(TPosDetailDao.Properties.NoDoSmar.eq(noDo)).list()

        adapter = MonitoringDetailAdapter(arrayListOf(), object : MonitoringDetailAdapter.OnAdapterListener{
            override fun onClick(po: TPosDetail) {}

        })

        adapter.setPoList(listDetailMontioring)
        Log.d("detail size",listDetailMontioring.size.toString())

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
                    val filter = daoSession.tPosDetailDao.queryBuilder()
                        .where(TPosDetailDao.Properties.NoDoSmar.eq(noDo))
                        .where(TPosDetailDao.Properties.NoMatSap.like("%"+noMat+"%"))
                        .where(TPosDetailDao.Properties.NoPackaging.like("%"+noPackaging+"%")).list()
                    adapter.setPoList(filter)
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
                    val filter = daoSession.tPosDetailDao.queryBuilder()
                        .where(TPosDetailDao.Properties.NoDoSmar.eq(noDo))
                        .where(TPosDetailDao.Properties.NoMatSap.like("%"+noMat+"%"))
                        .where(TPosDetailDao.Properties.NoPackaging.like("%"+noPackaging+"%")).list()
                    adapter.setPoList(filter)
                }

            })

            btnClose.setOnClickListener { onBackPressed() }
        }

    }
}