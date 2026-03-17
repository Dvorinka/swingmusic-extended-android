package com.android.swingmusic.download.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.swingmusic.uicomponent.components.*
import com.android.swingmusic.uicomponent.theme.*
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.download.presentation.viewmodel.DownloadViewModel
import com.android.swingmusic.download.presentation.viewmodel.DownloadUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(
    viewModel: DownloadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SwingMusicTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SwingMusicSpacing.MD)
        ) {
            // Header with gradient background
            SwingMusicGradientCard(
                title = "Downloads",
                subtitle = if (uiState.downloads.isNotEmpty()) {
                    "${uiState.downloads.count { it.status == DownloadUiState.DownloadStatus.DOWNLOADING }} active • ${uiState.downloads.count { it.status == DownloadUiState.DownloadStatus.COMPLETED }} completed"
                } else {
                    "Manage your music downloads"
                },
                icon = Icons.Default.Download,
                onClick = null
            )

            Spacer(modifier = Modifier.height(SwingMusicSpacing.LG))

            // Connection Status Card
            SwingMusicCard(
                title = if (uiState.isConnectedToWebApp) "Web App Connected" else "Connect to Web App",
                subtitle = if (uiState.isConnectedToWebApp) {
                    "Syncing with ${uiState.webAppUrl}"
                } else {
                    "Access your library and sync downloads"
                },
                icon = if (uiState.isConnectedToWebApp) Icons.Default.CloudDone else Icons.Default.CloudQueue,
                onClick = { /* Open connection screen */ },
                backgroundColor = if (uiState.isConnectedToWebApp) SwingMusicColors.Success else SwingMusicColors.SurfaceVariant,
                contentColor = if (uiState.isConnectedToWebApp) SwingMusicColors.OnAccent else SwingMusicColors.OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(SwingMusicSpacing.LG))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD)
            ) {
                SwingMusicButton(
                    text = "Clear Completed",
                    onClick = { viewModel.clearCompleted() },
                    variant = ButtonVariant.Secondary,
                    icon = Icons.Default.ClearAll,
                    modifier = Modifier.weight(1f)
                )
                
                SwingMusicButton(
                    text = "Pause All",
                    onClick = { viewModel.pauseAll() },
                    variant = ButtonVariant.Secondary,
                    icon = Icons.Default.Pause,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(SwingMusicSpacing.LG))

            // Downloads List
            if (uiState.downloads.isEmpty()) {
                EmptyDownloadsState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD)
                ) {
                    items(uiState.downloads) { download ->
                        EnhancedDownloadItem(
                            download = download,
                            onPause = { viewModel.pauseDownload(download.id) },
                            onResume = { viewModel.resumeDownload(download.id) },
                            onCancel = { viewModel.cancelDownload(download.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyDownloadsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SwingMusicSpacing.XL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SwingMusicGradientCard(
            title = "No Downloads Yet",
            subtitle = "Start downloading music from URLs or connect to your web app",
            icon = Icons.Default.Download,
            onClick = { /* Open download dialog */ },
            gradientColors = listOf(SwingMusicColors.Primary, SwingMusicColors.Secondary)
        )

        Spacer(modifier = Modifier.height(SwingMusicSpacing.XL))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD),
            verticalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD)
        ) {
            item {
                SwingMusicCard(
                    title = "Quick Download",
                    subtitle = "Download from URL",
                    icon = Icons.Default.Link,
                    onClick = { /* Open URL dialog */ }
                )
            }
            
            item {
                SwingMusicCard(
                    title = "Browse Library",
                    subtitle = "Browse web library",
                    icon = Icons.Default.LibraryMusic,
                    onClick = { /* Open library */ }
                )
            }
            
            item {
                SwingMusicCard(
                    title = "Import Local",
                    subtitle = "Import local files",
                    icon = Icons.Default.FolderOpen,
                    onClick = { /* Open file picker */ }
                )
            }
            
            item {
                SwingMusicCard(
                    title = "Connect Web App",
                    subtitle = "Sync with web app",
                    icon = Icons.Default.CloudQueue,
                    onClick = { /* Open connection */ }
                )
            }
        }
    }
}

