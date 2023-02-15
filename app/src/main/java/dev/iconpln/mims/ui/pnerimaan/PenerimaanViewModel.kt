package dev.iconpln.mims.ui.pnerimaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class PenerimaanViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _penerimaanResponse = MutableLiveData<List<TPosPenerimaan>>()
    val penerimaanResponse: LiveData<List<TPosPenerimaan>> = _penerimaanResponse

    private val _penerimaanDetailResponse = MutableLiveData<List<TPosDetailPenerimaan>>()
    val penerimaanDetailResponse: LiveData<List<TPosDetailPenerimaan>> = _penerimaanDetailResponse

    fun insertDetailPenerimaan(daoSession: DaoSession, listDetailPenerimaan: List<TPosDetailPenerimaan>){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (listDetailPenerimaan.isNotEmpty()){
                    _penerimaanDetailResponse.postValue(listDetailPenerimaan)
                }else{
                    val listPos = daoSession.tPosDetailDao.queryBuilder().list()
                    val size = listPos.size
                    if (size > 0) {
                        val items = arrayOfNulls<TPosDetailPenerimaan>(size)
                        var item: TPosDetailPenerimaan
                        for ((i, model) in listPos.withIndex()){
                            item = TPosDetailPenerimaan()

                            item.noMatSap = model.noMatSap
                            item.noDoSmar = model.noDoSmar
                            item.qty = model.qty
                            item.kdPabrikan = model.kdPabrikan
                            item.doStatus = model.doStatus
                            item.poSapNo = model.poSapNo
                            item.poMpNo = model.poMpNo
                            item.noDoMims = model.noDoMims
                            item.noPackaging = model.noPackaging
                            item.plantCodeNo = model.plantCodeNo
                            item.plantName = model.plantName
                            item.storLoc = model.storLoc
                            item.leadTime = model.leadTime
                            item.createdDate = model.createdDate
                            item.uom = model.uom
                            item.barcode = model.noPackaging
                            if (model.noPemeriksaan.isNullOrEmpty()){
                                item.noPemeriksaan = ""
                            }else{
                                item.noPemeriksaan = model.noPemeriksaan
                            }
                            item.isChecked = 0
                            items[i] = item
                        }
                        daoSession.tPosDetailPenerimaanDao.insertInTx(items.toList())
                    }
                }
                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun getPenerimaan(daoSession: DaoSession,listMonitoring: List<TPosPenerimaan>){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (listMonitoring.isNotEmpty()){
                    _penerimaanResponse.postValue(listMonitoring)
                }else{
                    val listPos = daoSession.tPosDao.queryBuilder().list()
                    val size = listPos.size
                    if (size > 0) {
                        val items = arrayOfNulls<TPosPenerimaan>(size)
                        var item: TPosPenerimaan
                        for ((i, model) in listPos.withIndex()){
                            item = TPosPenerimaan()
                            item.createdDate = model?.createdDate
                            item.leadTime = model?.leadTime
                            item.storloc = model?.storLoc
                            item.noDoSmar = model?.noDoSmar
                            item.planCodeNo = model?.planCodeNo
                            item.plantName = model?.plantName
                            item.poMpNo = model?.poMpNo
                            item.poSapNo = model?.poSapNo
                            item.storLoc = model?.storLoc
                            item.tlskNo = model?.tlskNo
                            item.total = model?.total
                            item.kdPabrikan = model?.kdPabrikan
                            item.materialGroup = model?.materialGroup
                            item.namaKategoriMaterial = model?.namaKategoriMaterial
                            item.noDoMims = model?.noDoMims
                            item.total = model?.total
                            item.expeditions = model.expeditions
                            item.courierPersonName = model.courierPersonName
                            item.photoSuratBarang = ""
                            item.photoBarang = ""
                            item.tanggalDiterima = ""
                            item.petugasPenerima = ""
                            item.namaKurir = ""
                            item.namaEkspedisi = ""
                            item.isDone = 0
                            items[i] = item
                        }
                        daoSession.tPosPenerimaanDao.insertInTx(items.toList())
                    }
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