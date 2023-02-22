package dev.iconpln.mims.ui.pnerimaan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityPetugasPenerimaanUlpBinding
import java.text.SimpleDateFormat
import java.util.*

class PetugasPenerimaanULPActivity : AppCompatActivity() {

    private lateinit var cal: Calendar
    private var dateSet: String? = ""
    private lateinit var binding: ActivityPetugasPenerimaanUlpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetugasPenerimaanUlpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cal = Calendar.getInstance()

        val kurirArray = arrayOf(
            "Kurir 1", "Kurir 2"
        )
        val adapterStatus = ArrayAdapter(
            this@PetugasPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            kurirArray
        )
        binding.dropdownKurirPenerimaanulp.setAdapter(adapterStatus)
        binding.dropdownKurirPenerimaanulp.setOnItemClickListener { parent, view, position, id ->
            kurirArray[position]
        }

        val penerimaArray = arrayOf(
            "Penerima 1", "Penerima 2"
        )
        val penerimaAdapterStatus = ArrayAdapter(
            this@PetugasPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            penerimaArray
        )
        binding.dropdownPejabatPenerimaPenerimaanulp.setAdapter(penerimaAdapterStatus)
        binding.dropdownPejabatPenerimaPenerimaanulp.setOnItemClickListener { parent, view, position, id ->
            penerimaArray[position]
        }

        val packagingArray = arrayOf(
            "Packaging 1", "Packaging 2"
        )
        val packagingAdapterStatus = ArrayAdapter(
            this@PetugasPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            packagingArray
        )
        binding.dropdownPackagingPenerimaanulp.setAdapter(packagingAdapterStatus)
        binding.dropdownPackagingPenerimaanulp.setOnItemClickListener { parent, view, position, id ->
            packagingArray[position]
        }

        val pejabatArray = arrayOf(
            "Pejabat 1", "Pejabat 2"
        )
        val pejabatAdapterStatus = ArrayAdapter(
            this@PetugasPenerimaanULPActivity,
            android.R.layout.simple_dropdown_item_1line,
            pejabatArray
        )
        binding.dropdownJabatan2.setAdapter(pejabatAdapterStatus)
        binding.dropdownPejabatPenerimaPenerimaanulp.setOnItemClickListener { parent, view, position, id ->
            pejabatArray[position]
        }

        binding.btnLanjutpenerimaanulp.setOnClickListener {
            startActivity(Intent(this@PetugasPenerimaanULPActivity, PenerimaanSetelahPetugasPenerimaanULPActivity::class.java))
        }

        setDatePicker()
    }

    private fun setDatePicker() {
        val setDateListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edtTanggalDokumenulp.setText(sdf.format(cal.time))
                dateSet = sdf.format(cal.time)
            }

        binding.edtTanggalDokumenulp.setOnClickListener {
            DatePickerDialog(
                this@PetugasPenerimaanULPActivity, setDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}