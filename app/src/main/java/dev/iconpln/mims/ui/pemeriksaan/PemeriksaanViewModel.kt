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

    fun setPemeriksaan(daoSession: DaoSession,listPem: List<TPemeriksaan>){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true

                if (listPem.isNotEmpty()){
                    _pemeriksaanResponse.postValue(listPem)
                }else{
                    val listPos = daoSession.tPosDao.queryBuilder().list()
                    val size = listPos.size
                    if (size > 0) {
                        val items = arrayOfNulls<TPemeriksaan>(size)
                        var item: TPemeriksaan
                        for ((i, model) in listPos.withIndex()){
                            item = TPemeriksaan()
                            item.noPemeriksaan = ""
                            item.storloc = model.storloc
                            item.total = model.total
                            item.tlskNo = model.tlskNo
                            item.poSapNo = model.poSapNo
                            item.poMpNo = model.poMpNo
                            item.noDoSmar = model.noDoSmar
                            item.leadTime = model.leadTime
                            item.createdDate = model.createdDate
                            item.planCodeNo = model.planCodeNo
                            item.plantName = model.plantName
                            item.noDoMims = model.noDoMims
                            item.doStatus = model.doStatus
                            item.expeditions = model.expeditions
                            item.courierPersonName = model.courierPersonName
                            item.kdPabrikan = model.kdPabrikan
                            item.materialGroup = model.materialGroup
                            item.namaKategoriMaterial = model.namaKategoriMaterial
                            item.tanggalDiterima = ""
                            item.petugasPenerima = ""
                            item.namaKurir = ""
                            item.namaEkspedisi = ""

                            item.namaKetua = ""
                            item.namaManager = ""
                            item.namaSekretaris = ""
                            item.anggota = ""


                            item.ratingPenerimaan = ""
                            item.descPenerimaan = ""
                            item.ratingQuality = ""
                            item.descQuality = ""
                            item.ratingWaktu = ""
                            item.descWaktu = ""
                            item.ratingPath = ""
                            item.packangings = ""

                            item.state = 0
                            item.isDone = 0
                            items[i] = item
                        }
                        daoSession.tPemeriksaanDao.insertInTx(items.toList())
                    }
                }

                _pemeriksaanResponse.postValue(daoSession.tPemeriksaanDao.loadAll())
                _isLoading.value = false
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }

        }
    }

    fun setDataDetailPemeriksaan(
        daoSession: DaoSession,
        noPem: String,
        noDo: String?
    ){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _isLoading.value = true
                val listPemDetail = daoSession.tPemeriksaanDetailDao.queryBuilder().where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(noPem)).list()
                val listSns = daoSession.tPosSnsDao.queryBuilder().where(TPosSnsDao.Properties.NoDoSmar.eq(noDo)).list()
                if (listPemDetail.isNotEmpty()){
                    _pemeriksaanDetailResponse.postValue(listPemDetail)
                }else{
                    val size = listSns.size
                    if (size > 0){
                        val items = arrayOfNulls<TPemeriksaanDetail>(size)
                        var item: TPemeriksaanDetail
                        for ((i, model) in listSns.withIndex()){
                            item = TPemeriksaanDetail()
                            item.noPemeriksaan = noPem
                            item.noDoSmar = model.noDoSmar
                            item.statusSn = ""
                            item.sn = model.noSerial
                            item.noPackaging = model.noPackaging
                            item.noMaterail = model.noMatSap
                            item.kategori = model.namaKategoriMaterial
                            item.isChecked = 0
                            item.isDone = 0

                            items[i] = item
                        }
                        daoSession.tPemeriksaanDetailDao.insertInTx(items.toList())
                    }
                    _pemeriksaanDetailResponse.postValue(
                        daoSession.tPemeriksaanDetailDao.queryBuilder()
                            .where(TPemeriksaanDao.Properties.NoPemeriksaan.eq(noPem)).list()
                    )
                }
                _isLoading.value = false
            }catch (e: Exception){
                _isLoading.value = false
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }
}