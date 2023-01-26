package dev.iconpln.mims.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dev.iconpln.mims.ui.role.pabrikan.pengujian.PengujianActivity
import dev.iconpln.mims.databinding.FragmentHomeBinding
import dev.iconpln.mims.ui.login.LoginActivity
import dev.iconpln.mims.ui.role.pabrikan.arttribute_material.DataAtributMaterialActivity
import dev.iconpln.mims.ui.role.pabrikan.purchase_order.MonitoringPurchaseOrderActivity
import dev.iconpln.mims.ui.role.pabrikan.tracking.TrackingHistoryActivity
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.launch

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
            val i = Intent(context, MonitoringPurchaseOrderActivity::class.java)
            context?.startActivity(i)
        }

        binding.card2.setOnClickListener {
            val intent = Intent(context, PengujianActivity::class.java)
            context?.startActivity(intent)
        }

        binding.card3.setOnClickListener {
            startActivity(Intent(context, TrackingHistoryActivity::class.java))
        }

        binding.card4.setOnClickListener {
            val intent = Intent(context, DataAtributMaterialActivity::class.java)
            context?.startActivity(intent)
        }

        val session = SessionManager(requireContext())

        binding.btnLogout.setOnClickListener {
            val onLogout = Intent(requireContext(), LoginActivity::class.java)
            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

            lifecycleScope.launch {
                session.clearUserToken()
                session.clearSessionActivity()
            }
            session.user_token.asLiveData().observe(viewLifecycleOwner) {
                Log.d("MainActivity", "cek token : $it")
            }
            onLogout.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(onLogout)
            activity?.finish()
        }

//        binding.card2.setOnClickListener {
//            Toast.makeText(context, "Under Maintenance", Toast.LENGTH_SHORT).show()
//        }
    }


}