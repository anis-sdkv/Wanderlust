package com.wanderlust.ui.navigation

import com.wanderlust.ui.R


sealed class BottomNavigationItem(var graph: String, var icon: Int, var title: String) {

    object Home : BottomNavigationItem(
        "home_graph",
        R.drawable.baseline_format_list_bulleted_24,
        "Home"
    )

    object Map : BottomNavigationItem(
        "map_graph",
        R.drawable.baseline_map_24,
        "Map"
    )

    object Notifications : BottomNavigationItem(
        "notifications_graph",
        R.drawable.baseline_notifications_none_24,
        "Notifications"
    )

    object Profile : BottomNavigationItem(
        "profile_graph",
        R.drawable.baseline_person_outline_24,
        "Profile"
    )
}
