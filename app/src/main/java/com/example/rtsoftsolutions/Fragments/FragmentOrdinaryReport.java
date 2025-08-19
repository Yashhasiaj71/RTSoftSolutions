package com.example.rtsoftsolutions.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rtsoftsolutions.Models.AttendanceRecord;
import com.example.rtsoftsolutions.Models.Student;
import com.example.rtsoftsolutions.PdfExporter;
import com.example.rtsoftsolutions.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FragmentOrdinaryReport extends Fragment {

    private Spinner courseSpinner, batchSpinner;
    private DatePicker datePicker;
    private BarChart barChart;
    private TextView tvSummary;
    private Button btnExport, btnApplyDate;

    private FirebaseDatabase database;
    private DatabaseReference attendanceRef;
    private DatabaseReference studentsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ordinary_report , container , false);
        courseSpinner = v.findViewById(R.id.coursespinner);
        batchSpinner = v.findViewById(R.id.batchspinner);
        datePicker = v.findViewById(R.id.reportDatePicker);
        barChart = v.findViewById(R.id.barAttendanceChart);
        tvSummary = v.findViewById(R.id.tvAttendanceSummary);
        btnExport = v.findViewById(R.id.btnExportAttendancePdf);
        btnApplyDate = v.findViewById(R.id.btnApplyDate);

        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");
        attendanceRef = database.getReference("attendance");

        setupFilters();
        btnExport.setOnClickListener(view -> PdfExporter.exportFeesReport(requireContext(), barChart, tvSummary.getText().toString()));
        btnApplyDate.setOnClickListener(view -> {
            // hide date picker and refresh chart for selected date
            datePicker.setVisibility(View.GONE);
            renderChart();
        });
        return v;
    }

    private void setupFilters() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> courses = new HashSet<>();
                Set<String> batches = new HashSet<>();
                courses.add("All Courses");
                batches.add("All Batches");
                for (DataSnapshot s : snapshot.getChildren()) {
                    Student st = s.getValue(Student.class);
                    if (st != null) { courses.add(st.selectedCourseName); batches.add(st.selectedBatchName);} }
                ArrayAdapter<String> ca = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(courses));
                ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setAdapter(ca);
                ArrayAdapter<String> ba = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(batches));
                ba.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                batchSpinner.setAdapter(ba);

                courseSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { renderChart(); }
                    @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
                batchSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { renderChart(); }
                    @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
                renderChart();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void renderChart() {
        final String course = courseSpinner.getSelectedItem() != null ? courseSpinner.getSelectedItem().toString() : "All Courses";
        final String batch = batchSpinner.getSelectedItem() != null ? batchSpinner.getSelectedItem().toString() : "All Batches";
        final String date = currentDateString();

        attendanceRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                int present = 0, absent = 0, leave = 0;
                for (DataSnapshot s : snapshot.getChildren()) {
                    AttendanceRecord r = s.getValue(AttendanceRecord.class);
                    if (r == null) continue;
                    boolean okCourse = course.equals("All Courses") || course.equals(r.courseName);
                    boolean okBatch = batch.equals("All Batches") || batch.equals(r.batchName);
                    if (okCourse && okBatch) {
                        switch (r.status) {
                            case "Present": ++present; break;
                            case "Absent": ++absent; break;
                            case "Leave": ++leave; break;
                        }
                    }
                }
                updateBarChart(present, absent, leave);
                tvSummary.setText("Date: " + date + "  |  Present: " + present + "  Absent: " + absent + "  Leave: " + leave);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private String currentDateString() {
        Calendar c = Calendar.getInstance();
        if (datePicker.getVisibility() == View.VISIBLE) {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            c = selected;
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
    }

    private void updateBarChart(int present, int absent, int leave) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, present));
        entries.add(new BarEntry(1, absent));
        entries.add(new BarEntry(2, leave));
        BarDataSet set = new BarDataSet(entries, "Attendance");
        set.setColors(new int[]{Color.parseColor("#4CAF50"), Color.parseColor("#EF5350"), Color.parseColor("#FFC107")});
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.setFitBars(true);
        Description d = new Description();
        d.setText("Present/Absent/Leave");
        barChart.setDescription(d);
        barChart.invalidate();
    }
}

