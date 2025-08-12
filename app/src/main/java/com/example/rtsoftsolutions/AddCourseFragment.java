package com.example.rtsoftsolutions;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class AddCourseFragment extends Fragment {
    
    private EditText etCourseName, etCourseDescription, etInstructorName, etDuration;
    private EditText etFees, etStartDate, etEndDate, etMaxStudents;
    private EditText etBatchName, etBatchTime, etBatchDays;
    private Spinner spinnerStatus;
    private Button btnSave, btnCancel;
    
    // DatabaseReference will be created fresh in saveCourse() method

    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        initViews(view);
        setupSpinner();
        setupClickListeners();
    }

    private void initViews(View view) {
        System.out.println("=== initViews() called ===");
        
        etCourseName = view.findViewById(R.id.etCourseName);
        etCourseDescription = view.findViewById(R.id.etCourseDescription);
        etInstructorName = view.findViewById(R.id.etInstructorName);
        etDuration = view.findViewById(R.id.etDuration);
        etFees = view.findViewById(R.id.etFees);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etMaxStudents = view.findViewById(R.id.etMaxStudents);
        etBatchName = view.findViewById(R.id.etBatchName);
        etBatchTime = view.findViewById(R.id.etBatchTime);
        etBatchDays = view.findViewById(R.id.etBatchDays);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        
        // Debug logging for buttons
        System.out.println("btnSave found: " + (btnSave != null));
        System.out.println("btnCancel found: " + (btnCancel != null));
        
        if (btnSave != null) {
            System.out.println("btnSave text: " + btnSave.getText());
            System.out.println("btnSave enabled: " + btnSave.isEnabled());
            System.out.println("btnSave clickable: " + btnSave.isClickable());
            System.out.println("btnSave visibility: " + btnSave.getVisibility());
        } else {
            System.out.println("ERROR: btnSave is NULL!");
        }
        
        if (btnCancel != null) {
            System.out.println("btnCancel text: " + btnCancel.getText());
            System.out.println("btnCancel enabled: " + btnCancel.isEnabled());
        } else {
            System.out.println("ERROR: btnCancel is NULL!");
        }
        
        // Check if view is properly inflated
        System.out.println("View inflated successfully: " + (view != null));
        System.out.println("View ID: " + view.getId());
    }

    private void setupSpinner() {
        String[] statusOptions = {"Active", "Inactive", "Completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(), 
            android.R.layout.simple_spinner_item, 
            statusOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }

    private void setupClickListeners() {
        System.out.println("=== setupClickListeners() called ===");
        
        if (btnSave != null) {
            System.out.println("Setting up save button click listener...");
            btnSave.setOnClickListener(v -> {
                System.out.println("Save button clicked!");
                System.out.println("Button text: " + btnSave.getText());
                System.out.println("Button enabled: " + btnSave.isEnabled());
                saveCourse();
            });
            System.out.println("Save button click listener set successfully");
        } else {
            System.out.println("ERROR: Cannot set click listener - btnSave is null!");
        }
        
        if (btnCancel != null) {
            System.out.println("Setting up cancel button click listener...");
            btnCancel.setOnClickListener(v -> {
                System.out.println("Cancel button clicked!");
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            });
            System.out.println("Cancel button click listener set successfully");
        } else {
            System.out.println("ERROR: Cannot set click listener - btnCancel is null!");
        }
    }

    private void saveCourse() {
        System.out.println("=== saveCourse() method called ===");
        try {
            System.out.println("Getting input values...");
            // Get input values
            String courseName = etCourseName.getText().toString().trim();
            String courseDescription = etCourseDescription.getText().toString().trim();
            String instructorName = etInstructorName.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();
            String fees = etFees.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String maxStudents = etMaxStudents.getText().toString().trim();
            String batchName = etBatchName.getText().toString().trim();
            String batchTime = etBatchTime.getText().toString().trim();
            String batchDays = etBatchDays.getText().toString().trim();
            String status = spinnerStatus.getSelectedItem().toString();

            System.out.println("Input values:");
            System.out.println("Course Name: '" + courseName + "'");
            System.out.println("Description: '" + courseDescription + "'");
            System.out.println("Instructor: '" + instructorName + "'");
            System.out.println("Duration: '" + duration + "'");
            System.out.println("Fees: '" + fees + "'");
            System.out.println("Start Date: '" + startDate + "'");
            System.out.println("End Date: '" + endDate + "'");
            System.out.println("Max Students: '" + maxStudents + "'");
            System.out.println("Batch Name: '" + batchName + "'");
            System.out.println("Batch Time: '" + batchTime + "'");
            System.out.println("Batch Days: '" + batchDays + "'");
            System.out.println("Status: '" + status + "'");

            // Validate inputs
            if (courseName.isEmpty() || courseDescription.isEmpty() || instructorName.isEmpty() ||
                duration.isEmpty() || fees.isEmpty() || startDate.isEmpty() || 
                endDate.isEmpty() || maxStudents.isEmpty() || batchName.isEmpty() ||
                batchTime.isEmpty() || batchDays.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create course object
            String courseId = UUID.randomUUID().toString();
            Course course = new Course(courseId, courseName, courseDescription, instructorName,
                                     duration, fees, startDate, endDate, maxStudents, "0", status,
                                     batchName, batchTime, batchDays);

            // Log the course object for debugging
            System.out.println("Creating course with ID: " + courseId);
            System.out.println("Course name: " + courseName);
            System.out.println("Course status: " + status);

            System.out.println("Attempting to save course to emulator...");

            // Configure Firebase emulator (consistent with AddStudent)
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
  
            DatabaseReference databaseReference = database.getReference("courses");

            // Save to Firebase emulator using the existing databaseReference
            databaseReference.child(courseId).setValue(course)
                .addOnSuccessListener(aVoid -> {
                    try {
                        // Show success message
                        Toast.makeText(requireContext(), "Course added successfully", Toast.LENGTH_SHORT).show();
                        
                        // TEMPORARY: Don't navigate back to test if navigation causes crash
                        System.out.println("Course saved successfully, staying on this screen");
                        
                        // TODO: Re-enable navigation after fixing crash
                        // if (getActivity() != null && !getActivity().isFinishing()) {
                        //     new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        //         try {
                        //             if (getActivity() != null && !getActivity().isFinishing()) {
                        //                 getActivity().onBackPressed();
                        //             }
                        //         } catch (Exception e) {
                        //             e.printStackTrace();
                        //         }
                        //     });
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Navigation error: " + e.getMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to add course: " + e.getMessage(), 
                                 Toast.LENGTH_SHORT).show();
                });
        } catch (Exception e) {
            // Catch any unexpected errors
            e.printStackTrace();
            Toast.makeText(requireContext(), "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
} 
