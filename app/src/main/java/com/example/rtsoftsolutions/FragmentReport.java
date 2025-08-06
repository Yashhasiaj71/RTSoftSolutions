package com.example.rtsoftsolutions;

import static android.view.View.GONE;
import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import static java.lang.Thread.sleep;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rtsoftsolutions.databinding.ActivityAuthenticationBinding;

public class FragmentReport extends Fragment {



    final boolean[] state = new boolean[1] ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.report_fragment , container , false) ;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity active = getActivity() ;
        TextView[] arr = {active.findViewById(R.id.slider1)} ;

        NavController controller = findNavController(FragmentReport.this);
        ToggleButton buttonordinaryreport = view.findViewById(R.id.ordinarybutton);
        ToggleButton buttonfeesreport = view.findViewById(R.id.feesbutton) ;
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background);
        buttonfeesreport.setTextOn("Fees report");
        buttonfeesreport.setTextOff("") ;
        buttonordinaryreport.setTextOn("Ordinary Report");
        buttonordinaryreport.setTextOff("") ;
        state[0] = true ;
       int width =  active.findViewById(R.id.togglebuttonview).getWidth() ;
        active.findViewById(R.id.togglebuttonview).setMinimumWidth(width + 30);
        if(!buttonordinaryreport.isChecked() && !buttonfeesreport.isChecked()) {
            buttonordinaryreport.setChecked(true);
            Fragment fragmentB = new FragmentOrdinaryReport();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragmentB)
                    .commit();
            buttonfeesreport.setChecked(false);
        }



        buttonordinaryreport.setOnCheckedChangeListener((buttonView, isChecked) -> {
              if(state[0]) {
                   buttonordinaryreport.setChecked(true);
              }else {
                  if(isChecked) {
                      state[0] = true ;
                      Fragment fragmentB = new FragmentOrdinaryReport() ;
                      requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView , fragmentB).commit() ;
                      buttonfeesreport.setChecked(false);
                      buttonordinaryreport.setChecked(true);
                    //  buttonfeesreport.setForeground(drawable);
                     // buttonordinaryreport.setForeground(null);
                       blockanimator(getContext() , arr);
                  }
              }
        });

        buttonfeesreport.setOnCheckedChangeListener((buttonView, isChecked) -> {
              if(!state[0]) {
                  buttonfeesreport.setChecked(true);
              }else {
                  if(isChecked) {
                      state[0] = false ;
                       Fragment fragmentB = new Fragment_feesReport() ;
                       requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView , fragmentB).commit() ;
                       buttonordinaryreport.setChecked(false);
                       buttonfeesreport.setChecked(true);
                       //buttonordinaryreport.setForeground(drawable);
                 // buttonfeesreport.setForeground(null);
                      Animation Rightmovement= AnimationUtils.loadAnimation(getContext() , R.anim.rightswipe) ;
                      Animation fadein = AnimationUtils.loadAnimation(getContext() , R.anim.fadein);
                      blockanimator(getContext() , arr);


                  }
              }
        });

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


    private void blockanimator(Context context ,TextView[] arr) {
         new Thread(new Runnable() {
             @Override
             public void run() {
                 for(int i = 0 ; i < arr.length ; i++) {
                     if(state[0]) {
                         animateBlockOnUIThreadltr(arr[i]);
                     }else {
                          animateBlockOnUIThreadrtl(arr[(arr.length - 1) - i]);
                     }
                      try {
                           Thread.sleep(50) ;
                     } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                      }
                 }
             }
         }).start();
    }
}
