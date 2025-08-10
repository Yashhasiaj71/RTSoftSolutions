package com.example.rtsoftsolutions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.rtsoftsolutions.databinding.ActivityMainBinding;
import com.example.rtsoftsolutions.databinding.FacultyFormBinding;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_Faculty_Form extends Fragment {


    private FirebaseDatabase db ;
    private DatabaseReference reffaculty ;
    private FacultyFormBinding binding ;
    private final String TAG = "debugfaform" ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialized database
        try {
            db = AccessDatabase.getDB();
            reffaculty = db.getReference("Faculty") ;
        }catch(Exception e ) {
            Log.d(TAG , "Error in database connection on anything related to database") ;
        }


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         FacultyFormBinding binding= FacultyFormBinding.inflate(inflater) ;
        //todo : do your logic here
         binding.savedetails.setOnClickListener(v -> {
         });

         return binding.getRoot();



    }

}
