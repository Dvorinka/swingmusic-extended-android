package com.android.swingmusic.download.domain.repository

import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getAllDownloads(): Flow<List<com.android.swingmusic.download.domain.model.Download>>
    
    suspend fun addDownload(url: String): com.android.swingmusic.download.domain.model.Download
    suspend fun pauseDownload(downloadId: String)
    suspend fun resumeDownload(downloadId: String)
    suspend fun cancelDownload(downloadId: String)
    suspend fun clearCompleted()
    suspend fun pauseAll()
    suspend fun updateProgress(downloadId: String, progress: Float)
    suspend fun updateStatus(downloadId: String, status: com.android.swingmusic.download.domain.model.Download.Status)
}