@Composable
private fun EnhancedDownloadItem(
    download: DownloadUiState.DownloadItem,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    SwingMusicCard(
        title = download.title,
        subtitle = download.artist,
        icon = when (download.status) {
            DownloadUiState.DownloadStatus.DOWNLOADING -> Icons.Default.Downloading
            DownloadUiState.DownloadStatus.PAUSED -> Icons.Default.PauseCircle
            DownloadUiState.DownloadStatus.COMPLETED -> Icons.Default.CheckCircle
            DownloadUiState.DownloadStatus.FAILED -> Icons.Default.Error
            else -> Icons.Default.MusicNote
        },
        onClick = null,
        backgroundColor = when (download.status) {
            DownloadUiState.DownloadStatus.DOWNLOADING -> SwingMusicColors.PrimaryContainer
            DownloadUiState.DownloadStatus.COMPLETED -> SwingMusicColors.Success.copy(alpha = 0.1f)
            DownloadUiState.DownloadStatus.FAILED -> SwingMusicColors.Error.copy(alpha = 0.1f)
            else -> SwingMusicColors.Surface
        },
        contentColor = when (download.status) {
            DownloadUiState.DownloadStatus.DOWNLOADING -> SwingMusicColors.OnPrimaryContainer
            DownloadUiState.DownloadStatus.COMPLETED -> SwingMusicColors.Success
            DownloadUiState.DownloadStatus.FAILED -> SwingMusicColors.Error
            else -> SwingMusicColors.OnSurface
        },
        elevation = if (download.status == DownloadUiState.DownloadStatus.DOWNLOADING) 4 else 2
    ) {
        Column {
            // Progress bar
            SwingMusicProgressIndicator(
                progress = download.progress,
                label = null,
                showPercentage = false,
                modifier = Modifier.padding(bottom = SwingMusicSpacing.SM)
            )

            // Status and size info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (download.status) {
                        DownloadUiState.DownloadStatus.DOWNLOADING -> "Downloading..."
                        DownloadUiState.DownloadStatus.PAUSED -> "Paused"
                        DownloadUiState.DownloadStatus.COMPLETED -> "Completed"
                        DownloadUiState.DownloadStatus.FAILED -> "Failed"
                        else -> "Pending"
                    },
                    style = SwingMusicTypography.LabelSmall,
                    color = SwingMusicColors.OnSurfaceVariant
                )

                Text(
                    text = "${(download.progress * 100).toInt()}% • ${download.size}",
                    style = SwingMusicTypography.LabelSmall,
                    color = SwingMusicColors.OnSurfaceVariant
                )
            }

            // Action buttons
            if (download.status != DownloadUiState.DownloadStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(SwingMusicSpacing.SM))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SwingMusicSpacing.SM)
                ) {
                    when (download.status) {
                        DownloadUiState.DownloadStatus.DOWNLOADING -> {
                            SwingMusicButton(
                                text = "Pause",
                                onClick = onPause,
                                variant = ButtonVariant.Secondary,
                                icon = Icons.Default.Pause,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        DownloadUiState.DownloadStatus.PAUSED -> {
                            SwingMusicButton(
                                text = "Resume",
                                onClick = onResume,
                                variant = ButtonVariant.Primary,
                                icon = Icons.Default.PlayArrow,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        else -> {}
                    }

                    SwingMusicButton(
                        text = "Cancel",
                        onClick = onCancel,
                        variant = ButtonVariant.Ghost,
                        icon = Icons.Default.Close,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(SwingMusicSpacing.SM))
                
                SwingMusicButton(
                    text = "Open File",
                    onClick = { /* Open file */ },
                    variant = ButtonVariant.Primary,
                    icon = Icons.Default.FolderOpen,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
