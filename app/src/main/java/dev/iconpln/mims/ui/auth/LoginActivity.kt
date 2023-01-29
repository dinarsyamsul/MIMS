package dev.iconpln.mims.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoMaster
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.databasereport.DatabaseReport
import dev.iconpln.mims.data.local.databasereport.ReportUploader
import dev.iconpln.mims.databinding.ActivityLoginBinding
import dev.iconpln.mims.ui.auth.otp.OtpActivity
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.Helper
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.StorageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTimeUtils

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: AuthViewModel by viewModels()
    private lateinit var session: SessionManager
    private lateinit var daoSession: DaoSession

    private var username: String = ""
    private var password: String = ""
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

        session = SessionManager(this)
        daoSession = (application as MyApplication).daoSession!!

        mAndroidId = Helper.getAndroidId(this)
        mAppVersion = Helper.getVersionApp(this)
        mDeviceData = Helper.deviceData
        mIpAddress = Helper.getIPAddress(true)
        androidVersion = Build.VERSION.SDK_INT
        dateTimeUtc = DateTimeUtils.currentTimeMillis()

        loginViewModel.loginResponse.observe(this){
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

                        withContext(Dispatchers.Main){
                            val intentToHome = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intentToHome)
                            finish()
                        }
                    }
                }
                "DO OTP" -> {
                    loginViewModel.sendOtp(this@LoginActivity, username)
                    val intentToOtp = Intent(this@LoginActivity, OtpActivity::class.java)
                    intentToOtp.putExtra("username", username)
                    intentToOtp.putExtra("otpFrom", "login")
                    startActivity(intentToOtp)
                    finish()
                }
            }
        }

        loginViewModel.isLoading.observe(this){
            when(it) {
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.tvMsgError.visibility = View.GONE
            loginUser()
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
            username = edtEmail.text.toString().trim()
            password = edtPass.text.toString().trim()

//            if(email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                tvMsgError.visibility = View.VISIBLE
//                tvMsgError.text = "Format email yang dimasukkan salah"
//                return
//            }

            if (username.isEmpty()) {
                tvMsgError.visibility = View.VISIBLE
                tvMsgError.text = "Email tidak boleh kosong"
                return
            }

            if (password.isEmpty()) {
                tvMsgError.visibility = View.VISIBLE
                tvMsgError.text = "Password tidak boleh kosong"
                return
            }

            if (password.length < 5) {
                tvMsgError.visibility = View.VISIBLE
                tvMsgError.text = "Password minimal terdiri dari 5 karakter"
                return
            }

            CoroutineScope(Dispatchers.Main).launch {
                if (cbRememberMe.isChecked){
                    session.rememberMe(username)
                }else{
                    session.clearRememberMe()
                }
            }

            loginViewModel.getLogin(this@LoginActivity,
                daoSession,username, password,"",
                mAndroidId,mAppVersion,mDeviceData,mIpAddress,
                androidVersion,dateTimeUtc)
        }
    }
}
