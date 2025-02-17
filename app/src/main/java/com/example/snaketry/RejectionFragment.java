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

public class RejectionFragment extends DialogFragment {
    Button giveMeAnotherOnebtn,imOutbtn;
    public RejectionFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rejection, container, false);
        giveMeAnotherOnebtn=view.findViewById(R.id.anotheronebtn);
        imOutbtn=view.findViewById(R.id.imout);
        giveMeAnotherOnebtn.setOnClickListener(v ->
        {
            restart();
            dismiss(); // סגירת הפרגמנט
        });
        imOutbtn.setOnClickListener(v -> {
            dismiss(); // סגירת הפרגמנט
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            startActivity(intent);
        });
        return view;

    }
    private void restart() {
         MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.restart();  // קריאה לפונקציה במיין אקטיביטי
        }
    }
}
