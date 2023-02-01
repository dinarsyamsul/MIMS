package dev.iconpln.mims.ui.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RatingViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _ratingResponse = MutableLiveData<List<TPemeriksaan>>()
    val ratingResponse: LiveData<List<TPemeriksaan>> = _ratingResponse

    private val _ratingDetailResponse = MutableLiveData<List<TPemeriksaanDetail>>()
    val ratingDetailResponse: LiveData<List<TPemeriksaanDetail>> = _ratingDetailResponse


}