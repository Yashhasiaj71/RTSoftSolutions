package com.example.rtsoftsolutions.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rtsoftsolutions.Models.Student;
import com.example.rtsoftsolutions.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import com.example.rtsoftsolutions.Models.Course;

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
    private EditText etBirthDate;
    private DatePicker datePicker;
    private Spinner spinnerCourse;
    private Spinner spinnerBatch;
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

        // Initialize views
        nameinput = view.findViewById(R.id.nameinput);
        emailinput = view.findViewById(R.id.emailinput);
        phoneinput = view.findViewById(R.id.phoneinput);
        addressinput = view.findViewById(R.id.addressinput);
        aadharinput = view.findViewById(R.id.aadharinput);
        fathernameinput = view.findViewById(R.id.fathernameinput);
        mothernameinput = view.findViewById(R.id.mothernameinput);
        tpainput = view.findViewById(R.id.tpainput);
        etBirthDate = view.findViewById(R.id.etBirthDate);
        spinnerCourse = view.findViewById(R.id.spinnerCourse);
        spinnerBatch = view.findViewById(R.id.spinnerBatch);
        savedetails = view.findViewById(R.id.savedetails);
        imageholder = view.findViewById(R.id.imageholder);
        imageinputlayout = view.findViewById(R.id.imageinputlayout);
        amountfilled = view.findViewById(R.id.amountfilled);

        // Load courses for the spinner
        loadCoursesForSpinner();

        etBirthDate.setFocusable(false); // Prevent keyboard from showing
        etBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });


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


        private void openDatePicker() {
            // Get today’s date as default
            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            int year = calendar.get(java.util.Calendar.YEAR);
            int month = calendar.get(java.util.Calendar.MONTH);
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Month is 0-based, so add +1
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etBirthDate.setText(date);
                    },
                    year, month, day
            );

            // Optional: restrict future dates (since it's a DOB field)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();
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

        // Validate birth date
        String birthDate = etBirthDate.getText().toString().trim();
        if (birthDate.isEmpty()) {
            etBirthDate.setError("Birth date is required");
            return false;
        }

        // Validate course selection
        if (spinnerCourse.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Please select a course", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate batch selection
        if (spinnerBatch.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Please select a batch", Toast.LENGTH_SHORT).show();
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
        String birthDate = etBirthDate.getText().toString().trim();

        // Get selected course and batch
        Course selectedCourse = (Course) spinnerCourse.getSelectedItem();
        String selectedBatch = (String) spinnerBatch.getSelectedItem();

        if (selectedCourse == null || selectedCourse.getCourseId().isEmpty()) {
            Toast.makeText(requireContext(), "Please select a course", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Select a Batch".equals(selectedBatch)) {
            Toast.makeText(requireContext(), "Please select a batch", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create model
        Student student = new Student(name, email, phoneNo, aadharNo,
                address, fatherName, motherName, totalFees, birthDate,
                selectedCourse.getCourseId(), selectedCourse.getCourseName(), selectedBatch);

        // Connect to emulator and push data
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        DatabaseReference dbRef = database.getReference("students");

        String key = dbRef.push().getKey(); // Create a unique key
        dbRef.child(key).setValue(student)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(), "Data submitted successfully!", Toast.LENGTH_SHORT).show();

                    //  close the add student fragment
                    // requireActivity().getSupportFragmentManager().popBackStack();

                // Increment currentStudents for the selected course atomically
                if (selectedCourse != null && selectedCourse.getCourseId() != null && !selectedCourse.getCourseId().isEmpty()) {
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
                  
                    DatabaseReference courseRef = database2.getReference("courses").child(selectedCourse.getCourseId()).child("currentStudents");
                    courseRef.runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            try {
                                String currentVal = currentData.getValue(String.class);
                                int current = 0;
                                if (currentVal != null && currentVal.matches("\\d+")) {
                                    current = Integer.parseInt(currentVal);
                                }
                                current += 1;
                                currentData.setValue(String.valueOf(current));
                            } catch (Exception ignored) { }
                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                            // No-op; list updates via listeners
                        }
                    });
                }

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

    // Load courses for the spinner
    private void loadCoursesForSpinner() {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");

        DatabaseReference coursesRef = database.getReference("courses");

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Course> courseList = new ArrayList<>();

                courseList.add(new Course( "", "Select A Course", "", "", "", "", "", "", "", "", "", "","",""));

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    if (course != null && "Active".equals(course.getStatus())) {
                        courseList.add(course);
                    }
                }

                ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(requireContext(),
                        android.R.layout.simple_spinner_item, courseList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        if (view instanceof TextView) {
                            Course course = courseList.get(position);
                            ((TextView) view).setText(course.getCourseName() + " - " + course.getBatchName());
                        }
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        if (view instanceof TextView) {
                            Course course = courseList.get(position);
                            ((TextView) view).setText(course.getCourseName() + " - " + course.getBatchName());
                        }
                        return view;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCourse.setAdapter(adapter);

                // Set up batch spinner when course is selected
                spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            Course selectedCourse = courseList.get(position);
                            // Update batches
                            updateBatchSpinner(selectedCourse);
                            // Auto-fill fees from selected course
                            if (tpainput != null) {
                                tpainput.setText(selectedCourse.getFees() != null ? selectedCourse.getFees() : "");
                            }
                        } else {
                            // Reset fees when no course selected
                            if (tpainput != null) {
                                tpainput.setText("");
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load courses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update batch spinner based on selected course
    private void updateBatchSpinner(Course course) {
        List<String> batchOptions = new ArrayList<>();
        batchOptions.add("Select a Batch");
        batchOptions.add(course.getBatchName());

        ArrayAdapter<String> batchAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, batchOptions);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBatch.setAdapter(batchAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment================================================
        View view =  inflater.inflate(R.layout.fragment_add_student_, container, false);

        return view;
    }


}