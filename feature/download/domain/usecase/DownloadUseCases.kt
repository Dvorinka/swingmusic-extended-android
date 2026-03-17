package com.android.swingmusic.download.domain.usecase

import com.android.swingmusic.download.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(): Flow<List<com.android.swingmusic.download.domain.model.Download>> {
        return downloadRepository.getAllDownloads()
    }
}

class PauseDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(downloadId: String) {
        downloadRepository.pauseDownload(downloadId)
    }
}

class ResumeDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(downloadId: String) {
        downloadRepository.resumeDownload(downloadId)
    }
}

class CancelDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(downloadId: String) {
        downloadRepository.cancelDownload(downloadId)
    }
}

class ClearCompletedUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke() {
        downloadRepository.clearCompleted()
    }
}

class PauseAllUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke() {
        downloadRepository.pauseAll()
    }
}
