package dev.iconpln.mims.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.FragmentProfileBinding
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.ui.auth.LoginBiometricActivity
import dev.iconpln.mims.ui.auth.change_password.ChangePasswordActivity
import dev.iconpln.mims.ui.transmission_history.TransmissionActivity
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.popup_validation);
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
            val ivLogout = dialog.findViewById(R.id.imageView11) as ImageView
            val message = dialog.findViewById(R.id.message) as TextView
            val txtMessage = dialog.findViewById(R.id.txt_message) as TextView
            val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
            val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton

            ivLogout.setImageResource(R.drawable.ic_warning)
            message.text = "Logout"
            txtMessage.text = "Apakah kamu yakin untuk logout?"

            btnTidak.setOnClickListener {
                dialog.dismiss();
            }

            btnYa.setOnClickListener {
                Logout(session)
                dialog.dismiss()
            }

            dialog.show();
        }
    }

    private fun Logout(session: SessionManager) {
        session.is_login_biometric.asLiveData().observe(requireActivity()){
            Log.d("isClicked", "logout")
            when(it){
                1 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        session.clearUserToken()
                        session.clearSaveAuthToken()
                        session.clearSessionActivity()

                        withContext(Dispatchers.Main){
                            startActivity(Intent(requireActivity(), LoginBiometricActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                            requireActivity().finish()
                        }
                    }
                }else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    session.clearUserToken()
                    session.clearSaveAuthToken()
                    session.clearSessionActivity()

                    withContext(Dispatchers.Main){
                        startActivity(Intent(requireActivity(), LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        requireActivity().finish()
                    }
                }
            }
            }
        }
    }
}