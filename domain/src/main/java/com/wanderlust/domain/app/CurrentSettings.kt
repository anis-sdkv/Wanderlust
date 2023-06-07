package com.wanderlust.domain.model

data class CurrentSettings(
    val isDarkMode: Boolean?,
    val language: WanderlustLanguage
)

enum class WanderlustLanguage(val locale: String){
    ENGLISH(locale = "en"), RUSSIAN(locale = "ru")
}