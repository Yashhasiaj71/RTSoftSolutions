package com.example.rtsoftsolutions.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rtsoftsolutions.Models.Student;
import com.example.rtsoftsolutions.PdfExporter;
import com.example.rtsoftsolutions.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Fragment_feesReport extends Fragment {

    private Spinner spCourse, spBatch;
    private PieChart pieChart;
    private TextView tvSummary;
    private Button btnExport;

    private FirebaseDatabase database;
    private DatabaseReference studentsRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feesreport , container , false);
        spCourse = v.findViewById(R.id.spFeesCourse);
        spBatch = v.findViewById(R.id.spFeesBatch);
        pieChart = v.findViewById(R.id.pieFeesChart);
        tvSummary = v.findViewById(R.id.tvFeesSummary);
        btnExport = v.findViewById(R.id.btnExportFeesPdf);

        setupFilters();
        btnExport.setOnClickListener(view -> PdfExporter.exportFeesReport(requireContext(), pieChart, tvSummary.getText().toString()));
        return v;
    }

    private void setupFilters() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> courseSet = new HashSet<>();
                Set<String> batchSet = new HashSet<>();
                courseSet.add("All Courses");
                batchSet.add("All Batches");
                for (DataSnapshot s : snapshot.getChildren()) {
                    Student st = s.getValue(Student.class);
                    if (st == null) continue;
                    courseSet.add(st.selectedCourseName);
                    batchSet.add(st.selectedBatchName);
                }
                ArrayAdapter<String> ca = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(courseSet));
                ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCourse.setAdapter(ca);
                ArrayAdapter<String> ba = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(batchSet));
                ba.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spBatch.setAdapter(ba);

                View.OnClickListener reload = v -> renderChart();
                spCourse.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { renderChart(); }
                    @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
                spBatch.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { renderChart(); }
                    @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
                renderChart();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void renderChart() {
        final String course = spCourse.getSelectedItem() != null ? spCourse.getSelectedItem().toString() : "All Courses";
        final String batch = spBatch.getSelectedItem() != null ? spBatch.getSelectedItem().toString() : "All Batches";

        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalFees = 0, paid = 0, remaining = 0;
                for (DataSnapshot s : snapshot.getChildren()) {
                    Student st = s.getValue(Student.class);
                    if (st == null) continue;
                    boolean okCourse = course.equals("All Courses") || course.equals(st.selectedCourseName);
                    boolean okBatch = batch.equals("All Batches") || batch.equals(st.selectedBatchName);
                    if (okCourse && okBatch) {
                        totalFees += st.totalFees;
                        paid += st.paidFees;
                        remaining += st.remainingFees;
                    }
                }
                updatePieChart(paid, remaining);
                tvSummary.setText("Total: ₹" + totalFees + "  |  Paid: ₹" + paid + "  |  Remaining: ₹" + remaining);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updatePieChart(int paid, int remaining) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(paid, "Paid"));
        entries.add(new PieEntry(remaining, "Remaining"));
        PieDataSet set = new PieDataSet(entries, "Fees Status");
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4CAF50"));
        colors.add(Color.parseColor("#EF5350"));
        set.setColors(colors);
        PieData data = new PieData(set);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);
        pieChart.setUsePercentValues(true);
        Description desc = new Description();
        desc.setText("Paid vs Remaining");
        pieChart.setDescription(desc);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
