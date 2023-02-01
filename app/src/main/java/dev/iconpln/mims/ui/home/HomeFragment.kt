package dev.iconpln.mims.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.FragmentHomeBinding
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.ui.monitoring.MonitoringActivity
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pengiriman.PengirimanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.rating.RatingActivity
import dev.iconpln.mims.ui.role.pabrikan.arttribute_material.DataAtributMaterialActivity
import dev.iconpln.mims.ui.role.pabrikan.pengujian.PengujianActivity
import dev.iconpln.mims.ui.tracking.TrackingHistoryActivity
import dev.iconpln.mims.ui.transmission_history.TransmissionActivity
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

        val session = SessionManager(requireContext())
        val daoSession = (requireActivity().application as MyApplication).daoSession!!


        var listPrivilege = daoSession.tPrivilegeDao.queryBuilder().list()

        binding.back.setOnClickListener {
            startActivity(Intent(requireActivity(), TransmissionActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            val onLogout = Intent(requireContext(), LoginActivity::class.java)
            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

            lifecycleScope.launch {
                session.clearUserToken()
            }
            session.user_token.asLiveData().observe(viewLifecycleOwner) {
                Log.d("MainActivity", "cek token : $it")
            }
            onLogout.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(onLogout)
            activity?.finish()
        }

        binding.btnMonitoring.setOnClickListener {
            startActivity(Intent(requireActivity(), MonitoringActivity::class.java))
        }

        binding.btnDataAttr.setOnClickListener {
            startActivity(Intent(requireActivity(), DataAtributMaterialActivity::class.java))
        }

        binding.btnPengujian.setOnClickListener{
            startActivity(Intent(requireActivity(), PengujianActivity::class.java))
        }

        binding.btnPenerimaan.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val btnPemeriksaan = view.findViewById<CardView>(R.id.cv_pemeriksaan)
            val btnPenerimaan = view.findViewById<CardView>(R.id.cv_penerimaan)
            val btnRating = view.findViewById<CardView>(R.id.cv_rating)

            btnPenerimaan.setOnClickListener {
                startActivity(Intent(requireActivity(), PenerimaanActivity::class.java))
            }

            btnPemeriksaan.setOnClickListener {
                startActivity(Intent(requireActivity(), PemeriksaanActivity::class.java))
            }

            btnRating.setOnClickListener {
                startActivity(Intent(requireActivity(), RatingActivity::class.java))
            }

            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.btnTracking.setOnClickListener {
            startActivity(Intent(requireActivity(), TrackingHistoryActivity::class.java))
        }

        binding.btnPengiriman.setOnClickListener {
            startActivity(Intent(requireActivity(), PengirimanActivity::class.java))
        }
    }


}