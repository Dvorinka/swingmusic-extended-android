package com.android.swingmusic.presentation.navigator

import androidx.annotation.DrawableRes
import com.android.swingmusic.album.presentation.screen.destinations.AllAlbumScreenDestination
import com.android.swingmusic.artist.presentation.screen.destinations.AllArtistsScreenDestination
import com.android.swingmusic.folder.presentation.screen.destinations.FoldersAndTracksScreenDestination
import com.android.swingmusic.search.presentation.screen.destinations.SearchScreenDestination
import com.android.swingmusic.home.presentation.screen.destinations.HomeScreenDestination
import com.android.swingmusic.library.presentation.screen.destinations.LibraryScreenDestination
import com.android.swingmusic.download.presentation.screen.destinations.DownloadScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.android.swingmusic.uicomponent.R as UiComponent

sealed class BottomNavItem(
    var title: String,
    @param:DrawableRes var icon: Int,
    var destination: DestinationSpec<*>
) {
    data object Home : BottomNavItem(
        title = "Home",
        icon = UiComponent.drawable.ic_home,
        destination = HomeScreenDestination
    )

    data object Search : BottomNavItem(
        title = "Search",
        icon = UiComponent.drawable.ic_search,
        destination = SearchScreenDestination
    )

    data object Library : BottomNavItem(
        title = "Your Library",
        icon = UiComponent.drawable.ic_library,
        destination = LibraryScreenDestination
    )

    data object Downloads : BottomNavItem(
        title = "Downloads",
        icon = UiComponent.drawable.ic_download,
        destination = DownloadScreenDestination
    )

    // Legacy items - can be accessed through Library
    data object Folder : BottomNavItem(
        title = "Folders",
        icon = UiComponent.drawable.folder_filled,
        destination = FoldersAndTracksScreenDestination
    )

    data object Album : BottomNavItem(
        title = "Albums",
        icon = UiComponent.drawable.ic_album,
        destination = AllAlbumScreenDestination
    )

    data object Artist : BottomNavItem(
        title = "Artists",
        icon = UiComponent.drawable.ic_artist,
        destination = AllArtistsScreenDestination
    )
}
