package com.wanderlust.ui.settings

import com.wanderlust.domain.model.User

data class CurrentSettings(
    val isDarkMode: Boolean?,
    val language: WanderlustLanguage,
    val currectUser: User?
)

enum class WanderlustLanguage(val locale: String){
    ENLISH(locale = "en"), RUSSIAN(locale = "ru")
}
