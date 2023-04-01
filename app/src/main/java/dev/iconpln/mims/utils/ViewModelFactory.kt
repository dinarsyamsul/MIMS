package dev.iconpln.mims.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.data.remote.service.ApiService
import dev.iconpln.mims.ui.pnerimaan.registrasi.RegistrasiMaterialViewModel

class ViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(RegistrasiMaterialViewModel::class.java) -> {
                return RegistrasiMaterialViewModel(apiService) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}