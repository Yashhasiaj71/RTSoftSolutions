package com.example.rtsoftsolutions;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtsoftsolutions.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
// In your Activity or Fragment (e.g., in onCreate or onStart)

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log; // For logging the value
import android.widget.TextView;

public class MainActivity extends AppCompatActivity { // Or your relevant class
RecyclerView recyclerView;
    private static final String TAG = "FirebaseS_idAccess";
    private DatabaseReference sIdRef;
    private ValueEventListener sIdListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your layout
      recyclerView=  (RecyclerView) findViewById(R.id.rv);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
