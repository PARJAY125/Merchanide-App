package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class GlobalVariable : Application() {
    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val KEY_MERCHANDISER = "namaMerchandiser"
    }

    private lateinit var sharedPreferences: SharedPreferences

    var namaMerchandiser: String = ""
        set(value) {
            field = value
            // Save the value to SharedPreferences when it is set
            sharedPreferences.edit().putString(KEY_MERCHANDISER, value).apply()
        }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        namaMerchandiser = sharedPreferences.getString(KEY_MERCHANDISER, "") ?: ""
    }
}
