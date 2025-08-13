package com.example.rtsoftsolutions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MarkAttendanceFragment extends Fragment {

    private Spinner spCourse, spBatch;
    private DatePicker datePicker;
    private Button btnLoad, btnSave, btnReport;
    private RecyclerView rvStudents;
    private AttendanceAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private DatabaseReference attendanceRef;

    public MarkAttendanceFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.marking_attendance_fragment, container, false);

        spCourse = v.findViewById(R.id.spCourse);
        spBatch = v.findViewById(R.id.spBatch);
        datePicker = v.findViewById(R.id.datePicker);
        btnLoad = v.findViewById(R.id.btnLoad);
        btnSave = v.findViewById(R.id.btnSave);
        btnReport = v.findViewById(R.id.btnReport);
        rvStudents = v.findViewById(R.id.rvStudents);

        adapter = new AttendanceAdapter((id, status, reason) -> {});
        rvStudents.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvStudents.setAdapter(adapter);

        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");
        attendanceRef = database.getReference("attendance");

        loadCourseAndBatchFilters();

        btnLoad.setOnClickListener(view -> loadStudents());
        btnSave.setOnClickListener(view -> saveAttendance());
        // btnReport can navigate to a report fragment (not implemented fully here)

        return v;
    }

    private void loadCourseAndBatchFilters() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> courses = new ArrayList<>();
                List<String> batches = new ArrayList<>();
                courses.add("All Courses");
                batches.add("All Batches");
                for (DataSnapshot s : snapshot.getChildren()) {
                    Student st = s.getValue(Student.class);
                    if (st != null) {
                        if (!courses.contains(st.selectedCourseName)) courses.add(st.selectedCourseName);
                        if (!batches.contains(st.selectedBatchName)) batches.add(st.selectedBatchName);
                    }
                }
                ArrayAdapter<String> ca = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courses);
                ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCourse.setAdapter(ca);
                ArrayAdapter<String> ba = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, batches);
                ba.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spBatch.setAdapter(ba);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadStudents() {
        final String course = spCourse.getSelectedItem().toString();
        final String batch = spBatch.getSelectedItem().toString();
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Student> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Student st = s.getValue(Student.class);
                    if (st == null) continue;
                    boolean okCourse = course.equals("All Courses") || course.equals(st.selectedCourseName);
                    boolean okBatch = batch.equals("All Batches") || batch.equals(st.selectedBatchName);
                    if (okCourse && okBatch) list.add(st);
                }
                adapter.setStudents(list);
                // Hide calendar after loading to keep only list and mark options visible
                if (datePicker != null) datePicker.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Loaded " + list.size() + " students", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveAttendance() {
        String date = currentDateString();
        List<AttendanceRecord> records = adapter.getRecords(date);
        for (AttendanceRecord r : records) {
            // Save under attendance/date/studentId
            attendanceRef.child(date).child(r.studentId).setValue(r);
        }
        Toast.makeText(requireContext(), "Attendance saved for " + records.size() + " students", Toast.LENGTH_SHORT).show();
    }

    private String currentDateString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(c.getTime());
    }
}
