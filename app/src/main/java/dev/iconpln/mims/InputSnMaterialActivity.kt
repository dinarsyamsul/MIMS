package dev.iconpln.mims

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import android.view.Window
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.iconpln.mims.databinding.ActivityInputSnMaterialBinding

class InputSnMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputSnMaterialBinding
    private lateinit var rvInputSerial : RecyclerView
    private val list = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputSnMaterialBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // recyclerview
        rvInputSerial = findViewById(R.id.rv_serialnumber)
        rvInputSerial.setHasFixedSize(true)

        list.addAll(arrayListOf("","","","","","","","","","","","","","","","","","","","",""))
        showRecyclerList()

        // bottomsheet
        val bottomsheetdialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_input_sn_material,null)
        bottomsheetdialog.setContentView(view)
        binding.btnScanSnMaterial.setOnClickListener {
            bottomsheetdialog.show()
        }

        binding.btnInputSnManual.setOnClickListener {
            customDialog()
        }
    }

    private fun showRecyclerList(){
        rvInputSerial.layoutManager = LinearLayoutManager(this)
        val listInpputAdapter = InputSerialAdapter(list)
        rvInputSerial.adapter = listInpputAdapter
    }

    private fun customDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.activity_popdialog)

        dialog.show()
    }
}