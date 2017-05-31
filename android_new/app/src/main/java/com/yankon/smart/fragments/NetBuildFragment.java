package com.yankon.smart.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.yankon.smart.MainActivity;
import com.yankon.smart.R;

/**
 * create by jack for ne
 */
public class NetBuildFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {

    public static final String ARG_SECTION_NUMBER = "section_number";

    private ListPreference mSyncConfigPref;

    private ListPreference mDefaultHomePref;

    private SharedPreferences mPref;

    public static NetBuildFragment newInstance(int sectionNumber) {
        NetBuildFragment fragment = new NetBuildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity parentActivity = ((MainActivity) activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSyncConfigPref = (ListPreference) findPreference("win_policy");
        mDefaultHomePref = (ListPreference) findPreference("default_home_page");
        setupSyncPolicy();
    }

    private void setupSyncPolicy() {
        int winPolicyIndex = 0;
        int defaultHomePage = 0;
        try {
            winPolicyIndex = Integer.parseInt(mSyncConfigPref.getValue());
            defaultHomePage = Integer.parseInt(mDefaultHomePref.getValue());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mSyncConfigPref.setSummary(
                getResources().getStringArray(R.array.sync_policies)[winPolicyIndex]);
        mDefaultHomePref.setSummary(
                getResources().getStringArray(R.array.default_home_page_entries)[defaultHomePage+1]);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setupSyncPolicy();
    }
}
