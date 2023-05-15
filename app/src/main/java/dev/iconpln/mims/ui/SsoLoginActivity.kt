package dev.iconpln.mims.ui

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.databinding.ActivitySsoLoginBinding
import dev.iconpln.mims.ui.auth.AuthViewModel
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

class SsoLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySsoLoginBinding
    private lateinit var stringUrl: String

    private val loginViewModel: AuthViewModel by viewModels()
    private lateinit var session: SessionManager
    private lateinit var daoSession: DaoSession

    private var mAndroidId: String = ""
    private var mAppVersion: String = ""
    private var mDeviceData: String = ""
    private var mIpAddress: String = ""
    private var androidVersion: Int = 0
    private var dateTimeUtc: Long = 0L

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySsoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stringUrl = intent.getStringExtra("url")!!

        dialog = Dialog(this@SsoLoginActivity)
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

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
                        Log.d("checkUsername",it.user?.userName!!)
                        Log.d("checkIdTokenSSO", it?.idTokenSso!!)
                        SharedPrefsUtils.setStringPreference(this@SsoLoginActivity, "username", it.user?.userName!!)
                        SharedPrefsUtils.setStringPreference(this@SsoLoginActivity,"jwt", it.token!!)
                        SharedPrefsUtils.setStringPreference(this@SsoLoginActivity,"idTokenSso", it.idTokenSso!!)
                        SharedPrefsUtils.setStringPreference(this@SsoLoginActivity, "email", it.user?.mail!!)
                        SharedPrefsUtils.setStringPreference(this@SsoLoginActivity, "plant", it.user.plant!!)
                        SharedPrefsUtils.setStringPreference(this@SsoLoginActivity, "storloc", it.user.storloc!!)
                        SharedPrefsUtils.setIntegerPreference(this@SsoLoginActivity, "subroleId", it.user.subroleId!!)
                        SharedPrefsUtils.setIntegerPreference(this@SsoLoginActivity, "roleId", it.user.roleId!!)

                        withContext(Dispatchers.Main){
                            Log.d("Login SSO", "Ya")
                            startActivity(Intent(this@SsoLoginActivity, HomeActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
                            finish()
//                            val intentToHome = Intent(this@SsoLoginActivity, HomeActivity::class.java)
//                            intentToHome.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            intentToHome.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            intentToHome.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//                            startActivity(intentToHome)
//                            finish()
                        }
                    }
                }
                "DO OTP" -> {
                    loginViewModel.sendOtp(this@SsoLoginActivity, it.user?.userName!!)
                    val intentToOtp = Intent(this@SsoLoginActivity, OtpActivity::class.java)
                    intentToOtp.putExtra("username", it.user?.userName!!)
                    intentToOtp.putExtra("otpFrom", "login")
                    startActivity(intentToOtp)
                    finish()
                }
            }
        }

        loginViewModel.isLoading.observe(this){
            when(it) {
                true -> {
                    binding.wvSso.visibility = View.VISIBLE
                    binding.bgLoading.visibility = View.VISIBLE
                    dialog.show()
                    //binding.progressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.wvSso.visibility = View.GONE
                    binding.bgLoading.visibility = View.GONE
                    dialog.dismiss()
                    //binding.progressBar.visibility = View.GONE
                }
            }
        }

        with(binding){
            wvSso.settings.javaScriptEnabled = true

            wvSso.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    progressBar.visibility = View.GONE
                    super.onPageFinished(view, url)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url!!)
                    return true
                }

                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    val uri = Uri.parse(url)
                    val code = uri.getQueryParameter("code")
//                    Toast.makeText(this@SsoLoginActivity, uri.getQueryParameter("code"), Toast.LENGTH_SHORT).show()
                    if (code.isNullOrEmpty()){
                        Log.d("belum dapat kode", "ya")
//                        Toast.makeText(this@SsoLoginActivity, "Silahkan login dengan akun IAM anda", Toast.LENGTH_SHORT).show()
                    }else{
                        doLoginWithSso(code)
                    }
                    Log.d("kode_sso",code.toString())
                    super.doUpdateVisitedHistory(view, url, isReload)
                }
            }
            wvSso.loadUrl(stringUrl)
        }
    }

    private fun doLoginWithSso(code: String) {
        with(binding) {
            if (code.isNullOrEmpty()){
                Toast.makeText(this@SsoLoginActivity, "code tidak ditemukan", Toast.LENGTH_SHORT).show()
                return
            }

            daoSession = (application as MyApplication).daoSession!!
            loginViewModel.getLoginSso(this@SsoLoginActivity,
                daoSession,code,"",
                mAndroidId,mAppVersion,mDeviceData,mIpAddress,
                androidVersion,dateTimeUtc,session)
        }
    }
}