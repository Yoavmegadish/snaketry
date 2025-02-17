package com.example.snaketry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PauseFragment extends DialogFragment {

    public PauseFragment() {
        // קונסטרקטור ריק
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pause, container, false);

        Button keepPlayingBtn = view.findViewById(R.id.keepplayingbtn);  // כפתור המשך משחק
        keepPlayingBtn.setOnClickListener(v -> {
            startCountdown();  // התחלת הספירה לאחור
            dismiss();  // סגירת הפרגמנט
        });
         Button giveUpbtn=view.findViewById(R.id.giveupbtn);
         giveUpbtn.setOnClickListener(v -> {
             dismiss(); // סגירת הפרגמנט
             Intent intent = new Intent(getActivity(), PlayActivity.class);
             startActivity(intent);
         });
        return view;
    }

    private void startCountdown() {
        // שליחה למיין אקטיביטי כדי להתחיל את הספירה לאחור
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.startCountdown();  // קריאה לפונקציה במיין אקטיביטי
        }
    }
}
