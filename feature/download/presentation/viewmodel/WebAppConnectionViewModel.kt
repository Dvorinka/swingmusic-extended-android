package com.android.swingmusic.download.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.download.domain.repository.WebAppConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebAppConnectionViewModel @Inject constructor(
    private val webAppConnectionRepository: WebAppConnectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WebAppConnectionUiState())
    val uiState: StateFlow<WebAppConnectionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            webAppConnectionRepository.connectionState.collect { connectionState ->
                _uiState.value = _uiState.value.copy(
                    isConnected = connectionState.isConnected,
                    webAppUrl = if (connectionState.url.isNotEmpty()) connectionState.url else _uiState.value.webAppUrl,
                    pairingCode = connectionState.pairingCode,
                    isLoading = false
                )
            }
        }
    }

    fun updateWebAppUrl(url: String) {
        _uiState.value = _uiState.value.copy(webAppUrl = url)
    }

    fun generatePairingCode() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val code = webAppConnectionRepository.generatePairingCode()
                _uiState.value = _uiState.value.copy(
                    pairingCode = code,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun connectToWebApp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                webAppConnectionRepository.connect()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun validatePairingCode(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val isValid = webAppConnectionRepository.validatePairingCode(code)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isPairingCodeValid = isValid,
                    error = if (!isValid) "Invalid pairing code" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}

data class WebAppConnectionUiState(
    val isConnected: Boolean = false,
    val webAppUrl: String = "http://localhost:6081",
    val pairingCode: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPairingCodeValid: Boolean? = null
)
