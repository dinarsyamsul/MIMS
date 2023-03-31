package dev.iconpln.mims.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PemakaianUlpMandiriData(
    var noMaterial: String = "",
    var namaMaterial: String = "",
    var valuationType: String = "",
    var satuan: String = "",
    var jumlahReservasi: String = "",
    var noMeter: String = "",
    var jumlahPemakaian: String = ""

):Parcelable