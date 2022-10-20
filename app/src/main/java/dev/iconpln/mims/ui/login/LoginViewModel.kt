package dev.iconpln.mims.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.remote.response.LoginResponse
import dev.iconpln.mims.data.remote.service.ApiService
import kotlinx.coroutines.*
import org.json.JSONObject

class LoginViewModel(private val apiService: ApiService) : ViewModel() {

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

    fun getLogin(username: String, password: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            val requestBody = mutableMapOf<String, String>()
//            requestBody["username"] = username
//            requestBody["password"] = password
//            val response = apiService.login(requestBody)

            val response = apiService.anotherLogin(username, password)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val loginResult = response.body()
                    _loginResponse.postValue(loginResult)
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