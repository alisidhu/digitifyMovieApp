package com.movieapp.diditify.fragments

import android.os.Bundle
import com.movieapp.diditify.R



class SettingFragment : androidx.preference.PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}