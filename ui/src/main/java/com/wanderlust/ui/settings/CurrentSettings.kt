package com.wanderlust.ui.settings

data class CurrentSettings(
    val isDarkMode: Boolean?,
    val language: WanderlustLanguage
)

enum class WanderlustLanguage(val locale: String){
    ENLISH(locale = "en"), RUSSIAN(locale = "ru")
}
