package dev.iconpln.mims.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PenerimaanUlpData(
    var noPengiriman: String = "",
    var noPermintaan: String = "",
    var statusPemeriksaan: String = "",
    var deliveryDate: String = "",
    var statusPenerimaan: String = "",
    var jumlahKardus: String = ""

):Parcelable