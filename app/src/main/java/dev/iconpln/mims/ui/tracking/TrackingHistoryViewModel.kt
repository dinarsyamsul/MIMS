package dev.iconpln.mims.ui.tracking

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.remote.response.*
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackingHistoryViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _trackingResponse = MutableLiveData<TrackingHistoryResponse>()
    val trackingResponse: LiveData<TrackingHistoryResponse> = _trackingResponse

    private val _detailTrackingHistoryResponse = MutableLiveData<DetailTrackingHistoryResponse>()
    val detailTrackingHistoryResponse: LiveData<DetailTrackingHistoryResponse> = _detailTrackingHistoryResponse

    fun getTrackingHistory(sn: String, ctx: Context) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getTrackingHistory(sn)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val loginResult = response.body()
                        _trackingResponse.postValue(loginResult)
                    } else {
                        _isLoading.value = false
                    }
                }catch (e: Exception){
                    Log.d("checkLogin", e.toString())
                }
            }
        }
    }

    fun getDetailTrackingHistory(sn: String, noTransaksi: String, status: String, ctx: Context) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getDetailTrackingHistory(sn, noTransaksi, status)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val loginResult = response.body()
                        _detailTrackingHistoryResponse.postValue(loginResult)
                    } else {
                        _isLoading.value = false
                    }
                }catch (e: Exception){
                    Log.d("checkLogin", e.toString())
                }
            }
        }
    }
}