package dev.iconpln.mims.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivitySsoLogoutBinding
import dev.iconpln.mims.ui.auth.LoginActivity
import dev.iconpln.mims.ui.auth.LoginBiometricActivity
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SsoLogoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySsoLogoutBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySsoLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this@SsoLogoutActivity)
        var idTokenSso = SharedPrefsUtils.getStringPreference(this@SsoLogoutActivity, "idTokenSso","")
        var stringUrl = "https://iam.pln.co.id/svc-core/oauth2/session/end?post_logout_redirect_uri=http://localhost:8080/user/logout&id_token_hint=$idTokenSso"

        with(binding){
            wvLogout.settings.javaScriptEnabled = true

            wvLogout.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
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
                    if (url != stringUrl){
                        wvLogout.visibility = View.GONE
                        Logout(session)
                    }
                    super.doUpdateVisitedHistory(view, url, isReload)
                }
            }
            wvLogout.loadUrl(stringUrl)
        }
    }

    private fun Logout(session: SessionManager) {
        session.is_login_biometric.asLiveData().observe(this@SsoLogoutActivity){
            Log.d("isClicked", "logout")
            when(it){
                1 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        session.clearUserToken()
                        session.clearSaveAuthToken()
                        session.clearSessionActivity()
                        SharedPrefsUtils.clearPreferences(this@SsoLogoutActivity)

                        withContext(Dispatchers.Main){
                            startActivity(
                                Intent(this@SsoLogoutActivity, LoginBiometricActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                            finish()
                        }
                    }
                }else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    session.clearUserToken()
                    session.clearSaveAuthToken()
                    session.clearSessionActivity()
                    SharedPrefsUtils.clearPreferences(this@SsoLogoutActivity)

                    withContext(Dispatchers.Main){
                        startActivity(
                            Intent(this@SsoLogoutActivity, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        finish()
                    }
                }
            }
            }
        }
    }
}