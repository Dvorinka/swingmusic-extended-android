package com.android.swingmusic.library.presentation.screen.destinations

import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph

@ExternalModuleGraph
@NavHostGraph
object LibraryGraph : NavGraphSpec

@NavHostGraph(LibraryGraph::class)
object LibraryScreenDestination : DestinationSpec {
    override val route = "library"
    override val arguments = emptyList()
    
    fun NavHostController.navigateToLibrary() {
        this.navigate(LibraryScreenDestination.route)
    }
}
