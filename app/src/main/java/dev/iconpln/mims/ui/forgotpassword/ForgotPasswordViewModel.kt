package dev.iconpln.mims.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.iconpln.mims.data.remote.response.HitEmailResponse
import dev.iconpln.mims.data.remote.service.ApiService
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.NetworkStatusTracker
import dev.iconpln.mims.utils.map
import kotlinx.coroutines.*
import org.json.JSONObject

class ForgotPasswordViewModel(
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

    private val _getOtpResponse = MutableLiveData<HitEmailResponse>()
    val getOtpResponse: LiveData<HitEmailResponse> = _getOtpResponse

    val state =
        netWorkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched }
            )
            .asLiveData(Dispatchers.IO)

    fun getOtpPassword(username: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            val response = apiService.getOtpForgotPassword(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val getOtpResponse = response.body()
                    _getOtpResponse.postValue(getOtpResponse!!)
                } else {
                    _isLoading.value = false
                    val error = response.errorBody()?.toString()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun sendOtpPassword(username: String, otp: String, newPassword: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["otp"] = otp
            requestBody[newPassword] = newPassword
            val response = apiService.getOtpForgotPassword(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val getOtpResponse = response.body()
                    _getOtpResponse.postValue(getOtpResponse!!)
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