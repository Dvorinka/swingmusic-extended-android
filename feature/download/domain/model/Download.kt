package com.android.swingmusic.download.domain.model

data class Download(
    val id: String,
    val title: String,
    val artist: String,
    val url: String,
    val progress: Float = 0f,
    val size: String = "0 MB",
    val status: Status = Status.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
) {
    enum class Status {
        PENDING, DOWNLOADING, PAUSED, COMPLETED, FAILED
    }
}
