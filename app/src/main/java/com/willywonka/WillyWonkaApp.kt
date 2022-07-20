package com.willywonka

import android.app.Application
import com.willywonka.model.repository.preferences.PreferencesProvider
import com.willywonka.model.repository.api.WillyWonkaAPI

class WillyWonkaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //This app
        instance = this

        // Shared prefs
        mPrefs = PreferencesProvider.getInstance(this)

        // Api Service
        apiService = WillyWonkaAPI.getInstance()
        apiService.initApiService()
    }

    companion object {
        lateinit var instance: WillyWonkaApp
            private set

        lateinit var mPrefs: PreferencesProvider
            private set

        lateinit var apiService: WillyWonkaAPI
            private set
    }
}