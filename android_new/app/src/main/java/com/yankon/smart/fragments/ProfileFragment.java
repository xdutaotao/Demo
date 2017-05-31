package com.yankon.smart.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yankon.smart.R;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Settings;
import com.yankon.smart.utils.SyncUITask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.log_out_button)
    Button logOutButton;

    @BindView(R.id.sync_button)
    Button syncButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        String site = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("KII_SITE", null);
        String server = null;
        if (!TextUtils.isEmpty(site)) {
            if (site.equals("US")) {
                server = getString(R.string.server_us);
            } else if (site.equals("JP")) {
                server = getString(R.string.server_jp);
            } else if (site.equals("SG")) {
                server = getString(R.string.server_sg);
            } else if (site.equals("CN3")) {
                server = getString(R.string.server_cn);
            }
        }
        String emailDisplay = Settings.getEmail();
        if (!TextUtils.isEmpty(server))
            emailDisplay = emailDisplay + " (" + server + ")";
        email.setText(emailDisplay);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.logOut();
                LogUtils.d("ProfileFragment", "after log out, is logged in? " + Settings.isLoggedIn());
                LogUtils.d("ProfileFragment", "after log out, token is " + Settings.getToken());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(
                        Constants.INTENT_LOGGED_OUT));
            }
        });
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncUITask(getFragmentManager(), getString(R.string.syncing)).execute();
            }
        });
        return view;
    }
}
