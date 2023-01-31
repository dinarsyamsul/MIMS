package dev.iconpln.mims.utils

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.iconpln.mims.databinding.ActivityPopdialogBinding
import dev.iconpln.mims.ui.tracking.DataMaterialTrackingActivity
import dev.iconpln.mims.ui.tracking.SpecMaterialActivity
import dev.iconpln.mims.ui.tracking.TrackingHistoryViewModel

class PopupDialog: BottomSheetDialogFragment() {

    private var _binding: ActivityPopdialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrackingHistoryViewModel by viewModels()
    var sn=""

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

        viewModel.trackingResponse.observe(this) {
            binding.apply {
                if (it.datas.isNullOrEmpty()){
                    Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(requireContext(), SpecMaterialActivity::class.java)
                    intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, sn)
                    startActivity(intent)
                }
            }
        }

        binding.btnOk.setOnClickListener {
            if(binding.inptSnMaterial.text.toString().equals("")){
                Toast.makeText(getActivity(),"Harap inputkan serial number",Toast.LENGTH_SHORT).show()
            }
            else{
                sn=binding.inptSnMaterial.text.toString()
                viewModel.getTrackingHistory(sn, requireContext())
//                val intent = Intent(requireContext(), DataMaterialTrackingActivity::class.java)
//                intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, binding.inptSnMaterial.text.toString())
//                startActivity(intent)
//
//                val intent = Intent(requireContext(), SpecMaterialActivity::class.java)
//                intent.putExtra(DataMaterialTrackingActivity.EXTRA_SN, binding.inptSnMaterial.text.toString())
//                startActivity(intent)
            }

        }
    }
}