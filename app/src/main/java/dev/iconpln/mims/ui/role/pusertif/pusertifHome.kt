package dev.iconpln.mims.ui.role.pusertif

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dev.iconpln.mims.databinding.FragmentPusertifHomeBinding
import dev.iconpln.mims.ui.login.LoginActivity
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.launch


class pusertifHome : Fragment() {

    private var _binding: FragmentPusertifHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPusertifHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
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
    }
}
