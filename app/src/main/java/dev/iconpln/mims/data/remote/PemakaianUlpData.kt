package dev.iconpln.mims.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PemakaianUlpData(
    var noReservasi: String = "",
    var statusReservasi: String = "",
    var jenisPekerjaan: String = "",
    var statusKirimSap: String = "",
    var sumberReservasi: String = "",
    var tglReservasi: String = ""

):Parcelable