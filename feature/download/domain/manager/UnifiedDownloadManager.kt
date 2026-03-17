package com.android.swingmusic.download.domain.manager

import com.android.swingmusic.download.domain.model.Download
import com.android.swingmusic.download.domain.repository.DownloadRepository
import com.android.swingmusic.download.domain.repository.WebAppConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnifiedDownloadManager @Inject constructor(
    private val downloadRepository: DownloadRepository,
    private val webAppConnectionRepository: WebAppConnectionRepository
) {
    
    private val _syncState = MutableStateFlow(SyncState.IDLE)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()
    
    // Get all downloads with unified state
    fun getAllDownloads(): Flow<List<UnifiedDownloadItem>> {
        return downloadRepository.getAllDownloads().map { downloads ->
            downloads.map { it.toUnifiedItem() }
        }
    }
    
    // Get active downloads
    fun getActiveDownloads(): Flow<List<UnifiedDownloadItem>> {
        return downloadRepository.getAllDownloads().map { downloads ->
            downloads.filter { it.status == Download.Status.DOWNLOADING || it.status == Download.Status.PAUSED }
                .map { it.toUnifiedItem() }
        }
    }
    
    // Get completed downloads
    fun getCompletedDownloads(): Flow<List<UnifiedDownloadItem>> {
        return downloadRepository.getAllDownloads().map { downloads ->
            downloads.filter { it.status == Download.Status.COMPLETED }
                .map { it.toUnifiedItem() }
        }
    }
    
    // Start download with web app sync
    suspend fun startDownload(
        url: String,
        quality: String = "high",
        syncWithWebApp: Boolean = true
    ): Result<String> {
        return try {
            val downloadId = downloadRepository.addDownload(url)
            
            if (syncWithWebApp && webAppConnectionRepository.connectionState.value.isConnected) {
                syncDownloadWithWebApp(downloadId, url, quality)
            }
            
            Result.success(downloadId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Pause download
    suspend fun pauseDownload(downloadId: String) {
        downloadRepository.pauseDownload(downloadId)
        notifyWebAppDownloadStatusChange(downloadId, "paused")
    }
    
    // Resume download
    suspend fun resumeDownload(downloadId: String) {
        downloadRepository.resumeDownload(downloadId)
        notifyWebAppDownloadStatusChange(downloadId, "downloading")
    }
    
    // Cancel download
    suspend fun cancelDownload(downloadId: String) {
        downloadRepository.cancelDownload(downloadId)
        notifyWebAppDownloadStatusChange(downloadId, "cancelled")
    }
    
    // Sync all downloads with web app
    suspend fun syncAllWithWebApp(): Result<List<String>> {
        return try {
            _syncState.value = SyncState.SYNCING
            
            val downloads = downloadRepository.getAllDownloads().map { it.toUnifiedItem() }
            val syncedIds = mutableListOf<String>()
            
            downloads.forEach { download ->
                if (syncDownloadWithWebApp(download.id, download.url, download.quality)) {
                    syncedIds.add(download.id)
                }
            }
            
            _syncState.value = SyncState.SYNCED
            Result.success(syncedIds)
        } catch (e: Exception) {
            _syncState.value = SyncState.ERROR
            Result.failure(e)
        }
    }
    
    // Clear completed downloads
    suspend fun clearCompleted() {
        downloadRepository.clearCompleted()
        notifyWebApp("downloads_cleared", emptyMap<String, Any>())
    }
    
    // Get download statistics
    fun getDownloadStats(): Flow<DownloadStats> {
        return downloadRepository.getAllDownloads().map { downloads ->
            DownloadStats(
                total = downloads.size,
                active = downloads.count { it.status == Download.Status.DOWNLOADING },
                paused = downloads.count { it.status == Download.Status.PAUSED },
                completed = downloads.count { it.status == Download.Status.COMPLETED },
                failed = downloads.count { it.status == Download.Status.FAILED },
                totalSize = downloads.sumOf { parseSize(it.size) }
            )
        }
    }
    
    // Private helper methods
    private suspend fun syncDownloadWithWebApp(
        downloadId: String,
        url: String,
        quality: String
    ): Boolean {
        return try {
            notifyWebApp("download_started", mapOf(
                "downloadId" to downloadId,
                "url" to url,
                "quality" to quality,
                "timestamp" to System.currentTimeMillis()
            ))
            true
        } catch (e: Exception) {
            false
        }
    }
    
    private suspend fun notifyWebAppDownloadStatusChange(
        downloadId: String,
        status: String
    ) {
        notifyWebApp("download_status_changed", mapOf(
            "downloadId" to downloadId,
            "status" to status,
            "timestamp" to System.currentTimeMillis()
        ))
    }
    
    private suspend fun notifyWebApp(
        event: String,
        data: Map<String, Any>
    ) {
        // This would send the event to the web app via WebSocket or other connection
        // Implementation depends on the web app connection mechanism
    }
    
    private fun Download.toUnifiedItem(): UnifiedDownloadItem {
        return UnifiedDownloadItem(
            id = this.id,
            title = this.title,
            artist = this.artist,
            url = this.url,
            progress = this.progress,
            size = this.size,
            status = when (this.status) {
                Download.Status.PENDING -> UnifiedDownloadStatus.PENDING
                Download.Status.DOWNLOADING -> UnifiedDownloadStatus.DOWNLOADING
                Download.Status.PAUSED -> UnifiedDownloadStatus.PAUSED
                Download.Status.COMPLETED -> UnifiedDownloadStatus.COMPLETED
                Download.Status.FAILED -> UnifiedDownloadStatus.FAILED
            },
            quality = "high", // This would come from download metadata
            speed = "0 KB/s", // This would be calculated
            eta = "Calculating...", // This would be calculated
            createdAt = this.createdAt,
            completedAt = this.completedAt
        )
    }
    
    private fun parseSize(sizeStr: String): Long {
        // Parse size string like "12.5 MB" to bytes
        return try {
            val parts = sizeStr.split(" ")
            if (parts.size == 2) {
                val value = parts[0].toDouble()
                val unit = parts[1].uppercase()
                when (unit) {
                    "KB" -> (value * 1024).toLong()
                    "MB" -> (value * 1024 * 1024).toLong()
                    "GB" -> (value * 1024 * 1024 * 1024).toLong()
                    else -> 0L
                }
            } else {
                0L
            }
        } catch (e: Exception) {
            0L
        }
    }
}

// Unified data models
data class UnifiedDownloadItem(
    val id: String,
    val title: String,
    val artist: String,
    val url: String,
    val progress: Float,
    val size: String,
    val status: UnifiedDownloadStatus,
    val quality: String,
    val speed: String,
    val eta: String,
    val createdAt: Long,
    val completedAt: Long?
)

enum class UnifiedDownloadStatus {
    PENDING, DOWNLOADING, PAUSED, COMPLETED, FAILED
}

data class DownloadStats(
    val total: Int,
    val active: Int,
    val paused: Int,
    val completed: Int,
    val failed: Int,
    val totalSize: Long
)

enum class SyncState {
    IDLE, SYNCING, SYNCED, ERROR
}
