package com.example.wanderlust.navigation

import com.example.wanderlust.R

sealed class BottomNavigationItem(var graph: String, var route: String, var icon: Int, var title: String) {

    object Home : BottomNavigationItem(
        "home_graph",
        "home",
        R.drawable.baseline_format_list_bulleted_24,
        "Home")

    object Map : BottomNavigationItem(
        "map_graph",
        "map",
        R.drawable.baseline_map_24,
        "Map")

    object Notifications : BottomNavigationItem(
        "notifications_graph",
        "notifications",
        R.drawable.baseline_notifications_none_24,
        "Notifications")

    object Profile : BottomNavigationItem(
        "profile_graph",
        "profile",
        R.drawable.baseline_person_outline_24,
        "Profile")
}
