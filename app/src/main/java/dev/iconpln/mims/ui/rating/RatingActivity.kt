package dev.iconpln.mims.ui.rating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDao
import dev.iconpln.mims.databinding.ActivityRatingBinding
import dev.iconpln.mims.ui.pnerimaan.PenerimaanViewModel
import dev.iconpln.mims.ui.rating.detail_rating.DetailRatingActivity

class RatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: RatingAdapter
    private val viewModel: RatingViewModel by viewModels()
    private lateinit var listRating: List<TPemeriksaan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        listRating = daoSession.tPemeriksaanDao.queryBuilder().where(TPemeriksaanDao.Properties.State.eq(2)).list()

        adapter = RatingAdapter(arrayListOf(), object : RatingAdapter.OnAdapterListener{
            override fun onClick(po: TPemeriksaan) {
                startActivity(Intent(this@RatingActivity, DetailRatingActivity::class.java)
                    .putExtra("noDo", po.noDoSmar))
            }

        })

        adapter.setRatingList(listRating)

        with(binding){
            rvRating.adapter = adapter
            rvRating.setHasFixedSize(true)
            rvRating.layoutManager = LinearLayoutManager(this@RatingActivity, LinearLayoutManager.VERTICAL, false)
        }

    }
}