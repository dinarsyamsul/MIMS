package dev.iconpln.mims.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.iconpln.mims.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var muncul = false
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.img1.setOnClickListener{
//            if (muncul == true){
//                binding.img1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24))
//                binding.const2.visibility = View.VISIBLE
//                muncul = !muncul
//
//            } else {
//                binding.img1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24))
//                binding.const2.visibility = View.GONE
//                muncul = !muncul
//            }
//        }
//
//        binding.btn1.setOnClickListener {
//            val  dialogBinding = layoutInflater.inflate(R.layout.activity_popdialog, null)
//
//            val myDialog = Dialog(requireContext())
//            myDialog.setContentView(dialogBinding)
//
//            myDialog.setCancelable(true)
//            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            myDialog.show()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }

}