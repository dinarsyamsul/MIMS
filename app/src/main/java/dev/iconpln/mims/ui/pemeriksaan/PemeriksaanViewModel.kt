package dev.iconpln.mims.ui.pemeriksaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.*
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
        val listPemeriksaan = daoSession.tPemeriksaanDao.loadAll()

        _pemeriksaanResponse.postValue(listPemeriksaan)
    }
}