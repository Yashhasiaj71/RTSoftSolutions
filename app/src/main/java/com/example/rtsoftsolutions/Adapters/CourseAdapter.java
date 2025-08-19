package com.example.rtsoftsolutions.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtsoftsolutions.Models.Course;
import com.example.rtsoftsolutions.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    
    private List<Course> courseList;
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CourseAdapter(List<Course> courseList, OnCourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    public void updateCourseList(List<Course> newCourseList) {
        this.courseList = newCourseList;
        notifyDataSetChanged();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCourseName;
        private TextView tvInstructor;
        private TextView tvDuration;
        private TextView tvFees;
        private TextView tvStatus;
        private TextView tvStudents;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvInstructor = itemView.findViewById(R.id.tvInstructor);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvFees = itemView.findViewById(R.id.tvFees);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvStudents = itemView.findViewById(R.id.tvStudents);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCourseClick(courseList.get(position));
                }
            });
        }

        public void bind(Course course) {
            tvCourseName.setText(course.getCourseName());
            tvInstructor.setText("Instructor: " + course.getInstructorName());
            tvDuration.setText("Duration: " + course.getDuration());
            tvFees.setText("Fees: ₹" + course.getFees());
            tvStatus.setText(course.getStatus());
            tvStudents.setText(course.getCurrentStudents() + "/" + course.getMaxStudents() + " Students");
            
            // Set status color
            switch (course.getStatus().toLowerCase()) {
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
        }
    }
} 