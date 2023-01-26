package dev.iconpln.mims.ui.login.loginbiometric

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoMaster
import dev.iconpln.mims.data.local.databasereport.DatabaseReport
import dev.iconpln.mims.data.local.databasereport.ReportUploader
import dev.iconpln.mims.databinding.ActivityLoginBiometricBinding
import dev.iconpln.mims.ui.OtpActivity
import dev.iconpln.mims.ui.login.LoginActivity
import dev.iconpln.mims.ui.login.LoginViewModel
import dev.iconpln.mims.ui.role.pabrikan.DashboardPabrikanActivity
import dev.iconpln.mims.ui.role.pusertif.DashboardPusertifActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.Helper
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.StorageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTimeUtils

class LoginBiometricActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBiometricBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var session: SessionManager

    private var mAndroidId: String = ""
    private var mAppVersion: String = ""
    private var mDeviceData: String = ""
    private var mIpAddress: String = ""
    private var androidVersion: Int = 0
    private var dateTimeUtc: Long = 0L

    private var username: String = "0999.GUDANG"
    private var mPassword: String = "1998"

    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkStoragePermission()

        session = SessionManager(this)
        mAndroidId = Helper.getAndroidId(this)
        mAppVersion = Helper.getVersionApp(this)
        mDeviceData = Helper.deviceData
        mIpAddress = Helper.getIPAddress(true)
        androidVersion = Build.VERSION.SDK_INT
        dateTimeUtc = DateTimeUtils.currentTimeMillis()
        Log.d("androidID", mAndroidId)

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
                binding.btnBiometric.visibility=View.GONE
            }
        }
        else{
            binding.btnBiometric.visibility=View.GONE
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
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

        session.user_token.asLiveData().observe(this) { token ->
            Log.d("LoginActivity", "cek user token: $token")
        }

        loginViewModel.loginResponses.observe(this) { result ->
//            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
//            when (result.message) {
//                "DO LOGIN" -> {
//                    startActivity(Intent(this, DashboardPusertifActivity::class.java))
//                }
//                "DO OTP" -> {
//                    startActivity(Intent(this, OtpActivity::class.java)
//                        .putExtra("extra_username", "0999.GUDANG"))
//                }
//            }
        }

        loginViewModel.isLoading.observe(this) {
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
                loginUser()
                Log.d(ContentValues.TAG, "Authentication was successful")
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                StorageUtils.createDirectories()
                initDaoSession()
                DatabaseReport.getDatabase(this)
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Storage permission is needed to write external storage.", Toast.LENGTH_SHORT).show()
                }
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), Config.REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            }
        } else {
            StorageUtils.createDirectories()
            DatabaseReport.getDatabase(this)
            val iService = Intent(applicationContext, ReportUploader::class.java)
            startService(iService)
        }
    }

    private fun initDaoSession() {
        if ((application as MyApplication).daoSession == null) {
            val database = StorageUtils.getDirectory(StorageUtils.DIRECTORY_DATABASE) + "/" + Config.DATABASE_NAME
            val helper = DaoMaster.DevOpenHelper(this, database)
            val db = helper.writableDb
            (application as MyApplication).daoSession = DaoMaster(db).newSession()
        }
    }

    private fun loginUser() {
        with(binding) {
            val email = username
            val password = mPassword
        }
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
