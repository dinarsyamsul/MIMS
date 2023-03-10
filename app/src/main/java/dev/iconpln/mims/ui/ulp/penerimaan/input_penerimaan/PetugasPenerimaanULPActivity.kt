package dev.iconpln.mims.ui.ulp.penerimaan.input_penerimaan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlp
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlpDao
import dev.iconpln.mims.databinding.ActivityPetugasPemeriksaanBinding
import dev.iconpln.mims.databinding.ActivityPetugasPenerimaanUlpBinding
import dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan.DetailPemeriksaanActivity
import java.text.SimpleDateFormat
import java.util.*

class PetugasPenerimaanULPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetugasPenerimaanUlpBinding
    private lateinit var daoSession: DaoSession
    private lateinit var penerimaans: TTransPenerimaanUlp
    private var noRepackaging: String = ""
    private var noPengiriman: String = ""
    private lateinit var cal: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetugasPenerimaanUlpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!
        cal = Calendar.getInstance()

        setDatePicker()

        noRepackaging = intent.getStringExtra("noRepackaging")!!
        noPengiriman = intent.getStringExtra("noPengiriman")!!

        penerimaans = daoSession.tTransPenerimaanUlpDao.queryBuilder()
            .where(TTransPenerimaanUlpDao.Properties.NoPengiriman.eq(noPengiriman)).list()[0]

        binding.btnLanjutpenerimaanulp.setOnClickListener {
            validation()
        }

        with(binding){
            btnBack.setOnClickListener { onBackPressed() }
            txtNoPengiriman.text = penerimaans.noPengiriman
            txtGudangTujuan2Penerimaanulp.text = penerimaans.gudangTujuan
            txtGudangTujuanPenerimaanulp.text = penerimaans.gudangAsal
            txtNoRepackagingpenerimaanulp.text = penerimaans.noRepackaging

            edtKepalaGudang.setText(penerimaans.kepalaGudangPenerima)
            edtTanggalDokumen.setText(penerimaans.tanggalDokumen)
            edtKurir.setText(penerimaans.kurir)
            edtNota.setText(penerimaans.noNota)
            edtNoPk.setText(penerimaans.noPk)
            etJabatan.setText(penerimaans.pejabatPenerima)
        }
    }

    private fun validation() {
        with(binding){
            val kepalaGudang = edtKepalaGudang.text.toString()
            val noPk = edtNoPk.text.toString()
            val tglDoc = edtTanggalDokumen.text.toString()
            val pejabatPenerima = etJabatan.text.toString()
            val kurir = edtKurir.text.toString()
            val noNota = edtNota.text.toString()

            if (kepalaGudang.isNullOrEmpty()){
                Toast.makeText(this@PetugasPenerimaanULPActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (noPk.isNullOrEmpty()){
                Toast.makeText(this@PetugasPenerimaanULPActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (tglDoc.isNullOrEmpty()){
                Toast.makeText(this@PetugasPenerimaanULPActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (pejabatPenerima.isNullOrEmpty()){
                Toast.makeText(this@PetugasPenerimaanULPActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (kurir.isNullOrEmpty()){
                Toast.makeText(this@PetugasPenerimaanULPActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (noNota.isNullOrEmpty()){
                Toast.makeText(this@PetugasPenerimaanULPActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            submitForm(kepalaGudang,noPk,tglDoc,pejabatPenerima,kurir,noNota)
        }
    }

    private fun submitForm(
        kepalaGudang: String,
        noPk: String,
        tglDoc: String,
        pejabatPenerima: String,
        kurir: String,
        noNota: String
    ) {
        penerimaans.kepalaGudangPenerima = kepalaGudang
        penerimaans.noPk = noPk
        penerimaans.pejabatPenerima = pejabatPenerima
        penerimaans.tanggalDokumen = tglDoc
        penerimaans.kurir = kurir
        penerimaans.noNota = noNota

        daoSession.tTransPenerimaanUlpDao.update(penerimaans)

        startActivity(
            Intent(this@PetugasPenerimaanULPActivity, DetailPemeriksaanActivity::class.java)
            .putExtra("noRepackaging", noRepackaging)
            .putExtra("noPengiriman", noPengiriman))
    }

    private fun setDatePicker() {
        val dateSetListenerPermintaan = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.edtTanggalDokumen.setText(sdf.format(cal.time))
        }

        binding.edtTanggalDokumen.setOnClickListener {
            DatePickerDialog(this@PetugasPenerimaanULPActivity, dateSetListenerPermintaan,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}