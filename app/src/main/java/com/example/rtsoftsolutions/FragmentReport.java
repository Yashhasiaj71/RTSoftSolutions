package com.example.rtsoftsolutions;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

public class FragmentReport extends Fragment {

    final boolean[] state = new boolean[1];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.report_fragment , container , false) ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity active = getActivity();
        TextView[] arr = {active.findViewById(R.id.slider1)};

        NavController controller = findNavController(FragmentReport.this);
        ToggleButton buttonordinaryreport = view.findViewById(R.id.ordinarybutton);
        ToggleButton buttonfeesreport = view.findViewById(R.id.feesbutton);
        buttonfeesreport.setTextOn("Fees report");
        buttonfeesreport.setTextOff("");
        buttonordinaryreport.setTextOn("Ordinary Report");
        buttonordinaryreport.setTextOff("");
        state[0] = true;

        if(!buttonordinaryreport.isChecked() && !buttonfeesreport.isChecked()) {
            buttonordinaryreport.setChecked(true);
            replaceInner(new FragmentOrdinaryReport());
            buttonfeesreport.setChecked(false);
        }

        buttonordinaryreport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                state[0] = true;
                buttonfeesreport.setChecked(false);
                replaceInner(new FragmentOrdinaryReport());
            }
        });

        buttonfeesreport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                state[0] = false;
                buttonordinaryreport.setChecked(false);
                replaceInner(new Fragment_feesReport());
            }
        });
    }

    private void replaceInner(Fragment fragment) {
        FragmentTransaction tx = getChildFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainerView, fragment).commit();
    }
}