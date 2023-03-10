package dev.iconpln.mims.ui.ulp.penerimaan.input_pemeriksaan

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ActivityPetugasPemeriksaanBinding
import dev.iconpln.mims.ui.monitoring_permintaan.monitoring_permintaan_detail.MonitoringPermintaanDetailActivity
import dev.iconpln.mims.ui.ulp.penerimaan.PenerimaanUlpActivity
import dev.iconpln.mims.ui.ulp.penerimaan.input_penerimaan.PetugasPenerimaanULPActivity

class PetugasPemeriksaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetugasPemeriksaanBinding
    private lateinit var daoSession: DaoSession
    private lateinit var penerimaans: TTransPenerimaanUlp
    private var noRepackaging: String = ""
    private var noPengiriman: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetugasPemeriksaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

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
            txtGudangAsal.text = penerimaans.gudangAsal
            txtGudangTujuan.text = penerimaans.gudangTujuan
            txtNoPermintaan.text = penerimaans.noPermintaan
            txtNoRepackaging.text = penerimaans.noRepackaging

            edtKepalaGudang.setText(if (penerimaans.kepalaGudangPemeriksa.isNullOrEmpty()) "" else penerimaans.kepalaGudangPemeriksa)
            edtPetugasPemeriksa.setText(if (penerimaans.namaPetugasPemeriksa.isNullOrEmpty()) "" else penerimaans.namaPetugasPemeriksa)
            edtNoPk.setText(if (penerimaans.noPk.isNullOrEmpty()) "" else penerimaans.noPk)
            edtPejabatPemeriksa.setText(if (penerimaans.pejabatPemeriksa.isNullOrEmpty()) "" else penerimaans.pejabatPemeriksa)
            edtJabatanPetugasPemeriksa.setText(if (penerimaans.jabatanPetugasPemeriksa.isNullOrEmpty()) "" else penerimaans.jabatanPetugasPemeriksa)
            etJabatan.setText(if (penerimaans.jabatanPemeriksa.isNullOrEmpty()) "" else penerimaans.jabatanPemeriksa)
        }
    }

    private fun validation() {
        with(binding){
            val kepalaGudang = binding.edtKepalaGudang.text.toString()
            val noPk = binding.edtNoPk.text.toString()
            val pejabatPemeriksa = binding.edtPejabatPemeriksa.text.toString()
            val jabatan = binding.etJabatan.text.toString()
            val petugasPemeriksa = binding.edtPetugasPemeriksa.text.toString()
            val jabatanPetugas = binding.edtJabatanPetugasPemeriksa.text.toString()

            if (kepalaGudang.isNullOrEmpty()){
                Toast.makeText(this@PetugasPemeriksaanActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (noPk.isNullOrEmpty()){
                Toast.makeText(this@PetugasPemeriksaanActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (pejabatPemeriksa.isNullOrEmpty()){
                Toast.makeText(this@PetugasPemeriksaanActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (jabatan.isNullOrEmpty()){
                Toast.makeText(this@PetugasPemeriksaanActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (petugasPemeriksa.isNullOrEmpty()){
                Toast.makeText(this@PetugasPemeriksaanActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (jabatanPetugas.isNullOrEmpty()){
                Toast.makeText(this@PetugasPemeriksaanActivity, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
                return
            }

            submitForm(kepalaGudang,noPk,pejabatPemeriksa,jabatan,petugasPemeriksa,jabatanPetugas)

        }
    }

    private fun submitForm(
        kepalaGudang: String,
        noPk: String,
        pejabatPemeriksa: String,
        jabatan: String,
        petugasPemeriksa: String,
        jabatanPetugas: String
    ) {
        penerimaans.kepalaGudangPemeriksa = kepalaGudang
        penerimaans.noPk = noPk
        penerimaans.pejabatPemeriksa = pejabatPemeriksa
        penerimaans.jabatanPemeriksa = jabatan
        penerimaans.namaPetugasPemeriksa = petugasPemeriksa
        penerimaans.jabatanPetugasPemeriksa = jabatanPetugas

        daoSession.tTransPenerimaanUlpDao.update(penerimaans)

        startActivity(Intent(this@PetugasPemeriksaanActivity, PetugasPenerimaanULPActivity::class.java)
            .putExtra("noRepackaging", noRepackaging)
            .putExtra("noPengiriman", noPengiriman))
    }

    override fun onBackPressed() {
        val dialog = Dialog(this@PetugasPemeriksaanActivity)
        dialog.setContentView(R.layout.popup_validation)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown
        val icon = dialog.findViewById(R.id.imageView11) as ImageView
        val btnOk = dialog.findViewById(R.id.btn_ya) as AppCompatButton
        val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
        val message = dialog.findViewById(R.id.message) as TextView
        val txtMessage = dialog.findViewById(R.id.txt_message) as TextView

        icon.setImageResource(R.drawable.ic_popup_delete)
        message.text = "Keluar"
        txtMessage.text = "Jika keluar maka data tidak disimpan"


        btnTidak.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            dialog.dismiss();
            startActivity(
                Intent(this@PetugasPemeriksaanActivity, PenerimaanUlpActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        dialog.show();
    }
}