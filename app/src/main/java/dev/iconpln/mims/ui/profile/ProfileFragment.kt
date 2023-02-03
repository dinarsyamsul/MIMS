package dev.iconpln.mims.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.FragmentHomeBinding
import dev.iconpln.mims.databinding.FragmentProfileBinding
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.ui.auth.LoginBiometricActivity
import dev.iconpln.mims.ui.auth.change_password.ChangePasswordActivity
import dev.iconpln.mims.ui.transmission_history.TransmissionActivity
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireActivity())

        session.is_login_biometric.asLiveData().observe(requireActivity()){
            when(it){
                1 -> binding.btnSwitch.isChecked = true
                else -> binding.btnSwitch.isChecked = false
            }
        }

        binding.cvTransmisson.setOnClickListener {
            startActivity(Intent(requireActivity(), TransmissionActivity::class.java))
        }

        binding.cvUbahPassword.setOnClickListener {
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }

        binding.btnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            lifecycleScope.launch {
                when(isChecked){
                    true -> {
                        session.isLoginBiometric(1)
                    }

                    false -> {
                        session.isLoginBiometric(0)
                    }
                }
            }
        }

        binding.cvLogout.setOnClickListener {
            session.is_login_biometric.asLiveData().observe(requireActivity()){
                when(it){
                    1 -> {
                        val onLogout = Intent(requireContext(), LoginBiometricActivity::class.java)
                        onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

                        lifecycleScope.launch {
                            session.clearUserToken()
                            session.clearSaveAuthToken()
                            session.clearSessionActivity()
                        }
                        session.user_token.asLiveData().observe(viewLifecycleOwner) {
                            Log.d("MainActivity", "cek token : $it")
                        }
                        onLogout.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(onLogout)
                        activity?.finish()
                    }else -> {
                        val onLogout = Intent(requireContext(), LoginActivity::class.java)
                        onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        onLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

                        lifecycleScope.launch {
                           session.clearUserToken()
                           session.clearSaveAuthToken()
                           session.clearSessionActivity()
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
        }
    }
}