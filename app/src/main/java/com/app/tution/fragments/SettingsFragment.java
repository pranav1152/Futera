package com.app.tution.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tution.R;
import com.app.tution.activities.Launcher;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {

    private View settingsView;
    private Toolbar toolbar;
    private MaterialButton logoutButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        logoutButton = settingsView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getActivity(), Launcher.class);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });
        return settingsView;
    }
}