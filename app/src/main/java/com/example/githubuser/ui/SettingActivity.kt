package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.helper.MainViewModel
import com.example.githubuser.helper.SettingPreferences
import com.example.githubuser.helper.SettingViewModelFactory
import com.example.githubuser.utils.DarkMode
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val switchTheme = findViewById<SwitchMaterial>(R.id.sw_dark_mode)

        val pref = SettingPreferences.getInstance(dataStore)

        val mainViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            DarkMode.setDarkMode(isDarkModeActive)
            switchTheme.isChecked = isDarkModeActive
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }
}