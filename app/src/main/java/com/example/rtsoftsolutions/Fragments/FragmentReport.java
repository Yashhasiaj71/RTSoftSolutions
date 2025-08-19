package com.example.rtsoftsolutions.Fragments;

import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.example.rtsoftsolutions.R;

public class FragmentReport extends Fragment {

    final boolean[] state = new boolean[1];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.report_fragment, container, false);
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

        if (!buttonordinaryreport.isChecked() && !buttonfeesreport.isChecked()) {
            buttonordinaryreport.setChecked(true);
            replaceInner(new FragmentOrdinaryReport());
            buttonfeesreport.setChecked(false);
        }

        //on checked listeners

        buttonordinaryreport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (state[0]) {
                buttonordinaryreport.setChecked(true);
            } else {
                if (isChecked) {
                    state[0] = true;
                    Fragment fragmentB = new FragmentOrdinaryReport();
                    buttonfeesreport.setChecked(false);
                    buttonordinaryreport.setChecked(true);
                    blockanimator(getContext(), arr);
                    replaceInner(fragmentB);
                }
            }
        });

        // fees report setoncheckedchangelistener
        buttonfeesreport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!state[0]) {
                buttonfeesreport.setChecked(true);
            } else {
                if (isChecked) {
                    state[0] = false;
                    Fragment fragmentB = new Fragment_feesReport();
                    buttonordinaryreport.setChecked(false);
                    buttonfeesreport.setChecked(true);
                    Animation Rightmovement = AnimationUtils.loadAnimation(getContext(), R.anim.rightswipe);
                    Animation fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                    blockanimator(getContext(), arr);
                    replaceInner(fragmentB) ;


                }
            }
        });

    }

    private void replaceInner(Fragment fragment) {
        FragmentTransaction tx = getChildFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainerView, fragment).commit();
    }


    private void animateBlockOnUIThreadrtl(TextView block) {
        getActivity().runOnUiThread(() -> {
            ObjectAnimator animator = ObjectAnimator.ofFloat(block, "translationX", 0f, -400f);
            animator.setDuration(700);
            animator.start();
            block.setLayoutDirection(LAYOUT_DIRECTION_LTR);
        });
    }

    private void animateBlockOnUIThreadltr(TextView block) {
        getActivity().runOnUiThread(() -> {
            ObjectAnimator animator = ObjectAnimator.ofFloat(block, "translationX", 35f);
            animator.setDuration(700);
            animator.start();
            block.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        });
    }


    private void blockanimator(Context context, TextView[] arr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arr.length; i++) {
                    if (state[0]) {
                        animateBlockOnUIThreadltr(arr[i]);
                    } else {
                        animateBlockOnUIThreadrtl(arr[(arr.length - 1) - i]);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }
}