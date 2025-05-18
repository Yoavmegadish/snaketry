package com.example.snaketry;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PauseFragment extends DialogFragment {

    private MusicService musicService;
    private boolean isBound = false;
    private Switch musicSwitch;
    private boolean isMusicPlaying = true;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            musicSwitch.setChecked(isMusicPlaying);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    public PauseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pause, container, false);

        // Bind to MusicService
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Initialize music switch
        musicSwitch = view.findViewById(R.id.musicSwitch);
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isMusicPlaying = isChecked;
            if (isBound) {
                if (isChecked) {
                    musicService.startMusic();
                } else {
                    musicService.pauseMusic();
                }
            }
        });

        Button keepPlayingBtn = view.findViewById(R.id.keepplayingbtn);
        Button giveUpbtn = view.findViewById(R.id.giveupbtn);

        keepPlayingBtn.setOnClickListener(v -> {
            startCountdown();
            dismiss();
        });

        giveUpbtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                        .setTitle("Exit Game")
                        .setMessage("Are you sure you want to quit the game?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            getActivity().finish();  // Close the activity
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();  // Close the dialog
                        })
                        .show();
            }
        });

        return view;
    }

    private void startCountdown() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.startCountdown();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isBound && getActivity() != null) {
            getActivity().unbindService(connection);
            isBound = false;
        }
    }
}
