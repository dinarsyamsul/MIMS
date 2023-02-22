package dev.iconpln.mims.ui.monitoring_permintaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MonitoringPermintaanViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _monitoringPermintaanResponse = MutableLiveData<List<TMonitoringPermintaan>>()
    val monitoringPermintaanResponse: LiveData<List<TMonitoringPermintaan>> = _monitoringPermintaanResponse

    private val _monitoringPermintaanDetailResponse = MutableLiveData<List<TMonitoringPermintaanDetail>>()
    val monitoringPermintaanDetailResponse: LiveData<List<TMonitoringPermintaanDetail>> = _monitoringPermintaanDetailResponse

    fun getMonitoringPermintaan(daoSession: DaoSession){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                val lisMonitoring = daoSession.tMonitoringPermintaanDao.loadAll()

                _monitoringPermintaanResponse.postValue(lisMonitoring)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun getMonitoringPermintaanDetail(daoSession: DaoSession, noPermintaan: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                val listMonitoringDetail = daoSession.tMonitoringPermintaanDetailDao.queryBuilder()
                    .where(TMonitoringPermintaanDetailDao.Properties.NoPermintaan.eq(noPermintaan)).list()

                _monitoringPermintaanDetailResponse.postValue(listMonitoringDetail)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun searchDetail(daoSession: DaoSession, namaMaterial: String){
        var listDetail = daoSession.tMonitoringPermintaanDetailDao.queryBuilder()
            .where(TMonitoringPermintaanDetailDao.Properties.NomorMaterial.like("%" + namaMaterial + "%")).list()
        _monitoringPermintaanDetailResponse.postValue(listDetail)
    }

    fun search(
        daoSession: DaoSession,
        srcNoPermintaanText: String,
        srcStatusPengeluaranText: String,
        srcGudangAsalText: String,
        srcTglPermintaanText: String
    ){
        var listDataMonitoring = daoSession.tMonitoringPermintaanDao.queryBuilder().where(
            TMonitoringPermintaanDao.Properties.NoPermintaan.like("%" + srcNoPermintaanText + "%"),
            TMonitoringPermintaanDao.Properties.KodePengeluaran.like("%" + srcStatusPengeluaranText + "%"),
            TMonitoringPermintaanDao.Properties.StorLocAsalName.like("%" + srcGudangAsalText + "%"),
            TMonitoringPermintaanDao.Properties.TanggalPermintaan.like("%" + srcTglPermintaanText + "%")).list()

        _monitoringPermintaanResponse.postValue(listDataMonitoring)
    }
}