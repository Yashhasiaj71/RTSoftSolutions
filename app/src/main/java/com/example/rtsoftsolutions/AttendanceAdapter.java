package com.example.rtsoftsolutions;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    public interface OnStatusChangedListener {
        void onChanged(String studentId, String status, String reason);
    }

    private final List<Student> students = new ArrayList<>();
    private final Map<String, AttendanceRecord> recordByStudentId = new HashMap<>();
    private final OnStatusChangedListener listener;

    public AttendanceAdapter(OnStatusChangedListener listener) {
        this.listener = listener;
    }

    public void setStudents(List<Student> list) {
        students.clear();
        if (list != null) students.addAll(list);
        notifyDataSetChanged();
    }

    public List<AttendanceRecord> getRecords(String date) {
        List<AttendanceRecord> result = new ArrayList<>();
        for (Student s : students) {
            AttendanceRecord rec = recordByStudentId.get(s.phoneNo); // temporary key; replace with real id if available
            if (rec == null) {
                rec = new AttendanceRecord(s.phoneNo, s.name, s.selectedCourseName, s.selectedBatchName, date, "Present", "");
            } else {
                rec.date = date;
            }
            result.add(rec);
        }
        return result;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_student, parent, false);
        return new AttendanceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        holder.bind(students.get(position));
    }

    @Override
    public int getItemCount() { return students.size(); }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCourseBatch;
        RadioGroup rgStatus;
        RadioButton rbPresent, rbAbsent, rbLeave;
        EditText etReason;

        AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvCourseBatch = itemView.findViewById(R.id.tvCourseBatch);
            rgStatus = itemView.findViewById(R.id.rgStatus);
            rbPresent = itemView.findViewById(R.id.rbPresent);
            rbAbsent = itemView.findViewById(R.id.rbAbsent);
            rbLeave = itemView.findViewById(R.id.rbLeave);
            etReason = itemView.findViewById(R.id.etReason);
        }

        void bind(Student s) {
            tvName.setText(s.name);
            tvCourseBatch.setText(s.selectedCourseName + " • " + s.selectedBatchName);

            // default present
            rgStatus.setOnCheckedChangeListener(null);
            rbPresent.setChecked(true);
            etReason.setVisibility(View.GONE);
            etReason.setText("");

            rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
                String status = checkedId == R.id.rbPresent ? "Present" : (checkedId == R.id.rbAbsent ? "Absent" : "Leave");
                etReason.setVisibility(("Absent".equals(status) || "Leave".equals(status)) ? View.VISIBLE : View.GONE);
                AttendanceRecord rec = new AttendanceRecord(s.phoneNo, s.name, s.selectedCourseName, s.selectedBatchName, "", status, etReason.getText().toString());
                recordByStudentId.put(s.phoneNo, rec);
                if (listener != null) listener.onChanged(s.phoneNo, status, rec.reason);
            });

            etReason.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s1, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s1, int start, int before, int count) {
                    AttendanceRecord rec = recordByStudentId.get(s.phoneNo);
                    if (rec != null) rec.reason = s1.toString();
                }
                @Override public void afterTextChanged(Editable s1) {}
            });
        }
    }
}
