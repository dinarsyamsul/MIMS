package dev.iconpln.mims.ui.pemeriksaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
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

    fun setDataDetailPemeriksaan(daoSession: DaoSession,listPemeriksaan: List<TPemeriksaanDetail>,noDo: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (listPemeriksaan.isNotEmpty()){
                    _pemeriksaanDetailResponse.postValue(listPemeriksaan)
                }else{
                    val listPem = daoSession.tPemeriksaanDao.queryBuilder().list()
                    val size = listPem.size
                    if (size > 0) {
                        val items = arrayOfNulls<TPemeriksaanDetail>(size)
                        var item: TPemeriksaanDetail
                        for ((i, model) in listPem.withIndex()){
                            item = TPemeriksaanDetail()

                            item.noPemeriksaan = model.noPemeriksaan
                            item.sn = ""
                            item.noDoSmar = model.noDoSmar
                            item.noMaterail = model.materialGroup
                            item.noPackaging = model.packangings
                            item.status = model.doStatus
                            item.isDone = 0
                            items[i] = item
                        }
                        daoSession.tPemeriksaanDetailDao.insertInTx(items.toList())
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