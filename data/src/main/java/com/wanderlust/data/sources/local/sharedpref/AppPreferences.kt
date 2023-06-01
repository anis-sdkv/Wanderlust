package com.wanderlust.data.sources.local.sharedpref

import android.content.Context
import javax.inject.Inject

class AppPreferences(private val context: Context) {

    private val storage = context.getSharedPreferences(USER_STORAGE, Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        storage
            .edit()
            .putString(USER_ID, userId)
            .apply()
    }

    fun getUserId(): String? {
        return storage.getString(USER_ID, null)
    }

    fun saveSettings(settings: AppSettings) {
        storage
            .edit()
            .putString(LANGUAGE, settings.language)
            .putString(THEME, settings.isDarkMode?.toString())
            .apply()
    }

    fun getSettings(): AppSettings {
        val language = storage.getString(LANGUAGE, "ENGLISH") ?: "ENGLISH"
        val theme = storage.getString(THEME, null)?.toBoolean()
        return AppSettings(language = language, isDarkMode = theme)
    }

    companion object {
        const val USER_STORAGE = "USER_STORAGE"
        const val USER_ID = "USER_ID"
        const val LANGUAGE = "LANGUAGE"
        const val THEME = "THEME"
    }
}