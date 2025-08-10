package com.example.rtsoftsolutions;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private ImageButton imageholder ;
    Activity currentactivity ;
    ActivityResultLauncher<Intent> imagePickerLauncher ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          //====================Initialize all datatypes============================

        //        get all the input fields
        nameinput = view.findViewById(R.id.nameinput);
        emailinput = view.findViewById(R.id.emailinput);
        phoneinput = view.findViewById(R.id.phoneinput);
        addressinput = view.findViewById(R.id.addressinput);
        aadharinput = view.findViewById(R.id.aadharinput);
        fathernameinput = view.findViewById(R.id.fathernameinput);
        mothernameinput = view.findViewById(R.id.mothernameinput);
        tpainput = view.findViewById(R.id.tpainput);
        imageholder = view.findViewById(R.id.imageholder);

//        ======== handle save button put data to databse =======
       Button save = view.findViewById(R.id.savedetails);
       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               add to database
            submitForm();
           }
       });
    }

//    function to check if all the input fields are valid and not null
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

// function to submit the form to the database
private void submitForm() {
    if (!isFormValid()) {
        Toast.makeText(requireContext(),"Not valid Formats",Toast.LENGTH_LONG).show();
        return; // From previous function
    }

    // Extract all values
    String name = nameinput.getText().toString().trim();
    String email = emailinput.getText().toString().trim();
    String phoneNo = phoneinput.getText().toString().trim();
    String aadharNo = aadharinput.getText().toString().trim();
    String address = addressinput.getText().toString().trim();
    String fatherName = fathernameinput.getText().toString().trim();
    String motherName = mothernameinput.getText().toString().trim();
    int totalFees = Integer.parseInt(tpainput.getText().toString().trim());

    // Create model
    Student student = new Student(name, email, phoneNo, aadharNo,
            address, fatherName, motherName, totalFees);

    // Connect to emulator and push data
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.useEmulator("10.0.2.2", 9000); // Emulator IP and port
    DatabaseReference dbRef = database.getReference("students");

    String key = dbRef.push().getKey(); // Create a unique key
    dbRef.child(key).setValue(student)
            .addOnSuccessListener(unused -> {
                Toast.makeText(requireContext(), "Data submitted successfully!", Toast.LENGTH_SHORT).show();

                //  close the add student fragment
                   // requireActivity().getSupportFragmentManager().popBackStack();

            })
            .addOnFailureListener(e -> {
                Toast.makeText(requireContext(), "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

}


    public boolean launchmediainputactivity(Context current , ActivityResultLauncher<Intent> imagePickerLauncher) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
            return true ;
        } catch (NullPointerException e) {
            Log.d(TAG, "Null Pointer exception please watch it carefully you stupid");
        }
        return false ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment================================================
        View view =  inflater.inflate(R.layout.fragment_add_student_, container, false);

        return view;
    }


}