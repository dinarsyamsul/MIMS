package dev.iconpln.mims.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.iconpln.mims.data.remote.service.ApiService
import dev.iconpln.mims.ui.login.LoginViewModel
import dev.iconpln.mims.ui.scan.ScanViewModel

class ViewModelFactory(
    private val session: SessionManager,
    private val apiService: ApiService,
    private val networkStatusTracker: NetworkStatusTracker
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(session, apiService, networkStatusTracker) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                return ScanViewModel(apiService) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}