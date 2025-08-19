package com.example.rtsoftsolutions;
import android.os.Bundle;

import com.example.rtsoftsolutions.Fragments.AdminHome;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    //changes from madhav
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your layout
        Fragment AdminHome = new AdminHome(); // your target fragment

    }

   @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this , R.id.ContainerView);

       ImageButton homeB = findViewById(R.id.HomeButton);
       ImageButton studentB = findViewById(R.id.StudentButton);
       ImageButton reportB = findViewById(R.id.ReportButton);

       View Homebg = findViewById(R.id.HomeClick);
       View Studentbg = findViewById(R.id.StudentClick);
       View Reportbg = findViewById(R.id.ReportClick);


       Homebg.setOnClickListener(v->{
           navController.navigate(R.id.action_global_AdminHome);
           homeB.setImageResource(R.drawable.house_regular_full_green);
           studentB.setImageResource(R.drawable.user_regular_full);
           reportB.setImageResource(R.drawable.chart_bar_regular_full);
       });

       Studentbg.setOnClickListener(v->{
           navController.navigate(R.id.action_global_studentmanager);
           homeB.setImageResource(R.drawable.house_regular_full);
           studentB.setImageResource(R.drawable.user_regular_full_green);
           reportB.setImageResource(R.drawable.chart_bar_regular_full);
       });

       Reportbg.setOnClickListener(v->{
           navController.navigate(R.id.action_global_fragmentReport);
           homeB.setImageResource(R.drawable.house_regular_full);
           studentB.setImageResource(R.drawable.user_regular_full);
           reportB.setImageResource(R.drawable.chart_bar_regular_full_green);
       });
    }
}
