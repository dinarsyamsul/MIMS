package dev.iconpln.mims

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.iconpln.mims.databinding.ActivityBottomsheetTrackingBinding

class BottomsheetTrackingActivity : BottomSheetDialogFragment() {

    private var _binding: ActivityBottomsheetTrackingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityBottomsheetTrackingBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
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