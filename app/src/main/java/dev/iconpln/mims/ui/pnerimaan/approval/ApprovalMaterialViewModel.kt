package dev.iconpln.mims.ui.pnerimaan.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.remote.response.*
import dev.iconpln.mims.data.remote.service.ApiService
import kotlinx.coroutines.*
import org.json.JSONObject

class ApprovalMaterialViewModel (private val apiService: ApiService) : ViewModel() {

    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _getMaterialAktivResponse = MutableLiveData<GetMaterialAktivasiResponse>()
    val getMaterialAktivasiResponse: LiveData<GetMaterialAktivasiResponse> = _getMaterialAktivResponse

    private val _aktivMaterialResponse = MutableLiveData<AktivasiSerialNumberResponse>()
    val aktivMaterialResponse: LiveData<AktivasiSerialNumberResponse> = _aktivMaterialResponse

    private val _detailMaterialRegistrasiResponse = MutableLiveData<GetMaterialRegistrasiDetailByDateResponse>()
    val detailMaterialRegistrasiResponse: LiveData<GetMaterialRegistrasiDetailByDateResponse> = _detailMaterialRegistrasiResponse

    fun getMaterialAktivasi(status: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getMaterialAktivasi(status)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    val materialAktivasiResponse = response.body()
                    _getMaterialAktivResponse.postValue(materialAktivasiResponse)
                } else {
                    _isLoading.postValue(false)
                    val error = response.errorBody()?.string()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun setAktivasiMaterial(sn: ArrayList<String>) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val requestBody = RequestBodyAktivMaterial(sn)
            val response = apiService.aktivasiMaterial(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    val aktivMaterialResponse = response.body()
                    _aktivMaterialResponse.postValue(aktivMaterialResponse)
                } else {
                    _isLoading.postValue(false)
                    val error = response.errorBody()?.string()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun getMaterialRegistrasiDetailByDate(tgl_registrasi: String, status: String) {
        _isLoading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getMaterialRegistrasiDetailByDate(tgl_registrasi, status)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    val registrasiDetailByDateResponse = response.body()
                    _detailMaterialRegistrasiResponse.postValue(registrasiDetailByDateResponse)
                } else {
                    _isLoading.postValue(false)
                    val error = response.errorBody()?.string()
                    onError("Error : ${error?.let { getErrorMessage(it) }}")
                }
            }
        }
    }

    fun onError(message: String) {
        _errorMessage.postValue(message)
        _isLoading.postValue(false)
    }

    fun getErrorMessage(raw: String): String {
        val obj = JSONObject(raw)
        return obj.getString("message")
    }
}