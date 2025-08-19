package com.example.rtsoftsolutions;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CourseDetailFragment extends Fragment {
    
    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_COURSE_NAME = "course_name";
    private static final String ARG_COURSE_DESCRIPTION = "course_description";
    private static final String ARG_INSTRUCTOR_NAME = "instructor_name";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_FEES = "fees";
    private static final String ARG_START_DATE = "start_date";
    private static final String ARG_END_DATE = "end_date";
    private static final String ARG_MAX_STUDENTS = "max_students";
    private static final String ARG_CURRENT_STUDENTS = "current_students";
    private static final String ARG_STATUS = "status";

    private String courseId, courseName, courseDescription, instructorName, duration, fees;
    private String startDate, endDate, maxStudents, currentStudents, status;

    public CourseDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString(ARG_COURSE_ID);
            courseName = getArguments().getString(ARG_COURSE_NAME);
            courseDescription = getArguments().getString(ARG_COURSE_DESCRIPTION);
            instructorName = getArguments().getString(ARG_INSTRUCTOR_NAME);
            duration = getArguments().getString(ARG_DURATION);
            fees = getArguments().getString(ARG_FEES);
            startDate = getArguments().getString(ARG_START_DATE);
            endDate = getArguments().getString(ARG_END_DATE);
            maxStudents = getArguments().getString(ARG_MAX_STUDENTS);
            currentStudents = getArguments().getString(ARG_CURRENT_STUDENTS);
            status = getArguments().getString(ARG_STATUS);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        TextView tvCourseName = view.findViewById(R.id.tvCourseName);
        TextView tvCourseDescription = view.findViewById(R.id.tvCourseDescription);
        TextView tvInstructor = view.findViewById(R.id.tvInstructor);
        TextView tvDuration = view.findViewById(R.id.tvDuration);
        TextView tvFees = view.findViewById(R.id.tvFees);
        TextView tvStartDate = view.findViewById(R.id.tvStartDate);
        TextView tvEndDate = view.findViewById(R.id.tvEndDate);
        TextView tvMaxStudents = view.findViewById(R.id.tvMaxStudents);
        TextView tvCurrentStudents = view.findViewById(R.id.tvCurrentStudents);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnBack = view.findViewById(R.id.btnBack);

        // Set data
        tvCourseName.setText(courseName);
        tvCourseDescription.setText(courseDescription);
        tvInstructor.setText(instructorName);
        tvDuration.setText(duration);
        tvFees.setText("₹" + fees);
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
        tvMaxStudents.setText(maxStudents);
        tvCurrentStudents.setText(currentStudents);
        tvStatus.setText(status);

        // Set status background
        switch (status.toLowerCase()) {
            case "active":
                tvStatus.setBackgroundResource(R.drawable.status_active);
                break;
            case "inactive":
                tvStatus.setBackgroundResource(R.drawable.status_inactive);
                break;
            case "completed":
                tvStatus.setBackgroundResource(R.drawable.status_completed);
                break;
        }

        // Set click listeners
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        btnEdit.setOnClickListener(v -> {
            // TODO: Implement edit functionality
        });

        btnDelete.setOnClickListener(v -> {
            // TODO: Implement delete functionality
        });
    }
} 