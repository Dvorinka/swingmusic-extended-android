package com.android.swingmusic.home.presentation.screen.destinations

import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.annotation.ExternalModuleGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph

@ExternalModuleGraph
@NavHostGraph
object HomeGraph : NavGraphSpec

@NavHostGraph(HomeGraph::class)
object HomeScreenDestination : DestinationSpec {
    override val route = "home"
    override val arguments = emptyList()
    
    fun NavHostController.navigateToHome() {
        this.navigate(HomeScreenDestination.route)
    }
}
