package dev.iconpln.mims

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.iconpln.mims.databinding.ActivityBottomsheetTrackingBinding

class BottomsheetTrackingActivity : BottomSheetDialogFragment() {
    
    private lateinit var binding: ActivityBottomsheetTrackingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_bottomsheet_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        binding.card1.setOnClickListener {
            Toast.makeText(context, "Test QR Code", Toast.LENGTH_SHORT).show()
        }
        
        binding.card2.setOnClickListener {
            Toast.makeText(context, "Test Barcode", Toast.LENGTH_SHORT).show()
        }
        
    }
}