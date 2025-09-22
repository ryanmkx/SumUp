package com.example.sumup.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumup.data.repository.SummaryItem
import com.example.sumup.data.repository.SummaryRepository
import com.example.sumup.data.repository.UserAuthRepository
import com.example.sumup.businessLogic.GetUserSummaries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val authRepo: UserAuthRepository,
    private val summaryRepo: SummaryRepository
) : ViewModel() {

    private val _summaries = MutableStateFlow<List<SummaryItem>>(emptyList())
    val summaries: StateFlow<List<SummaryItem>> = _summaries.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val getUserSummariesUseCase = GetUserSummaries(authRepo, summaryRepo)

    init {
        loadSummaries()
    }

    fun loadSummaries() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getUserSummariesUseCase.execute()
                .onSuccess { summariesList ->
                    _summaries.value = summariesList
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load summaries"
                }

            _isLoading.value = false
        }
    }

    fun refreshSummaries() {
        loadSummaries()
    }
}