package com.android.swingmusic.lyrics.presentation.screen.destinations

import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph

@ExternalModuleGraph
@NavHostGraph
object LyricsGraph : NavGraphSpec

@NavHostGraph(LyricsGraph::class)
object LyricsScreenDestination : DestinationSpec {
    override val route = "lyrics"
    override val arguments = emptyList()
    
    fun NavHostController.navigateToLyrics() {
        this.navigate(LyricsScreenDestination.route)
    }
}
