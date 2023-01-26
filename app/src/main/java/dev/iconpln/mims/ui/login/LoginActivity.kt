package dev.iconpln.mims.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.local.databasereport.DatabaseReport
import dev.iconpln.mims.data.local.databasereport.ReportUploader
import dev.iconpln.mims.databinding.ActivityLoginBinding
import dev.iconpln.mims.ui.OtpActivity
import dev.iconpln.mims.ui.forgotpassword.ForgotPasswordActivity
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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var session: SessionManager
    private lateinit var daoSession: DaoSession

    private var mAndroidId: String = ""
    private var mAppVersion: String = ""
    private var mDeviceData: String = ""
    private var mIpAddress: String = ""
    private var androidVersion: Int = 0
    private var dateTimeUtc: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkStoragePermission()

        daoSession = (application as MyApplication).daoSession!!
        session = SessionManager(this)
        mAndroidId = Helper.getAndroidId(this)
        mAppVersion = Helper.getVersionApp(this)
        mDeviceData = Helper.deviceData
        mIpAddress = Helper.getIPAddress(true)
        androidVersion = Build.VERSION.SDK_INT
        dateTimeUtc = DateTimeUtils.currentTimeMillis()
        Log.d("androidID", mAndroidId)

        session.user_token.asLiveData().observe(this) { token ->
            Log.d("LoginActivity", "cek user token: $token")
        }

        loginViewModel.loginResponses.observe(this) { result ->
            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
            when (result) {
                "DO LOGIN" -> {
                    startActivity(Intent(this, DashboardPabrikanActivity::class.java))
                }
                "DO OTP" -> {
                    startActivity(Intent(this, OtpActivity::class.java)
                        .putExtra("extra_username", binding.edtEmail.text.toString().trim())
                        .putExtra("android_id", mAndroidId)
                        .putExtra("device_data", mDeviceData)
                        .putExtra("otp_type", Config.OTP_TYPE_DO_LOGIN_OTP))
                }
            }
        }

        loginViewModel.agoLoginResponse.observe(this) {
            it.data.forEach { result ->
                if (result.id != null) {
                    startActivity(Intent(this@LoginActivity, DashboardPabrikanActivity::class.java))
                    Toast.makeText(
                        this,
                        "Login berhasil",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Login gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginViewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.tvMsgError.visibility = View.GONE
            loginUser()
//            startActivity(Intent(this, OtpActivity::class.java)
//                .putExtra("data", "this is from login"))
//            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

        binding.txtForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnUpdateVersion.setOnClickListener {
//            startActivity(Intent(this,LoginBiometricActivity::class.java))
        }

        session.user_email.asLiveData().observe(this){
            binding.edtEmail.setText(it)
            if (!it.isNullOrEmpty()){
                binding.cbRememberMe.isChecked = true
            }
        }
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
            val email = edtEmail.text.toString().trim()
            val password = edtPass.text.toString().trim()

//            if(email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                tvMsgError.visibility = View.VISIBLE
//                tvMsgError.text = "Format email yang dimasukkan salah"
//                return
//            }

            if (email.isEmpty()) {
                tvMsgError.visibility = View.VISIBLE
                tvMsgError.text = "Email tidak boleh kosong"
                return
            }

            if (password.isEmpty()) {
                tvMsgError.visibility = View.VISIBLE
                tvMsgError.text = "Password tidak boleh kosong"
                return
            }

//            if (password.length < 5) {
//                tvMsgError.visibility = View.VISIBLE
//                tvMsgError.text = "Password minimal terdiri dari 5 karakter"
//                return
//            }

            CoroutineScope(Dispatchers.Main).launch {
                if (cbRememberMe.isChecked){
                    session.rememberMe(email)
                }else{
                    session.clearRememberMe()
                }
            }

            loginViewModel.getLogin(email, password, "",mAndroidId,"1.0.0",
                mDeviceData,mIpAddress,androidVersion,dateTimeUtc,this@LoginActivity, daoSession)
        }
    }
}
