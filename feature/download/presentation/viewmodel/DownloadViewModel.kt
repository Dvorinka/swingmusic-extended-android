package com.android.swingmusic.download.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.download.domain.usecase.*
import com.android.swingmusic.download.domain.model.Download
import com.android.swingmusic.download.domain.repository.WebAppConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadUseCase: DownloadUseCase,
    private val pauseDownloadUseCase: PauseDownloadUseCase,
    private val resumeDownloadUseCase: ResumeDownloadUseCase,
    private val cancelDownloadUseCase: CancelDownloadUseCase,
    private val clearCompletedUseCase: ClearCompletedUseCase,
    private val pauseAllUseCase: PauseAllUseCase,
    private val webAppConnectionRepository: WebAppConnectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DownloadUiState())
    val uiState: StateFlow<DownloadUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            downloadUseCase().collect { downloads ->
                _uiState.value = _uiState.value.copy(downloads = downloads.map { it.toUiState() })
            }
        }
        
        viewModelScope.launch {
            webAppConnectionRepository.connectionState.collect { connectionState ->
                _uiState.value = _uiState.value.copy(
                    isConnectedToWebApp = connectionState.isConnected,
                    webAppUrl = connectionState.url
                )
            }
        }
    }

    fun pauseDownload(downloadId: String) {
        viewModelScope.launch {
            pauseDownloadUseCase(downloadId)
        }
    }

    fun resumeDownload(downloadId: String) {
        viewModelScope.launch {
            resumeDownloadUseCase(downloadId)
        }
    }

    fun cancelDownload(downloadId: String) {
        viewModelScope.launch {
            cancelDownloadUseCase(downloadId)
        }
    }

    fun clearCompleted() {
        viewModelScope.launch {
            clearCompletedUseCase()
        }
    }

    fun pauseAll() {
        viewModelScope.launch {
            pauseAllUseCase()
        }
    }

    fun connectToWebApp() {
        viewModelScope.launch {
            webAppConnectionRepository.connect()
        }
    }

    fun disconnectFromWebApp() {
        viewModelScope.launch {
            webAppConnectionRepository.disconnect()
        }
    }
}

data class DownloadUiState(
    val downloads: List<DownloadItem> = emptyList(),
    val isConnectedToWebApp: Boolean = false,
    val webAppUrl: String = "",
    val isLoading: Boolean = false
) {
    data class DownloadItem(
        val id: String,
        val title: String,
        val artist: String,
        val progress: Float,
        val size: String,
        val status: DownloadStatus,
        val url: String
    )

    enum class DownloadStatus {
        PENDING, DOWNLOADING, PAUSED, COMPLETED, FAILED
    }
}

fun Download.toUiState(): DownloadUiState.DownloadItem {
    return DownloadUiState.DownloadItem(
        id = this.id,
        title = this.title,
        artist = this.artist,
        progress = this.progress,
        size = this.size,
        status = when (this.status) {
            Download.Status.PENDING -> DownloadUiState.DownloadStatus.PENDING
            Download.Status.DOWNLOADING -> DownloadUiState.DownloadStatus.DOWNLOADING
            Download.Status.PAUSED -> DownloadUiState.DownloadStatus.PAUSED
            Download.Status.COMPLETED -> DownloadUiState.DownloadStatus.COMPLETED
            Download.Status.FAILED -> DownloadUiState.DownloadStatus.FAILED
        },
        url = this.url
    )
}
