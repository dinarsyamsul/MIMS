package dev.iconpln.mims.ui.monitoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MonitoringViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _monitoringPOResponse = MutableLiveData<List<TPos>>()
    val monitoringPOResponse: LiveData<List<TPos>> = _monitoringPOResponse

    private val _detailMonitoringPOResponse = MutableLiveData<List<TPosDetail>>()
    val detailMonitoringPOResponse: LiveData<List<TPosDetail>> = _detailMonitoringPOResponse

    fun getPo(listMonitoring: List<TPos>){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                _monitoringPOResponse.postValue(listMonitoring)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun getPoByDo(listMonitoring: List<TPosDetail>,noDo: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                _detailMonitoringPOResponse.postValue(listMonitoring)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun getFileterPoDetail(listMonitoring: List<TPosDetail>,noMat: String, noPackage: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (noMat.isNullOrEmpty() && noPackage.isNullOrEmpty()){
                    _detailMonitoringPOResponse.postValue(listMonitoring)
                }
                else if (noMat.isNullOrEmpty() && noPackage.isNotEmpty()){
                    val listFilter = listMonitoring.filter {
                        it.noPackaging.lowercase().contains(noPackage.lowercase())
                    }
                    _detailMonitoringPOResponse.postValue(listFilter)
                }
                else if (noMat.isNotEmpty() && noPackage.isNullOrEmpty()){
                    val listFilter = listMonitoring.filter {
                        it.noMatSap.lowercase().contains(noMat.lowercase())
                    }
                    _detailMonitoringPOResponse.postValue(listFilter)
                }
                else if (noMat.isNotEmpty() && noPackage.isNotEmpty()){
                    val listFilter = listMonitoring.filter {
                        it.noPackaging.lowercase().contains(noPackage.lowercase()) &&
                        it.noMatSap.lowercase().contains(noMat.lowercase())
                    }
                    _detailMonitoringPOResponse.postValue(listFilter)
                }

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun getPoFilter(listMonitoring: List<TPos>, noPo: String, noDo: String, startDate: String, endDate: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (noPo.isNotEmpty() && noDo.isNullOrEmpty() && startDate.isNullOrEmpty() && endDate.isNullOrEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.poMpNo.lowercase().contains(noPo.lowercase())
                    }
                    _monitoringPOResponse.postValue(listFilter)

                }
                else if (noPo.isNotEmpty() && noDo.isNullOrEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.poMpNo.lowercase().contains(noPo.lowercase())&&
                        it.createdDate.lowercase().contains(startDate.lowercase()) &&
                        it.createdDate.lowercase().contains(endDate.lowercase())
                    }
                    _monitoringPOResponse.postValue(listFilter)

                }
                else if (noPo.isNotEmpty() && noDo.isNotEmpty() && startDate.isNullOrEmpty() && endDate.isNullOrEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.poMpNo.lowercase().contains(noPo.lowercase())&&
                        it.noDoSmar.lowercase().contains(noDo.lowercase())

                    }
                    _monitoringPOResponse.postValue(listFilter)

                }
                else if (noPo.isNullOrEmpty() && noDo.isNotEmpty() && startDate.isNullOrEmpty() && endDate.isNullOrEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.noDoSmar.lowercase().contains(noDo.lowercase())
                    }
                    _monitoringPOResponse.postValue(listFilter)
                }
                else if (noPo.isNullOrEmpty() && noDo.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.noDoSmar.lowercase().contains(noDo.lowercase())&&
                        it.createdDate.lowercase().contains(startDate.lowercase()) &&
                        it.createdDate.lowercase().contains(endDate.lowercase())

                    }
                    _monitoringPOResponse.postValue(listFilter)
                }
                else if (noPo.isNullOrEmpty() && noDo.isNullOrEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.createdDate.lowercase().contains(startDate.lowercase())
                        && it.createdDate.lowercase().contains(endDate.lowercase())
                    }
                    _monitoringPOResponse.postValue(listFilter)
                }
                else if (noPo.isNotEmpty() && noDo.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()){
                    var listFilter = listMonitoring.filter {
                        it.createdDate.lowercase().contains(startDate.lowercase()) &&
                        it.createdDate.lowercase().contains(endDate.lowercase()) &&
                        it.noDoSmar.lowercase().contains(noDo.lowercase()) &&
                        it.poMpNo.lowercase().contains(noPo.lowercase())
                    }
                    _monitoringPOResponse.postValue(listFilter)
                }

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }
}