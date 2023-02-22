package dev.iconpln.mims.ui.pnerimaan

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.iconpln.mims.databinding.ActivityPenerimaanUlpactivityBinding
import java.text.SimpleDateFormat
import java.util.*

class PenerimaanULPActivity : AppCompatActivity() {

    private lateinit var cal: Calendar
    private var dateSet: String? = ""
    private lateinit var binding: ActivityPenerimaanUlpactivityBinding
    private val list = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerimaanUlpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cal = Calendar.getInstance()

        list.addAll(arrayListOf("1", "2", "3", "4", "5", "6", "7", "8"))
        showRecyclerView()
        setDatePicker()

    }

    private fun showRecyclerView() {
        binding.rvPenerimaanULP.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = PenerimaanULPAdapter(list)
        binding.rvPenerimaanULP.adapter = listDataAdapter
        listDataAdapter.setOnItemClickCallback(object : PenerimaanULPAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                startActivity(
                    Intent(
                        this@PenerimaanULPActivity,
                        PetugasPenerimaanULPActivity::class.java
                    )
                )
            }
        })
    }

    private fun setDatePicker() {
        val setDateListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edtTanggalDiterimaULP.setText(sdf.format(cal.time))
                dateSet = sdf.format(cal.time)
            }

        binding.edtTanggalDiterimaULP.setOnClickListener {
            DatePickerDialog(
                this@PenerimaanULPActivity, setDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}