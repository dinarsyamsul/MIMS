package dev.iconpln.mims.ui.monitoring

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosDao
import dev.iconpln.mims.data.local.database.TPosDetail
import dev.iconpln.mims.utils.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import java.time.LocalDate
import java.util.Date

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

    fun getPoFilter(daoSession: DaoSession, noPo: String, noDo: String, startDate: String, endDate: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true
                var endDateAdjust = endDate

                if (endDate.isNullOrEmpty()){
                    endDateAdjust = LocalDateTime.now().toString(Config.DATE)
                }
                val listMonitoring = daoSession.tPosDao.queryBuilder()
                    .where(TPosDao.Properties.PoMpNo.like("%"+ noPo +"%"))
                    .where(TPosDao.Properties.NoDoSmar.like("%"+ noDo +"%"))
                    .where(TPosDao.Properties.CreatedDate.between(startDate,endDateAdjust))
                    .list()
                _monitoringPOResponse.postValue(listMonitoring)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }
}