package com.kobir.iptv.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Player : Screen("player/{channelId}") {
        fun createRoute(channelId: Long) = "player/$channelId"
    }
    data object EPG : Screen("epg")
    data object Setup : Screen("setup")
}
