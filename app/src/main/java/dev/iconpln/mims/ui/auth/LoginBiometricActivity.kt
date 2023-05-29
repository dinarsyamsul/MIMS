package dev.iconpln.mims.ui.auth

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.databinding.ActivityLoginBiometricBinding
import dev.iconpln.mims.ui.auth.otp.OtpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.Helper
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTimeUtils

class LoginBiometricActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBiometricBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var session: SessionManager
    private lateinit var daoSession: DaoSession

    private var mAndroidId: String = ""
    private var mAppVersion: String = ""
    private var mDeviceData: String = ""
    private var mIpAddress: String = ""
    private var androidVersion: Int = 0
    private var dateTimeUtc: Long = 0L

    private var username: String = ""
    private var mPassword: String = ""

    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)
        daoSession = (application as MyApplication).daoSession!!

        session = SessionManager(this)
        mAndroidId = Helper.getAndroidId(this)
        mAppVersion = Helper.getVersionApp(this)
        mDeviceData = Helper.deviceData
        mIpAddress = Helper.getIPAddress(true)
        androidVersion = Build.VERSION.SDK_INT
        dateTimeUtc = DateTimeUtils.currentTimeMillis()
        Log.d("androidID", mAndroidId)

        username = SharedPrefsUtils.getStringPreference(this@LoginBiometricActivity,"username","14.Hexing_Electric")!!
        mPassword = SharedPrefsUtils.getStringPreference(this@LoginBiometricActivity,"password","12345")!!


        biometricPrompt = createBiometricPrompt()

        if (Build.VERSION.SDK_INT >=28) {
            if(!username.isNullOrEmpty() && !mPassword.isNullOrEmpty()){
                val promptInfo = createPromptInfo()
                if (BiometricManager.from(this@LoginBiometricActivity)
                        .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricPrompt.authenticate(promptInfo)
                }
            }
            else{
                binding.btnBiometric.visibility= View.GONE
            }
        }
        else{
            binding.btnBiometric.visibility= View.GONE
        }

        binding.btnBiometric.setOnClickListener {
            if(username.isNullOrEmpty() && mPassword.isNullOrEmpty()){
                Toast.makeText(this, "silahkan masukkan username dan password dulu", Toast.LENGTH_SHORT).show()
            }else {
                val promptInfo = createPromptInfo()
                if (BiometricManager.from(this@LoginBiometricActivity)
                        .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricPrompt.authenticate(promptInfo)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.loginResponse.observe(this) {
            when(it.message){
                "DO LOGIN" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        session.sessionActivity(Config.IS_LOGIN)
                        session.saveAuthToken(
                            it.token.toString(),
                            it.user?.roleId.toString(),
                            it.user?.mail.toString(),
                            it.user?.kdUser.toString()
                        )
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "jwtWeb", it.webToken!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity,"jwt", it.token!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "username", username)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "fullName", it.user?.fullName!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "roleName", it.user?.roleName!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "email", it.user?.mail!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "plant", it.user?.plant!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "plantName", it.user?.plantName!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "storloc", it.user?.storloc!!)
                        SharedPrefsUtils.setStringPreference(this@LoginBiometricActivity, "storlocName", it.user?.storLocName!!)
                        SharedPrefsUtils.setIntegerPreference(this@LoginBiometricActivity, "subroleId", it.user?.subroleId!!)
                        SharedPrefsUtils.setIntegerPreference(this@LoginBiometricActivity, "roleId", it.user?.roleId!!)

                        withContext(Dispatchers.Main){
                            val intentToHome = Intent(this@LoginBiometricActivity, HomeActivity::class.java)
                            startActivity(intentToHome)
                            finish()
                        }
                    }
                }
                "DO OTP" -> {
                    viewModel.sendOtp(this@LoginBiometricActivity, username)
                    val intentToOtp = Intent(this@LoginBiometricActivity, OtpActivity::class.java)
                    intentToOtp.putExtra("username", username)
                    intentToOtp.putExtra("otpFrom", "login")
                    startActivity(intentToOtp)
                    finish()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            @SuppressLint("RestrictedApi")
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(ContentValues.TAG, "$errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {

                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(ContentValues.TAG, "Authentication failed for an unknown reason")

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startActivity(Intent(this@LoginBiometricActivity, HomeActivity::class.java))
                viewModel.getLogin(this@LoginBiometricActivity,
                    daoSession,username, mPassword,"",
                    mAndroidId,mAppVersion,mDeviceData,mIpAddress,
                    androidVersion,dateTimeUtc,session)
                Log.d(ContentValues.TAG, "Authentication was successful")
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autentikasi Biometric")
            .setSubtitle("Harap letakan jempol anda pada sensor biometric")
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText("Autentikasi manual")
            .build()
        return promptInfo
    }
}