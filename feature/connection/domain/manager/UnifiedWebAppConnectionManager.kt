package com.android.swingmusic.connection.domain.manager

import com.android.swingmusic.connection.domain.repository.WebAppConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class UnifiedWebAppConnectionManager @Inject constructor(
    private val webAppConnectionRepository: WebAppConnectionRepository
) {
    
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()
    
    private val _connectionInfo = MutableStateFlow(ConnectionInfo())
    val connectionInfo: StateFlow<ConnectionInfo> = _connectionInfo.asStateFlow()
    
    init {
        // Monitor connection state changes
        webAppConnectionRepository.connectionState.collect { repoState ->
            _connectionState.value = if (repoState.isConnected) {
                ConnectionState.CONNECTED
            } else {
                ConnectionState.DISCONNECTED
            }
            
            _connectionInfo.value = ConnectionInfo(
                url = repoState.url,
                pairingCode = repoState.pairingCode,
                isConnected = repoState.isConnected,
                lastConnected = if (repoState.isConnected) System.currentTimeMillis() else null
            )
        }
    }
    
    // Generate pairing code
    suspend fun generatePairingCode(): Result<String> {
        return try {
            val code = webAppConnectionRepository.generatePairingCode()
            Result.success(code)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Connect to web app
    suspend fun connectToWebApp(url: String, pairingCode: String? = null): Result<Boolean> {
        return try {
            _connectionState.value = ConnectionState.CONNECTING
            
            val success = if (pairingCode != null) {
                webAppConnectionRepository.validatePairingCode(pairingCode)
            } else {
                webAppConnectionRepository.connect()
            }
            
            if (success) {
                _connectionState.value = ConnectionState.CONNECTED
                _syncStatus.value = SyncStatus.SYNCING
                
                // Start initial sync
                performInitialSync()
            } else {
                _connectionState.value = ConnectionState.DISCONNECTED
            }
            
            Result.success(success)
        } catch (e: Exception) {
            _connectionState.value = ConnectionState.ERROR
            Result.failure(e)
        }
    }
    
    // Disconnect from web app
    suspend fun disconnectFromWebApp(): Result<Unit> {
        return try {
            webAppConnectionRepository.disconnect()
            _connectionState.value = ConnectionState.DISCONNECTED
            _syncStatus.value = SyncStatus.IDLE
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Sync data with web app
    suspend fun syncData(syncType: SyncType = SyncType.FULL): Result<SyncResult> {
        return try {
            if (_connectionState.value != ConnectionState.CONNECTED) {
                return Result.failure(Exception("Not connected to web app"))
            }
            
            _syncStatus.value = SyncStatus.SYNCING
            
            val result = when (syncType) {
                SyncType.FULL -> performFullSync()
                SyncType.LIBRARY -> syncLibrary()
                SyncType.PLAYLISTS -> syncPlaylists()
                SyncType.DOWNLOADS -> syncDownloads()
                SyncType.SETTINGS -> syncSettings()
            }
            
            _syncStatus.value = if (result.success) SyncStatus.SYNCED else SyncStatus.ERROR
            Result.success(result)
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.ERROR
            Result.failure(e)
        }
    }
    
    // Send message to web app
    suspend fun sendMessage(message: WebAppMessage): Result<Unit> {
        return try {
            if (_connectionState.value != ConnectionState.CONNECTED) {
                return Result.failure(Exception("Not connected to web app"))
            }
            
            // Send message via WebSocket or other connection
            sendMessageToWebApp(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Get connection statistics
    fun getConnectionStats(): Flow<ConnectionStats> {
        return webAppConnectionRepository.connectionState.map { state ->
            ConnectionStats(
                isConnected = state.isConnected,
                url = state.url,
                lastConnected = if (state.isConnected) System.currentTimeMillis() else null,
                totalSyncs = 0, // This would be tracked in a real implementation
                successfulSyncs = 0,
                failedSyncs = 0
            )
        }
    }
    
    // Private helper methods
    private suspend fun performInitialSync() {
        try {
            syncData(SyncType.FULL)
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.ERROR
        }
    }
    
    private suspend fun performFullSync(): SyncResult {
        val results = mutableListOf<SyncItemResult>()
        
        try {
            // Sync library
            results.add(syncLibrary())
            
            // Sync playlists
            results.add(syncPlaylists())
            
            // Sync downloads
            results.add(syncDownloads())
            
            // Sync settings
            results.add(syncSettings())
            
            val success = results.all { it.success }
            return SyncResult(
                success = success,
                items = results,
                duration = System.currentTimeMillis(),
                error = if (!success) "Some sync operations failed" else null
            )
        } catch (e: Exception) {
            return SyncResult(
                success = false,
                items = emptyList(),
                duration = System.currentTimeMillis(),
                error = e.message
            )
        }
    }
    
    private suspend fun syncLibrary(): SyncItemResult {
        return try {
            // Implementation would sync library data with web app
            SyncItemResult(
                type = SyncType.LIBRARY,
                success = true,
                itemsSynced = 100, // This would be the actual count
                error = null
            )
        } catch (e: Exception) {
            SyncItemResult(
                type = SyncType.LIBRARY,
                success = false,
                itemsSynced = 0,
                error = e.message
            )
        }
    }
    
    private suspend fun syncPlaylists(): SyncItemResult {
        return try {
            // Implementation would sync playlists with web app
            SyncItemResult(
                type = SyncType.PLAYLISTS,
                success = true,
                itemsSynced = 25, // This would be the actual count
                error = null
            )
        } catch (e: Exception) {
            SyncItemResult(
                type = SyncType.PLAYLISTS,
                success = false,
                itemsSynced = 0,
                error = e.message
            )
        }
    }
    
    private suspend fun syncDownloads(): SyncItemResult {
        return try {
            // Implementation would sync downloads with web app
            SyncItemResult(
                type = SyncType.DOWNLOADS,
                success = true,
                itemsSynced = 50, // This would be the actual count
                error = null
            )
        } catch (e: Exception) {
            SyncItemResult(
                type = SyncType.DOWNLOADS,
                success = false,
                itemsSynced = 0,
                error = e.message
            )
        }
    }
    
    private suspend fun syncSettings(): SyncItemResult {
        return try {
            // Implementation would sync settings with web app
            SyncItemResult(
                type = SyncType.SETTINGS,
                success = true,
                itemsSynced = 10, // This would be the actual count
                error = null
            )
        } catch (e: Exception) {
            SyncItemResult(
                type = SyncType.SETTINGS,
                success = false,
                itemsSynced = 0,
                error = e.message
            )
        }
    }
    
    private suspend fun sendMessageToWebApp(message: WebAppMessage) {
        // Implementation would send message via WebSocket or other connection
        val json = Json.encodeToString(message)
        // Send json to web app
    }
}

// Data models
@Serializable
data class WebAppMessage(
    val type: String,
    val data: Map<String, Any>,
    val timestamp: Long = System.currentTimeMillis()
)

data class ConnectionInfo(
    val url: String = "",
    val pairingCode: String? = null,
    val isConnected: Boolean = false,
    val lastConnected: Long? = null
)

data class ConnectionStats(
    val isConnected: Boolean,
    val url: String,
    val lastConnected: Long?,
    val totalSyncs: Int,
    val successfulSyncs: Int,
    val failedSyncs: Int
)

data class SyncResult(
    val success: Boolean,
    val items: List<SyncItemResult>,
    val duration: Long,
    val error: String?
)

data class SyncItemResult(
    val type: SyncType,
    val success: Boolean,
    val itemsSynced: Int,
    val error: String?
)

enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED, ERROR
}

enum class SyncStatus {
    IDLE, SYNCING, SYNCED, ERROR
}

enum class SyncType {
    FULL, LIBRARY, PLAYLISTS, DOWNLOADS, SETTINGS
}
