package dev.iconpln.mims.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.data.remote.response.AgoLoginResponse
import dev.iconpln.mims.data.remote.response.HitEmailResponse
import dev.iconpln.mims.data.remote.response.LoginResponse
import dev.iconpln.mims.data.remote.response.VerifyTokenResponse
import dev.iconpln.mims.data.remote.service.ApiService
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.TokenManager
import dev.iconpln.mims.utils.map
import kotlinx.coroutines.*
import org.json.JSONObject

class LoginViewModel(
    private val session: TokenManager,
    private val apiService: ApiService,
    netWorkStatusTracker: NetworkStatusTracker
) : ViewModel() {

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _agoLoginResponse = MutableLiveData<AgoLoginResponse>()
    val agoLoginResponse: LiveData<AgoLoginResponse> = _agoLoginResponse

    private val _hitEmailResponse = MutableLiveData<HitEmailResponse>()
    val hitEmailResponse: LiveData<HitEmailResponse> = _hitEmailResponse

    private val _verifyTokenResponse = MutableLiveData<VerifyTokenResponse>()
    val verifyTokenResponse: LiveData<VerifyTokenResponse> = _verifyTokenResponse

    val state =
        netWorkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched }
            )
            .asLiveData(Dispatchers.IO)

    fun getLogin(username: String, password: String, device_token: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["password"] = password
            requestBody["device_token"] = device_token
            val response = apiService.login(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val loginResult = response.body()
                    _loginResponse.postValue(loginResult)
                    loginResult?.data?.forEach {
                        if (it.userToken != "") {
                            session.saveAuthToken(
                                it.userToken,
                                it.deviceToken,
                                it.roleId
                            )
                        }
                    }
                } else {
                    _isLoading.value = false
                    val error = response.errorBody()?.toString()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun getAgoLogin(username: String, password: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            val requestBody = mutableMapOf<String, String>()
//            requestBody["username"] = username
//            requestBody["password"] = password
//            val response = apiService.login(requestBody)

            val response = apiService.agoLogin(username, password)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val loginResult = response.body()
                    _agoLoginResponse.postValue(loginResult)
                } else {
                    _isLoading.value = false
                    val error = response.errorBody()?.toString()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun hitEmail(username: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.hitEmailResponse(username)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val hitMailResult = response.body()
                    _hitEmailResponse.postValue(hitMailResult)
                } else {
                    _isLoading.value = false
                    val error = response.errorBody()?.toString()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun sendTokenOtp(mail_token: String, username: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["email_token"] = mail_token
            requestBody["username"] = username
            val response = apiService.sendOtp(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val loginResult = response.body()
                    _verifyTokenResponse.postValue(loginResult)
                    loginResult?.data?.forEach {
                        session.saveAuthToken(
                            user_token = it.userToken,
                            device_token = it.deviceToken,
                            role_id = it.roleId
                        )
                    }
                } else {
                    _isLoading.value = false
                    val error = response.errorBody()?.toString()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    private fun onError(message: String) {
        _isLoading.postValue(false)
        _errorMessage.postValue(message)
    }

    private fun getErrorMessage(raw: String): String {
        val obj = JSONObject(raw)
        return obj.getString("message")
    }
}

sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}