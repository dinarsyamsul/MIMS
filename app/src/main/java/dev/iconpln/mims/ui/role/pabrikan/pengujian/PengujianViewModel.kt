package dev.iconpln.mims.ui.role.pabrikan.pengujian

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.remote.response.PengujianMaterialPabrikanResponse
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.data.remote.service.ApiService
import kotlinx.coroutines.*
import org.json.JSONObject
import javax.inject.Inject


class PengujianViewModel : ViewModel() {

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pengujianResponse = MutableLiveData<PengujianMaterialPabrikanResponse>()
    val pengujianResponse: LiveData<PengujianMaterialPabrikanResponse> = _pengujianResponse

    fun getPengujian(noPengujian: String?, status: String?, pageIn: Int? = 1, pageSize: Int? = 5, ctx: Context) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService(ctx)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getPengujianMaterialPabrikan(noPengujian, status, pageIn, pageSize)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val dataResult = response.body()
                    _pengujianResponse.postValue(dataResult)
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