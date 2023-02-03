package dev.iconpln.mims.ui.pemeriksaan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDao
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.data.local.database.TPosSnsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PemeriksaanViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pemeriksaanResponse = MutableLiveData<List<TPemeriksaan>>()
    val pemeriksaanResponse: LiveData<List<TPemeriksaan>> = _pemeriksaanResponse

    private val _pemeriksaanDetailResponse = MutableLiveData<List<TPemeriksaanDetail>>()
    val pemeriksaanDetailResponse: LiveData<List<TPemeriksaanDetail>> = _pemeriksaanDetailResponse

    fun getPemeriksaan(daoSession: DaoSession){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true
                val listPemeriksaan = daoSession.tPemeriksaanDao.queryBuilder().list()
                _pemeriksaanResponse.postValue(listPemeriksaan)
                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun setDataDetailPemeriksaan(daoSession: DaoSession,listPemeriksaan: List<TPemeriksaanDetail>,noPemeriksaan: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true
                val pemeriksaan = daoSession.tPemeriksaanDao.queryBuilder().where(TPemeriksaanDao.Properties.NoPemeriksaan.eq(noPemeriksaan)).limit(1).unique()
//                var listSns = daoSession.tPosSnsDao.queryBuilder().where(TPosSnsDao.Properties.NoDoSmar.eq(noDo)).list()

                Log.d("checkPemerikssanDetail", "${listPemeriksaan.size}")
                if (!listPemeriksaan.isNotEmpty()){
                    var listPackagings = pemeriksaan.packangings.split(",")
                    for (i in listPackagings){
                        var listSns = daoSession.tPosSnsDao.queryBuilder().where(TPosSnsDao.Properties.NoPackaging.eq(i)).list()
                        if (listSns.size > 0){
                            for (h in listSns){
                                Log.d("testInser", h.noSerial)
                                var item = TPemeriksaanDetail()
                                item.sn = h.noSerial
                                item.noDoSmar = pemeriksaan.noDoSmar
                                item.noMaterail = pemeriksaan.materialGroup
                                item.noPackaging = i
                                item.status = pemeriksaan.doStatus
                                item.noPemeriksaan = pemeriksaan.noPemeriksaan
                                item.isDone = 0

                                daoSession.tPemeriksaanDetailDao.insert(item)
                            }
                        }
                    }
                }

            }catch (e: Exception){
                _isLoading.value = false
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }
}