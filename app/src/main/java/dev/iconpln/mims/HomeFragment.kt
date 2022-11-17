package dev.iconpln.mims

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.iconpln.mims.databinding.ActivityMonitoringPurchaseOrderBinding
import dev.iconpln.mims.databinding.FragmentHomeBinding
import dev.iconpln.mims.databinding.FragmentProfileBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.card1.setOnClickListener {
            val i = Intent(context, MonitoringPurchaseOrder::class.java)
            context?.startActivity(i)

            binding.card4.setOnClickListener {
                val intent = Intent(context, UploadDataMaterial::class.java)
                context?.startActivity(intent)


            }
        }
    }


}