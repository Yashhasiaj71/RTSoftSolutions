package com.example.rtsoftsolutions;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rtsoftsolutions.Models.Student;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AddStudent_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG =  "debugaddstudent" ;
    private Button savedetails ;
    private EditText nameinput ;
    private EditText emailinput ;
    private EditText phoneinput ;
    private EditText addressinput ;
    private EditText aadharinput ;
    private EditText fathernameinput ;
    private EditText mothernameinput;
    private EditText tpainput ;
    private EditText amountfilled ;
    private LinearLayout imageinputlayout ;
    private ImageView imageholder ;
    Activity currentactivity ;
    ActivityResultLauncher<String> imagePickerLauncher ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

// :Todo : write your logic here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Initialize all inputfields
        nameinput = view.findViewById(R.id.nameinput);
        emailinput = view.findViewById(R.id.emailinput);
        phoneinput = view.findViewById(R.id.phoneinput);
        addressinput = view.findViewById(R.id.addressinput);
        aadharinput = view.findViewById(R.id.aadharinput);
        fathernameinput = view.findViewById(R.id.fathernameinput);
        mothernameinput = view.findViewById(R.id.mothernameinput);
        tpainput = view.findViewById(R.id.tpainput);
        Button save = view.findViewById(R.id.savedetails);
        imageholder = view.findViewById(R.id.imageholder) ;


//      submit button on click listener
        save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//          add to database
            submitForm();
           }
       });


        //initialize image picker launcher
        launchmediainputactivity() ;
        //click listener to launch imageselecting activity
        imageholder.setOnClickListener(v -> {
            if(imagePickerLauncher == null) {
                Toast.makeText(getContext() , "Check imagePickerLauncher" , Toast.LENGTH_LONG).show();
            }else {
                imagePickerLauncher.launch("image/*");
            }
        });

}


    private void handleImageUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            // Use the bitmap (e.g., show in ImageView or convert to Base64)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean launchmediainputactivity() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            handleImageUri(uri);
                        }
                    }
                }
        );
        return true ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment================================================
        View view =  inflater.inflate(R.layout.fragment_add_student_, container, false);
        return view;
    }




    private boolean isFormValid() {
        // Get trimmed input from EditTexts
        String name = nameinput.getText().toString().trim();
        String email = emailinput.getText().toString().trim();
        String phoneNo = phoneinput.getText().toString().trim();  // now String
        String aadharStr = aadharinput.getText().toString().trim();
        String address = addressinput.getText().toString().trim();
        String fatherName = fathernameinput.getText().toString().trim();
        String motherName = mothernameinput.getText().toString().trim();
        String totalFeesStr = tpainput.getText().toString().trim();

        // Check empty fields
        if (name.isEmpty()) {
            nameinput.setError("Name is required");
            return false;
        }

        if (email.isEmpty()) {
            emailinput.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailinput.setError("Invalid email format");
            return false;
        }

        if (phoneNo.isEmpty()) {
            phoneinput.setError("Phone number is required");
            return false;
        } else if (!phoneNo.matches("\\d{10}")) {
            phoneinput.setError("Phone number must be exactly 10 digits");
            return false;
        }

        if (aadharStr.isEmpty()) {
            aadharinput.setError("Aadhar number is required");
            return false;
        } else if (!aadharStr.matches("\\d{12}")) {
            aadharinput.setError("Aadhar must be exactly 12 digits");
            return false;
        }

        if (address.isEmpty()) {
            addressinput.setError("Address is required");
            return false;
        }

        if (fatherName.isEmpty()) {
            fathernameinput.setError("Father's name is required");
            return false;
        }

        if (motherName.isEmpty()) {
            mothernameinput.setError("Mother's name is required");
            return false;
        }

        if (totalFeesStr.isEmpty()) {
            tpainput.setError("Total fees is required");
            return false;
        }

        // Check numeric value of total fees
        try {
            int totalFees = Integer.parseInt(totalFeesStr);
        } catch (NumberFormatException e) {
            tpainput.setError("Fees must be a valid number");
            return false;
        }
        return true; // All checks passed
    }

    private void submitForm() {
        if (!isFormValid()) {
            Toast.makeText(requireContext(),"Not valid Formats",Toast.LENGTH_LONG).show();
            return; // From previous function
        }

        // Extract all values
        String name = nameinput.getText().toString().trim();
        String email = emailinput.getText().toString().trim();
        String phoneNo = phoneinput.getText().toString().trim();
        String aadharNo = aadharinput.getText().toString().trim().replaceAll("\\s" , "");
        String address = addressinput.getText().toString().trim();
        String fatherName = fathernameinput.getText().toString().trim();
        String motherName = mothernameinput.getText().toString().trim();
        int totalFees = Integer.parseInt(tpainput.getText().toString().trim());

        // Create model
        Student student = new Student(name, email, phoneNo, aadharNo,
                address, fatherName, motherName, totalFees);

        // Connect to emulator and push data
        FirebaseDatabase firebasedb = AccessDatabase.getDB() ;
        DatabaseReference dbRef = firebasedb.getReference("Student");

        String key = dbRef.push().getKey(); // Create a unique key
        dbRef.child(key).setValue(student)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(), "Data submitted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

}