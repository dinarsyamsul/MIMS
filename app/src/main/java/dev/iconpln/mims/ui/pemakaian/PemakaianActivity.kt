package dev.iconpln.mims.ui.pemakaian

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemakaian
import dev.iconpln.mims.data.local.database.TPemakaianDao
import dev.iconpln.mims.data.local.database.TPemakaianDetail
import dev.iconpln.mims.data.local.database.TPemakaianDetailDao
import dev.iconpln.mims.data.local.database.TTransPemakaianDetail
import dev.iconpln.mims.data.local.database.TTransPemakaianDetailDao
import dev.iconpln.mims.databinding.ActivityPemakaianBinding
import java.text.SimpleDateFormat
import java.util.*

class PemakaianActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemakaianBinding
    private lateinit var adapter: PemakaianAdapter
    private lateinit var pemakaians: List<TPemakaian>
    private lateinit var daoSession: DaoSession
    private var noReservasi = ""
    private var tglReservasi = ""
    private lateinit var cal: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemakaianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()

        pemakaians = daoSession.tPemakaianDao.loadAll()

        adapter = PemakaianAdapter(arrayListOf(), object : PemakaianAdapter.OnAdapterListener{
            override fun onClick(pemakaian: TPemakaian) {
                insertDataDetail(pemakaian.noTransaksi)
                startActivity(Intent(this@PemakaianActivity, DetailPemakaianUlpYantekActivity::class.java)
                    .putExtra("noTransaksi", pemakaian.noTransaksi))
            }

        })

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            rvPemakaianUlp.adapter = adapter
            rvPemakaianUlp.layoutManager = LinearLayoutManager(this@PemakaianActivity, LinearLayoutManager.VERTICAL, false)
            rvPemakaianUlp.setHasFixedSize(true)

            srcNomorReservasi.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    noReservasi = s.toString()
                    doSearch()
                }

            })

            val dateSetListenerStart = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                var myFormat = "yyyy-MM-dd" // mention the format you need
                var sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edtTanggalDiterima.setText(sdf.format(cal.time))
                tglReservasi = sdf.format(cal.time)
                doSearch()
            }

            edtTanggalDiterima.setOnClickListener{
                DatePickerDialog(this@PemakaianActivity, dateSetListenerStart,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()            }
        }

        adapter.setpemakaianList(pemakaians)
    }

    private fun doSearch() {
        val searchList = daoSession.tPemakaianDao.queryBuilder()
            .where(TPemakaianDao.Properties.TanggalReservasi.like("%"+tglReservasi+"%"))
            .where(TPemakaianDao.Properties.NoReservasi.like("%"+noReservasi+"%")).list()

        adapter.setpemakaianList(searchList)
    }

    private fun insertDataDetail(noTransaksi: String) {
        val checkDataExist = daoSession.tTransPemakaianDetailDao.queryBuilder()
            .where(TTransPemakaianDetailDao.Properties.NoTransaksi.eq(noTransaksi)).list().size > 0

        val dataDetail = daoSession.tPemakaianDetailDao.queryBuilder()
            .where(TPemakaianDetailDao.Properties.NoTransaksi.eq(noTransaksi)).list()

        if (!checkDataExist){
            for (i in dataDetail){
                val item = TTransPemakaianDetail()
                item.valuationType = i.valuationType
                item.unit = i.unit
                item.nomorMaterial = i.nomorMaterial
                item.keterangan = i.keterangan
                item.namaMaterial = i.namaMaterial
                item.noMeter = i.noMeter
                item.noTransaksi = i.noTransaksi
                item.qtyPemakaian = i.qtyPemakaian
                item.qtyPengeluaran = i.qtyPengeluaran
                item.qtyReservasi = i.qtyReservasi
                item.snScanned = ""
                item.isDone = 0
                daoSession.tTransPemakaianDetailDao.insert(item)
            }
        }
    }
}