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
import dev.iconpln.mims.ui.pnerimaan.PenerimaanViewModel
import dev.iconpln.mims.ui.rating.detail_rating.DetailRatingActivity

class RatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    private lateinit var daoSession: DaoSession
    private lateinit var listPemDetail: MutableList<TPemeriksaanDetail>
    private lateinit var pemDetail: TPemeriksaan
    private lateinit var adapter: RatingAdapter
    private var noPem: String = ""
    private var noDo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        noPem = intent.getStringExtra("noPemeriksaan")!!
        noDo = intent.getStringExtra("noDo")!!
        listPemDetail = daoSession.tPemeriksaanDetailDao.queryBuilder()
            .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem)).list()
        pemDetail = daoSession.tPemeriksaanDao.queryBuilder()
            .where(TPemeriksaanDao.Properties.NoDoSmar.eq(noDo)).limit(1).unique()

        adapter = RatingAdapter(arrayListOf(), object : RatingAdapter.OnAdapterListener{
            override fun onClick(po: TPemeriksaanDetail) {}

        })

        adapter.setPedList(listPemDetail)

        with(binding){
            txtIsiEkspedisi.text = pemDetail.namaEkspedisi
            txtNoDo.text = pemDetail.noDoSmar
            txtPlant.text = pemDetail.plantName
            txtStoreloc.text = pemDetail.storLoc
            txtKuantitasDiterima.text = pemDetail.total
            txtNamaKurir.text = pemDetail.namaKurir
            txtPetugasPengiriman.text = pemDetail.namaKurir
            txtPrimaryOrder.text = pemDetail.poSapNo
            txtTglDiterima.text = pemDetail.tanggalDiterima
            txtTglPengiriman.text = pemDetail.createdDate
            txtTglTerima.text = pemDetail.tanggalDiterima
            txtTlsk.text = pemDetail.tlskNo
            txtUnitAsal.text = pemDetail.plantName
            txtVendor.text = "-"

            srcNoPackaging.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val filter = listPemDetail.filter {
                        it.noPackaging.toLowerCase().contains(s.toString().toLowerCase())
                    }
                    adapter.setPedList(filter)
                }

            })

            rvPackaging.adapter = adapter
            rvPackaging.setHasFixedSize(true)
            rvPackaging.layoutManager = LinearLayoutManager(this@RatingActivity, LinearLayoutManager.VERTICAL, false)

            btnTerima.setOnClickListener {
                validate()
            }

            btnRating.setOnClickListener {
                startActivity(Intent(this@RatingActivity, DetailRatingActivity::class.java)
                    .putExtra("noDo", noDo)
                    .putExtra("noPem", noPem))
            }
        }

    }

    private fun validate() {
        if (pemDetail.isDone != 1){
            Toast.makeText(this@RatingActivity, "Kamu belum melakukan rating", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = Dialog(this@RatingActivity)
        dialog.setContentView(R.layout.popup_penerimaan);
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
        val btnOk = dialog.findViewById(R.id.btn_ok) as AppCompatButton
        val message = dialog.findViewById(R.id.textView16) as TextView
        val txtMessage = dialog.findViewById(R.id.textView22) as TextView

        message.text = "Berhasil"
        txtMessage.text = "Data berhasil di kirim"

        daoSession.tPemeriksaanDao.update(pemDetail)

        btnOk.setOnClickListener {
            dialog.dismiss();
            startActivity(Intent(this@RatingActivity, PemeriksaanActivity::class.java ))
            finish()
        }

        dialog.show();
    }
}