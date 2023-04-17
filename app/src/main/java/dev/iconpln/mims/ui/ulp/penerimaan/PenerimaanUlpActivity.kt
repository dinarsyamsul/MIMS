package dev.iconpln.mims.ui.ulp.penerimaan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ActivityPenerimaanUlpBinding
import dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan.PetugasPemeriksaanActivity
import dev.iconpln.mims.ui.ulp.penerimaan.input_penerimaan.PetugasPenerimaanULPActivity
import java.text.SimpleDateFormat
import java.util.*

class PenerimaanUlpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenerimaanUlpBinding
    private lateinit var daoSession: DaoSession
    private lateinit var adapter: PenerimaanULPAdapter
    private lateinit var penerimaanUlps: List<TTransPenerimaanUlp>
    private lateinit var cal: Calendar
    private var deliveryDate = ""
    private var srcNoPengiriman = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanUlpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()

        insertData()
        setDatePicker()
        setSrcNoPengiriman()

        penerimaanUlps = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .whereOr(TTransPenerimaanUlpDao.Properties.StatusPenerimaan.notEq("DITERIMA"),
                TTransPenerimaanUlpDao.Properties.StatusPemeriksaan.notEq("SUDAH DIPERIKSA")).list()


        adapter = PenerimaanULPAdapter(arrayListOf(), object : PenerimaanULPAdapter.OnAdapterListener{
            override fun onClick(pengujian: TTransPenerimaanUlp) {
                insertDataDetail(pengujian.noRepackaging)
                if (pengujian.isDonePemeriksaan == 1){
                    Toast.makeText(this@PenerimaanUlpActivity, "Anda sudah menyelesaikan pengiriman ini", Toast.LENGTH_SHORT).show()
                }else if(pengujian.statusPemeriksaan == "SUDAH DIPERIKSA" && pengujian.statusPenerimaan == "DITERIMA"){
                    Toast.makeText(this@PenerimaanUlpActivity, "Pengiriman ini sudah di selesaikan", Toast.LENGTH_SHORT).show()
                } else{
                    startActivity(Intent(this@PenerimaanUlpActivity, PetugasPemeriksaanActivity::class.java)
                        .putExtra("noRepackaging", pengujian.noRepackaging)
                        .putExtra("noPengiriman", pengujian.noPengiriman))
                }
            }

        })

        adapter.setPenerimaanList(penerimaanUlps)

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            rvPenerimaanULP.adapter = adapter
            rvPenerimaanULP.setHasFixedSize(true)
            rvPenerimaanULP.layoutManager = LinearLayoutManager(this@PenerimaanUlpActivity, LinearLayoutManager.VERTICAL, false)
        }


    }

    private fun setSrcNoPengiriman() {
        binding.srcNoPengirimanULP.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                srcNoPengiriman = s.toString()
                val searchList = daoSession.tTransPenerimaanUlpDao.queryBuilder()
                    .where(TTransPenerimaanUlpDao.Properties.DeliveryDate.like("%"+deliveryDate+"%"))
                    .where(TTransPenerimaanUlpDao.Properties.NoPengiriman.like("%"+srcNoPengiriman+"%")).list()

                adapter.setPenerimaanList(searchList)
            }

        })
    }

    private fun setDatePicker() {
        val dateSetListenerPermintaan = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            deliveryDate = sdf.format(cal.time)
            binding.edtTanggalDiterimaULP.setText(deliveryDate)

            val searchList = daoSession.tTransPenerimaanUlpDao.queryBuilder()
                .where(TTransPenerimaanUlpDao.Properties.DeliveryDate.like("%"+deliveryDate+"%"))
                .where(TTransPenerimaanUlpDao.Properties.NoPengiriman.like("%"+srcNoPengiriman+"%")).list()

            adapter.setPenerimaanList(searchList)
        }

        binding.edtTanggalDiterimaULP.setOnClickListener {
            DatePickerDialog(this@PenerimaanUlpActivity, dateSetListenerPermintaan,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun insertDataDetail(noRepackaging: String) {
        val transPenerimaanDetails = daoSession.tTransPenerimaanDetailUlpDao.queryBuilder()
            .where(TTransPenerimaanDetailUlpDao.Properties.NoRepackaging.eq(noRepackaging)).list()

        val penerimaanDetails = daoSession.tPenerimaanDetailUlpDao.queryBuilder()
            .where(TPenerimaanDetailUlpDao.Properties.NoRepackaging.eq(noRepackaging)).list()

        if (transPenerimaanDetails.isEmpty()){
            val size = penerimaanDetails.size
            if (size > 0) {
                val items = arrayOfNulls<TTransPenerimaanDetailUlp>(size)
                var item: TTransPenerimaanDetailUlp
                for ((i, model) in penerimaanDetails.withIndex()){
                    item = TTransPenerimaanDetailUlp()
                    item.noRepackaging = model?.noRepackaging
                    item.noTransaksi = model?.noTransaksi
                    item.qtyPenerimaan = model?.qtyPenerimaan
                    item.materialDesc = model?.materialDesc
                    item.noMaterial = model?.noMaterial
                    item.qtyPemeriksaan = model?.qtyPemeriksaan
                    item.qtyPengiriman = model?.qtyPengiriman
                    item.qtyPermintaan = model?.qtyPermintaan
                    item.qtySesuai = model?.qtySesuai
                    item.isDone = 0
                    items[i] = item
                }
                daoSession.tTransPenerimaanDetailUlpDao.insertInTx(items.toList())
            }
        }
    }

    private fun insertData() {
        val transPenerimaanUlp = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .where(TTransPenerimaanUlpDao.Properties.StatusPemeriksaan.notEq("SUDAH DIPERIKSA"))
            .where(TTransPenerimaanUlpDao.Properties.StatusPenerimaan.notEq("DITERIMA"))
            .list()

        val penerimaanUlps = daoSession.tPenerimaanUlpDao.queryBuilder()
            .where(TPenerimaanUlpDao.Properties.StatusPemeriksaan.notEq("SUDAH DIPERIKSA"))
            .where(TPenerimaanUlpDao.Properties.StatusPenerimaan.notEq("DITERIMA"))
            .list()

        if (transPenerimaanUlp.isEmpty()){
            val size = penerimaanUlps.size
            if (size > 0) {
                val items = arrayOfNulls<TTransPenerimaanUlp>(size)
                var item: TTransPenerimaanUlp
                for ((i, model) in penerimaanUlps.withIndex()){
                    item = TTransPenerimaanUlp()
                    item.noPengiriman = model?.noPengiriman
                    item.noPermintaan = model?.noPermintaan
                    item.statusPemeriksaan = model?.statusPemeriksaan
                    item.deliveryDate = model?.deliveryDate
                    item.statusPenerimaan = model?.statusPenerimaan
                    item.jumlahKardus = model?.jumlahKardus
                    item.gudangAsal = model?.gudangAsal
                    item.noRepackaging = model?.noRepackaging
                    item.gudangTujuan = model?.gudangTujuan
                    item.tanggalPemeriksaan = model?.tanggalPemeriksaan
                    item.tanggalPenerimaan = model?.tanggalPenerimaan
                    item.kepalaGudangPemeriksa = model?.kepalaGudangPemeriksa
                    item.pejabatPemeriksa = model?.pejabatPemeriksa
                    item.jabatanPemeriksa = model?.jabatanPemeriksa
                    item.namaPetugasPemeriksa = model?.namaPetugasPemeriksa
                    item.jabatanPetugasPemeriksa = model?.jabatanPetugasPemeriksa
                    item.kepalaGudangPenerima = model?.kepalaGudangPenerima
                    item.noPk = model?.noPk
                    item.tanggalDokumen = model?.tanggalDokumen
                    item.pejabatPenerima = model?.pejabatPenerima
                    item.kurir = model?.kurir
                    item.noNota = model?.noNota
                    item.noMaterial = model?.noMaterial
                    item.spesifikasi = model?.spesifikasi
                    item.kuantitasPeriksa = model?.kuantitasPeriksa
                    item.kuantitas = model?.kuantitas
                    item.tempStatusPenerimaan = model.statusPenerimaan
                    item.tempStatusPemeriksaan = model.statusPemeriksaan
                    item.isDone = 0
                    item.isDonePemeriksaan = 0
                    items[i] = item
                }
                daoSession.tTransPenerimaanUlpDao.insertInTx(items.toList())
            }
        }
    }
}