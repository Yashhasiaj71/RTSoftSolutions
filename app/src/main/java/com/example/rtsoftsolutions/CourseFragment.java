package com.example.rtsoftsolutions;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.navigation.Navigation;
import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment implements CourseAdapter.OnCourseClickListener {

    private RecyclerView rvCourses;
    private FloatingActionButton fabAddCourse;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;
    private DatabaseReference databaseReference;

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        
        // Initialize Firebase with emulator (like AddStudent_Fragment)
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
       databaseReference = database.getReference("courses");
        
        // Initialize views
        rvCourses = view.findViewById(R.id.rvCourses);
        fabAddCourse = view.findViewById(R.id.fabAddCourse);
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
        
        // Load courses from Firebase emulator
        loadCourses();
        
        return view;
    }

    private void setupRecyclerView() {
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseList, this);
        rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCourses.setAdapter(courseAdapter);
    }

    private void setupClickListeners() {
        fabAddCourse.setOnClickListener(v -> {
            // Navigate to AddCourseFragment using Navigation component
            Navigation.findNavController(v).navigate(R.id.addCourseFragment);
        });
    }

    private void loadCourses() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    if (course != null) {
                        courseList.add(course);
                    }
                }
                courseAdapter.notifyDataSetChanged();
                
                if (courseList.isEmpty()) {
                    Toast.makeText(requireContext(), "No courses found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load courses: " + databaseError.getMessage(), 
                             Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCourseClick(Course course) {
        // Navigate to CourseDetailFragment using Navigation component
        // We'll need to pass the course data as arguments
        Bundle args = new Bundle();
        args.putString("course_id", course.getCourseId());
        args.putString("course_name", course.getCourseName());
        args.putString("course_description", course.getCourseDescription());
        args.putString("instructor_name", course.getInstructorName());
        args.putString("duration", course.getDuration());
        args.putString("fees", course.getFees());
        args.putString("start_date", course.getStartDate());
        args.putString("end_date", course.getEndDate());
        args.putString("max_students", course.getMaxStudents());
        args.putString("current_students", course.getCurrentStudents());
        args.putString("status", course.getStatus());
        
        Navigation.findNavController(requireView()).navigate(R.id.courseDetailFragment, args);
    }
}