package com.example.snaketry;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RejectionFragment extends DialogFragment {
    Button giveMeAnotherOnebtn, imOutbtn;
    TextView scoreText, youreoutText;

    public RejectionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rejection, container, false);
        
        // Initialize views
        giveMeAnotherOnebtn = view.findViewById(R.id.anotheronebtn);
        imOutbtn = view.findViewById(R.id.imout);
        scoreText = view.findViewById(R.id.scoreText);
        youreoutText = view.findViewById(R.id.youreout);

        // Get and display the current score
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            int currentScore = activity.getLastScore();
            scoreText.setText("Score: " + currentScore);
        }

        // Set click listeners
        giveMeAnotherOnebtn.setOnClickListener(v -> {
            restart();
            dismiss();
        });

        imOutbtn.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            startActivity(intent);
        });

        // Add animations
        startAnimations();

        return view;
    }

    private void startAnimations() {
        // Animate "Game Over" text
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(youreoutText, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(youreoutText, "scaleY", 0f, 1f);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleX.start();
        scaleY.start();

        // Animate score text with delay
        scoreText.setAlpha(0f);
        scoreText.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(300)
                .start();
    }

    private void restart() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.restart();
        }
    }
}
