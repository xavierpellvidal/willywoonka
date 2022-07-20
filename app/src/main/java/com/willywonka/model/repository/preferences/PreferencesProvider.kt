package com.willywonka.model.repository.preferences

import android.content.Context
import android.content.SharedPreferences

enum class PreferencesKey {
    WOONKLY_PREFERENCES,
    FILTERED_DATA,
    GENDER_FILTER,
    PROFESSION_FILTER
}

class PreferencesProvider private constructor(context: Context){

    companion object {
        //SharedPreference singleton
        private var instance: PreferencesProvider? = null

        fun getInstance(context: Context): PreferencesProvider {
            if (instance == null) {
                instance = PreferencesProvider(context)
            }
            return instance!!
        }
    }

    private val mPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(PreferencesKey.WOONKLY_PREFERENCES.name, Context.MODE_PRIVATE)
    }

    fun set(key: PreferencesKey, value: String?) {
        val editor = mPrefs.edit()
        editor.putString(key.name, value).apply()
    }

    fun string(key: PreferencesKey) : String? {
        return mPrefs.getString(key.name, null)
    }

    //Delete sharedPrefs
    fun clear() {
        val editor = mPrefs.edit()
        editor.clear().apply()
    }

    /**
     * APPLY FILTERS
     */
    fun getFilteredData() : Boolean{
        return mPrefs.getBoolean(PreferencesKey.FILTERED_DATA.name, false)
    }

    fun setFilteredData(value: Boolean) {
        val editor = mPrefs.edit()
        editor.putBoolean(PreferencesKey.FILTERED_DATA.name, value).apply()
    }

    /**
     * GENDER FILTER
     */
    fun setGenderFilter(gender : String) {
        val editor = mPrefs.edit()
        editor.putString(PreferencesKey.GENDER_FILTER.name, gender).apply()
    }

    fun getGenderFilter(): String? {
        return mPrefs.getString(PreferencesKey.GENDER_FILTER.name, "")
    }

    /**
     * PROFESSION FILTER
     */
    fun setProfessionFilter(gender : String) {
        val editor = mPrefs.edit()
        editor.putString(PreferencesKey.PROFESSION_FILTER.name, gender).apply()
    }

    fun getProfessionFilter(): String? {
        return mPrefs.getString(PreferencesKey.PROFESSION_FILTER.name, "")
    }
}