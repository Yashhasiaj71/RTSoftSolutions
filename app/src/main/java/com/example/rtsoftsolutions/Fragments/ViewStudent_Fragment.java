package com.example.rtsoftsolutions.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtsoftsolutions.Adapters.StudentAdapter;
import com.example.rtsoftsolutions.Models.Student;
import com.example.rtsoftsolutions.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewStudent_Fragment extends Fragment implements StudentAdapter.OnStudentClickListener {

    private static final String TAG = "ViewStudent_Fragment";

    // UI Components
    private EditText studentSearchInput;
    private ImageButton clearSearchButton;
    private Spinner courseFilterSpinner;
    private Spinner batchFilterSpinner;
    private Chip sortByNameChip;
    private Chip sortByCourseChip;
    private Chip sortByFeesChip;
    private TextView studentCountText;
    private TextView filteredCountText;
    private RecyclerView studentRecyclerView;
    private LinearLayout emptyStateLayout;
    private ProgressBar loadingProgressBar;

    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;

    // Data
    private List<Student> allStudents;
    private List<Student> filteredStudents;
    private StudentAdapter studentAdapter;
    private Set<String> courseSet;
    private Set<String> batchSet;

    // Current filters and sort
    private String currentSearchQuery = "";
    private String currentCourseFilter = "All Courses";
    private String currentBatchFilter = "All Batches";
    private String currentSortBy = "name"; // name, course, fees

    public ViewStudent_Fragment() {
        // Required empty public constructor
    }

    public static ViewStudent_Fragment newInstance(String param1, String param2) {
        ViewStudent_Fragment fragment = new ViewStudent_Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle arguments if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_student_, container, false);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");

        // Initialize data
        allStudents = new ArrayList<>();
        filteredStudents = new ArrayList<>();
        courseSet = new HashSet<>();
        batchSet = new HashSet<>();

        // Initialize UI components
        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        setupSearchListener();

        // Load students from Firebase
        loadStudentsFromFirebase();

        return view;
    }

    private void initializeViews(View view) {
        studentSearchInput = view.findViewById(R.id.StudentSearchInput);
        clearSearchButton = view.findViewById(R.id.ClearSearchButton);
        courseFilterSpinner = view.findViewById(R.id.CourseFilterSpinner);
        batchFilterSpinner = view.findViewById(R.id.BatchFilterSpinner);
        sortByNameChip = view.findViewById(R.id.SortByNameChip);
        sortByCourseChip = view.findViewById(R.id.SortByCourseChip);
        sortByFeesChip = view.findViewById(R.id.SortByFeesChip);
        studentCountText = view.findViewById(R.id.StudentCountText);
        filteredCountText = view.findViewById(R.id.FilteredCountText);
        studentRecyclerView = view.findViewById(R.id.StudentRecyclerView);
        emptyStateLayout = view.findViewById(R.id.EmptyStateLayout);
        loadingProgressBar = view.findViewById(R.id.LoadingProgressBar);
    }

    private void setupRecyclerView() {
        studentAdapter = new StudentAdapter(filteredStudents, this);
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        studentRecyclerView.setAdapter(studentAdapter);
    }

    private void setupClickListeners() {
        // Clear search button
        clearSearchButton.setOnClickListener(v -> {
            studentSearchInput.setText("");
            currentSearchQuery = "";
            applyFiltersAndSort();
        });

        // Sort chips
        sortByNameChip.setOnClickListener(v -> {
            setSortChip(sortByNameChip, sortByCourseChip, sortByFeesChip);
            currentSortBy = "name";
            applyFiltersAndSort();
        });

        sortByCourseChip.setOnClickListener(v -> {
            setSortChip(sortByCourseChip, sortByNameChip, sortByFeesChip);
            currentSortBy = "course";
            applyFiltersAndSort();
        });

        sortByFeesChip.setOnClickListener(v -> {
            setSortChip(sortByFeesChip, sortByNameChip, sortByCourseChip);
            currentSortBy = "fees";
            applyFiltersAndSort();
        });
    }

    private void setupSearchListener() {
        studentSearchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().toLowerCase().trim();
                clearSearchButton.setVisibility(currentSearchQuery.isEmpty() ? View.GONE : View.VISIBLE);
                applyFiltersAndSort();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setSortChip(Chip selectedChip, Chip... otherChips) {
        selectedChip.setChipBackgroundColorResource(R.color.Accent_green);
        selectedChip.setTextColor(getResources().getColor(android.R.color.white));
        
        for (Chip chip : otherChips) {
            chip.setChipBackgroundColorResource(android.R.color.darker_gray);
            chip.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    private void loadStudentsFromFirebase() {
        showLoading(true);
        
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allStudents.clear();
                courseSet.clear();
                batchSet.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        allStudents.add(student);
                        courseSet.add(student.selectedCourseName);
                        batchSet.add(student.selectedBatchName);
                    }
                }

                setupFilterSpinners();
                applyFiltersAndSort();
                showLoading(false);
                updateStudentCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showLoading(false);
                Toast.makeText(requireContext(), "Failed to load students: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load students", databaseError.toException());
            }
        });
    }

    private void setupFilterSpinners() {
        // Setup course filter spinner
        List<String> courseList = new ArrayList<>(courseSet);
        Collections.sort(courseList);
        courseList.add(0, "All Courses");
        
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, courseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseFilterSpinner.setAdapter(courseAdapter);

        // Setup batch filter spinner
        List<String> batchList = new ArrayList<>(batchSet);
        Collections.sort(batchList);
        batchList.add(0, "All Batches");
        
        ArrayAdapter<String> batchAdapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, batchList);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchFilterSpinner.setAdapter(batchAdapter);

        // Set listeners
        courseFilterSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                currentCourseFilter = parent.getItemAtPosition(position).toString();
                applyFiltersAndSort();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        batchFilterSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                currentBatchFilter = parent.getItemAtPosition(position).toString();
                applyFiltersAndSort();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void applyFiltersAndSort() {
        filteredStudents.clear();

        // Apply filters
        for (Student student : allStudents) {
            boolean matchesSearch = currentSearchQuery.isEmpty() ||
                    student.name.toLowerCase().contains(currentSearchQuery) ||
                    student.selectedCourseName.toLowerCase().contains(currentSearchQuery) ||
                    student.selectedBatchName.toLowerCase().contains(currentSearchQuery);

            boolean matchesCourse = currentCourseFilter.equals("All Courses") ||
                    student.selectedCourseName.equals(currentCourseFilter);

            boolean matchesBatch = currentBatchFilter.equals("All Batches") ||
                    student.selectedBatchName.equals(currentBatchFilter);

            if (matchesSearch && matchesCourse && matchesBatch) {
                filteredStudents.add(student);
            }
        }

        // Apply sorting
        sortStudents();

        // Update UI
        studentAdapter.updateStudents(filteredStudents);
        updateFilteredCount();
        showEmptyStateIfNeeded();
    }

    private void sortStudents() {
        switch (currentSortBy) {
            case "name":
                Collections.sort(filteredStudents, (s1, s2) -> s1.name.compareToIgnoreCase(s2.name));
                break;
            case "course":
                Collections.sort(filteredStudents, (s1, s2) -> s1.selectedCourseName.compareToIgnoreCase(s2.selectedCourseName));
                break;
            case "fees":
                Collections.sort(filteredStudents, (s1, s2) -> Integer.compare(s1.remainingFees, s2.remainingFees));
                break;
        }
    }

    private void updateStudentCount() {
        studentCountText.setText("Total Students: " + allStudents.size());
    }

    private void updateFilteredCount() {
        filteredCountText.setText("Showing: " + filteredStudents.size());
    }

    private void showEmptyStateIfNeeded() {
        if (filteredStudents.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            studentRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            studentRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            emptyStateLayout.setVisibility(View.GONE);
            studentRecyclerView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onStudentClick(Student student, String studentId) {
        // Navigate to student details or profile
        NavController controller = findNavController(ViewStudent_Fragment.this);
        controller.navigate(R.id.action_viewStudent_Fragment_to_studentProfile);

        SharedPreferences nameShare = getActivity().getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = nameShare.edit();
        editor.putString("Name",student.name);
        editor.commit();
        editor.apply();
        Log.d("name",student.name);

        // You can implement navigation to a student details fragment here
    }
}