package dev.iconpln.mims.ui.auth

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.data.local.database.DaoMaster
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database_local.DatabaseReport
import dev.iconpln.mims.data.local.database_local.ReportUploader
import dev.iconpln.mims.databinding.ActivityLoginBinding
import dev.iconpln.mims.ui.auth.otp.OtpActivity
import dev.iconpln.mims.utils.*
import dev.iconpln.mims.utils.StorageUtils.isPermissionAllowed
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
    private var APP_STORAGE_ACCESS_REQUEST_CODE=501

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()

        session = SessionManager(this)

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
                        SharedPrefsUtils.setStringPreference(this@LoginActivity,"jwt", it.token!!)
                        SharedPrefsUtils.setStringPreference(this@LoginActivity, "username", username)
                        SharedPrefsUtils.setStringPreference(this@LoginActivity, "email", it.user?.mail!!)
                        SharedPrefsUtils.setStringPreference(this@LoginActivity, "password", password)

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

    private fun requestPermission() {
        if(!isPermissionAllowed(this@LoginActivity)){
            if (SDK_INT >= Build.VERSION_CODES.R) {
                Toast.makeText(this, "Harap izinkan aplikasi mengakses penyimpanan", Toast.LENGTH_SHORT).show();
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                    startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE)
                }
            } else {
                //below android 11
                ActivityCompat.requestPermissions(
                    this@LoginActivity, arrayOf(WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE), APP_STORAGE_ACCESS_REQUEST_CODE
                )
            }
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

            daoSession = (application as MyApplication).daoSession!!
            loginViewModel.getLogin(this@LoginActivity,
                daoSession,username, password,"",
                mAndroidId,mAppVersion,mDeviceData,mIpAddress,
                androidVersion,dateTimeUtc,session)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_STORAGE_ACCESS_REQUEST_CODE) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                Log.i("Permission","OS11")
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    StorageUtils.createDirectories()
                    initDaoSession()
                    DatabaseReport.getDatabase(this)
                    val iService = Intent(applicationContext, ReportUploader::class.java)
                    startService(iService)
                } else {
                    Toast.makeText(this, "Harap izinkan aplikasi mengakses penyimpanan!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            APP_STORAGE_ACCESS_REQUEST_CODE -> if (grantResults.size > 0) {
                Log.i("Permission","Dibawah OS11")
                val READ_EXTERNAL_STORAGE = grantResults[0] === PackageManager.PERMISSION_GRANTED
                val WRITE_EXTERNAL_STORAGE = grantResults[1] === PackageManager.PERMISSION_GRANTED
                if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    StorageUtils.createDirectories()
                    initDaoSession()
                    DatabaseReport.getDatabase(this)
                    val iService = Intent(applicationContext, ReportUploader::class.java)
                    startService(iService)
                } else {
                    Toast.makeText(this, "Harap izinkan aplikasi mengakses penyimpanan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
