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
                    val listPos = daoSession.tPosSnsDao.queryBuilder().list()
                    val size = listPos.size
                    if (size > 0) {
                        val items = arrayOfNulls<TPosDetailPenerimaan>(size)
                        var item: TPosDetailPenerimaan
                        for ((i, model) in listPos.withIndex()){
                            item = TPosDetailPenerimaan()

                            item.noDoSmar = model.noDoSmar
                            item.qty = ""
                            item.kdPabrikan = model.kdPabrikan
                            item.doStatus = model.doStatus
                            item.noPackaging = model.noPackaging
                            item.serialNumber = model.noSerial
                            item.noMaterial = model.noMatSap
                            item.namaKategoriMaterial = model.namaKategoriMaterial
                            item.storLoc = model.storLoc
                            if (model.statusPenerimaan.isNullOrEmpty()) item.statusPenerimaan = "" else item.statusPenerimaan = model.statusPenerimaan
                            if (model.statusPemeriksaan.isNullOrEmpty()) item.statusPemeriksaan = "" else item.statusPemeriksaan = model.statusPemeriksaan
                            item.doLineItem = model?.doLineItem
                            item.isComplaint = 0
                            item.isChecked = 0
                            item.isDone = 0
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

    fun getPenerimaan(daoSession: DaoSession,penerimaans: List<TPosPenerimaan>){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (penerimaans.isNotEmpty()){
                    _penerimaanResponse.postValue(penerimaans)
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
                            item.tlskNo = model?.tlskNo
                            item.total = model?.total
                            item.kdPabrikan = model?.kdPabrikan
                            item.materialGroup = model?.materialGroup
                            item.namaKategoriMaterial = model?.namaKategoriMaterial
                            item.noDoMims = model?.noDoMims
                            item.total = model?.total
                            item.courierPersonName = model.courierPersonName
                            item.doLineItem = model.doLineItem

                            if (model.expeditions.isNullOrEmpty()){
                                item.expeditions = ""
                            }else{
                                item.expeditions = model.expeditions
                            }

                            if (model.tglDiterima.isNullOrEmpty()) item.tanggalDiterima = "" else item.tanggalDiterima = model.tglDiterima
                            if (model.petugasPenerima.isNullOrEmpty()) item.petugasPenerima = "" else item.petugasPenerima = model.petugasPenerima
                            if (model.kurirPengantar.isNullOrEmpty()) item.kurirPengantar = "" else item.kurirPengantar = model.kurirPengantar

                            item.statusPemeriksaan = if (model?.statusPemeriksaan.isNullOrEmpty()) "" else model?.statusPemeriksaan
                            item.statusPenerimaan = if (model?.statusPenerimaan.isNullOrEmpty()) "" else model?.statusPenerimaan

                            item.poDate = model.poDate
                            item.kodeStatusDoMims = model.kodeStatusDoMims
                            item.doStatus = model.doStatus
                            item.expeditions = model.expeditions
                            item.kdPabrikan = model.kdPabrikan
                            item.materialGroup = model.materialGroup
                            item.namaKategoriMaterial = model.namaKategoriMaterial
                            item.ratingPenerimaan = model.ratingResponse
                            item.descPenerimaan = ""
                            item.ratingQuality = model.ratingQuality
                            item.descQuality = ""
                            item.ratingWaktu = model.ratingDelivery
                            item.descWaktu = ""
                            item.nilaiRatingPenerimaan = ""
                            item.nilaiRatingQuality = ""
                            item.nilaiRatingWaktu = ""
                            if (model.ratingResponse.isNotEmpty() &&
                                model.ratingDelivery.isNotEmpty() &&
                                model.ratingQuality.isNotEmpty()) item.isDone = 1 else item.isDone = 0

                            item.isRating = 0
                            item.ratingDone = 0
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