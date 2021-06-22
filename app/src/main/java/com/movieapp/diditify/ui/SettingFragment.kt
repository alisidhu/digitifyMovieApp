package com.movieapp.diditify.ui
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.movieapp.diditify.R.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SettingFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(xml.preferences, rootKey)
    }

}