package com.example.rtsoftsolutions;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController controller = Navigation.findNavController(AuthenticationActivity.this , R.id.fragmentContainerView) ;
    }
}
