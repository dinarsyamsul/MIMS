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

    private val _monitoringPermintaanResponse = MutableLiveData<List<TTransMonitoringPermintaan>>()
    val monitoringPermintaanResponse: LiveData<List<TTransMonitoringPermintaan>> = _monitoringPermintaanResponse

    private val _monitoringPermintaanDetailResponse = MutableLiveData<List<TTransMonitoringPermintaanDetail>>()
    val monitoringPermintaanDetailResponse: LiveData<List<TTransMonitoringPermintaanDetail>> = _monitoringPermintaanDetailResponse

    fun getMonitoringPermintaan(daoSession: DaoSession){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                val lisMonitoring = daoSession.tTransMonitoringPermintaanDao.queryBuilder().list()

                _monitoringPermintaanResponse.postValue(lisMonitoring)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun getMonitoringPermintaanDetail(daoSession: DaoSession, noTransaksi: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                val listMonitoringDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
                    .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi)).list()

                _monitoringPermintaanDetailResponse.postValue(listMonitoringDetail)

                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun searchDetail(daoSession: DaoSession, namaMaterial: String, noTransaksi: String){
        var listDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
            .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(noTransaksi))
            .where(TTransMonitoringPermintaanDetailDao.Properties.NomorMaterial.like("%" + namaMaterial + "%")).list()
        _monitoringPermintaanDetailResponse.postValue(listDetail)
    }

    fun search(
        daoSession: DaoSession,
        srcNoPermintaanText: String,
        srcStatusPengeluaranText: String,
        srcGudangAsalText: String,
        srcTglPermintaanText: String
    ){
        var listDataMonitoring = daoSession.tTransMonitoringPermintaanDao.queryBuilder().where(
            TTransMonitoringPermintaanDao.Properties.NoPermintaan.like("%" + srcNoPermintaanText + "%"),
            TTransMonitoringPermintaanDao.Properties.KodePengeluaran.like("%" + srcStatusPengeluaranText + "%"),
            TTransMonitoringPermintaanDao.Properties.StorLocAsalName.like("%" + srcGudangAsalText + "%"),
            TTransMonitoringPermintaanDao.Properties.TanggalPermintaan.like("%" + srcTglPermintaanText + "%")).list()

        _monitoringPermintaanResponse.postValue(listDataMonitoring)
    }
}