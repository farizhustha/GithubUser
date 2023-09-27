package com.example.githubuser.utils

import androidx.appcompat.app.AppCompatDelegate

class DarkMode {
    companion object {
        fun setDarkMode(isDarkModeActive: Boolean) {
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}