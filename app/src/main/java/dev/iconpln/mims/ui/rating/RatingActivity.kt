package dev.iconpln.mims.ui.rating

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ActivityRatingBinding
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanViewModel
import dev.iconpln.mims.ui.rating.detail_rating.DetailRatingActivity

class RatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listPenDetail: MutableList<TPosSns>
    private lateinit var penerimaan: TPosPenerimaan
    private lateinit var adapter: RatingAdapter
    private var noDo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noDo = intent.getStringExtra("noDo")!!

        listPenDetail = daoSession.tPosSnsDao.queryBuilder()
            .where(TPosSnsDao.Properties.NoDoSmar.eq(noDo)).list()

        penerimaan = daoSession.tPosPenerimaanDao.queryBuilder()
            .where(TPosPenerimaanDao.Properties.NoDoSmar.eq(noDo)).list().get(0)

        adapter = RatingAdapter(arrayListOf(), object : RatingAdapter.OnAdapterListener{
            override fun onClick(po: TPosSns) {}

        })

        adapter.setPedList(listPenDetail.distinctBy { it.noPackaging })

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            txtIsiEkspedisi.text = penerimaan.expeditions
            txtNoDo.text = penerimaan.noDoSmar
            txtPlant.text = penerimaan.plantName
            txtStoreloc.text = penerimaan.storloc
            txtKuantitasDiterima.text = penerimaan.total
            txtNamaKurir.text = penerimaan.kurirPengantar
            txtPetugasPengiriman.text = penerimaan.kurirPengantar
            txtPrimaryOrder.text = penerimaan.poSapNo
            txtTglDiterima.text = penerimaan.tanggalDiterima.take(10)
            txtTglPengiriman.text = penerimaan.createdDate
            txtTglTerima.text = penerimaan.poDate.take(10)
            txtTlsk.text = penerimaan.tlskNo
            txtUnitAsal.text = penerimaan.plantName
            txtPrimaryOrder.text = if (penerimaan.poMpNo.isNullOrEmpty()) "-" else penerimaan.poMpNo

            if (penerimaan.ratingDone == 1){
                btnRating.setImageResource(R.drawable.ic_rating_done)
            }


            btnRating.setOnClickListener {
                if (penerimaan.ratingDone == 1){
                    Toast.makeText(this@RatingActivity, "Kamu sudah melakukan rating",Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@RatingActivity, DetailRatingActivity::class.java)
                        .putExtra("noDo", noDo))
                }
            }
            srcNoPackaging.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val filter = listPenDetail.filter {
                        it.noPackaging.toLowerCase().contains(s.toString().toLowerCase())
                    }
                    adapter.setPedList(filter.distinctBy { it.noPackaging })
                }

            })

            rvPackaging.adapter = adapter
            rvPackaging.setHasFixedSize(true)
            rvPackaging.layoutManager = LinearLayoutManager(this@RatingActivity, LinearLayoutManager.VERTICAL, false)

            btnBack.setOnClickListener {
                onBackPressed()
            }
//            btnTerima.setOnClickListener {
//                validate()
//            }
        }
        insertDataRating()
    }

    private fun insertDataRating() {
        val dataRating = daoSession.tDataRatingDao.queryBuilder()
            .where(TDataRatingDao.Properties.NoDoSmar.eq(noDo)).list()
        if (dataRating != null){
            val size = dataRating.size
            if (size > 0) {
                val items = arrayOfNulls<TTransDataRating>(size)
                var item: TTransDataRating
                for ((i, model) in dataRating.withIndex()){
                    item = TTransDataRating()
                    item.noDoSmar = model?.noDoSmar
                    item.ratingResponse = model?.ratingResponse
                    item.ratingQuality = model?.ratingQuality
                    item.ratingDelivery = model?.ratingDelivery
                    item.selesaiRating = model?.selesaiRating
                    item.ketepatan = model?.ketepatan
                    item.isDone = 0
                    items[i] = item
                }
                daoSession.tTransDataRatingDao.insertInTx(items.toList())
            }
        }
    }

    override fun onBackPressed() {
       startActivity(Intent(this,PenerimaanActivity::class.java)
           .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

//    private fun validate() {
//        if (penerimaan.ratingDone != 1){
//            Toast.makeText(this@RatingActivity, "Kamu belum melakukan rating", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val dialog = Dialog(this@RatingActivity)
//        dialog.setContentView(R.layout.popup_penerimaan);
//        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(false);
//        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
//        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
//        val message = dialog.findViewById(R.id.textView16) as TextView
//        val txtMessage = dialog.findViewById(R.id.textView22) as TextView
//
//        message.text = "Berhasil"
//        txtMessage.text = "Data berhasil di kirim"
//
//        penerimaan.isDone = 1
//        penerimaan.statusPenerimaan = "DITERIMA"
//        daoSession.tPosPenerimaanDao.update(penerimaan)
//
//        btnOk.setOnClickListener {
//            dialog.dismiss();
//            startActivity(Intent(this@RatingActivity, PenerimaanActivity::class.java )
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//            finish()
//        }
//
//        dialog.show();
//    }
}