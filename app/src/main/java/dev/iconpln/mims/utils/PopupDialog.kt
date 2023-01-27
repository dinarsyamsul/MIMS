package dev.iconpln.mims.utils

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.iconpln.mims.databinding.ActivityPopdialogBinding
import dev.iconpln.mims.ui.role.pabrikan.tracking.DataMaterialTrackingActivity

class PopupDialog: BottomSheetDialogFragment() {

    private var _binding: ActivityPopdialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityPopdialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {
            val intent = Intent(requireContext(), DataMaterialTrackingActivity::class.java)
            intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, binding.inptSnMaterial.text.toString())
            startActivity(intent)
        }
    }
}