package com.android.swingmusic.download.domain.repository

import kotlinx.coroutines.flow.Flow

data class WebAppConnectionState(
    val isConnected: Boolean = false,
    val url: String = "",
    val pairingCode: String? = null
)

interface WebAppConnectionRepository {
    val connectionState: Flow<WebAppConnectionState>
    
    suspend fun connect()
    suspend fun disconnect()
    suspend fun generatePairingCode(): String
    suspend fun validatePairingCode(code: String): Boolean
}
