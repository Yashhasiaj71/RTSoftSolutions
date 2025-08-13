package com.example.rtsoftsolutions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> students;
    private OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onStudentClick(Student student, String studentId);
    }

    public StudentAdapter(List<Student> students, OnStudentClickListener listener) {
        this.students = students != null ? students : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student, position);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void updateStudents(List<Student> newStudents) {
        this.students = newStudents != null ? newStudents : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addStudent(Student student) {
        students.add(student);
        notifyItemInserted(students.size() - 1);
    }

    public void removeStudent(int position) {
        if (position >= 0 && position < students.size()) {
            students.remove(position);
            notifyItemRemoved(position);
        }
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameText;
        private TextView studentCourseText;
        private TextView studentBatchText;
        private TextView studentPhoneText;
        private TextView studentFeesText;
        private TextView studentFeesStatusText;
        private ImageButton studentActionButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameText = itemView.findViewById(R.id.StudentNameText);
            studentCourseText = itemView.findViewById(R.id.StudentCourseText);
            studentBatchText = itemView.findViewById(R.id.StudentBatchText);
            studentPhoneText = itemView.findViewById(R.id.StudentPhoneText);
            studentFeesText = itemView.findViewById(R.id.StudentFeesText);
            studentFeesStatusText = itemView.findViewById(R.id.StudentFeesStatusText);
            studentActionButton = itemView.findViewById(R.id.StudentActionButton);
        }

        public void bind(Student student, int position) {
            studentNameText.setText(student.name);
            studentCourseText.setText(student.selectedCourseName);
            studentBatchText.setText(student.selectedBatchName);
            studentPhoneText.setText(student.phoneNo);
            
            // Display fees information
            studentFeesText.setText("₹" + student.remainingFees);
            
            // Set fees status
            if (student.remainingFees == 0) {
                studentFeesStatusText.setText("Paid");
                studentFeesStatusText.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                studentFeesStatusText.setBackgroundResource(R.drawable.status_background);
            } else if (student.remainingFees < student.totalFees) {
                studentFeesStatusText.setText("Partial");
                studentFeesStatusText.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                studentFeesStatusText.setBackgroundResource(R.drawable.status_background);
            } else {
                studentFeesStatusText.setText("Pending");
                studentFeesStatusText.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                studentFeesStatusText.setBackgroundResource(R.drawable.status_background);
            }

            // Set click listeners
            studentActionButton.setOnClickListener(v -> {
                if (listener != null) {
                    // We need to get the student ID from the parent fragment
                    // For now, we'll pass the position as a temporary solution
                    listener.onStudentClick(student, String.valueOf(position));
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStudentClick(student, String.valueOf(position));
                }
            });
        }
    }
}
