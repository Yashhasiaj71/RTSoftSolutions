package com.example.rtsoftsolutions.Models;

public class Course {
    private String courseId;
    private String courseName;
    private String courseDescription;
    private String instructorName;
    private String duration;
    private String fees;
    private String startDate;
    private String endDate;
    private String maxStudents;
    private String currentStudents;
    private String status; // Active, Inactive, Completed
    private String batchName; // e.g., "Morning Batch", "Evening Batch", "Weekend Batch"
    private String batchTime; // e.g., "9:00 AM - 11:00 AM"
    private String batchDays; // e.g., "Monday, Wednesday, Friday"

    public Course() {
        // Required empty constructor for Firebase
    }

    public Course(String courseId, String courseName, String courseDescription, String instructorName, 
                  String duration, String fees, String startDate, String endDate, 
                  String maxStudents, String currentStudents, String status, String batchName, 
                  String batchTime, String batchDays) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.instructorName = instructorName;
        this.duration = duration;
        this.fees = fees;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
        this.currentStudents = currentStudents;
        this.status = status;
        this.batchName = batchName;
        this.batchTime = batchTime;
        this.batchDays = batchDays;
    }

    public Course( String selectACourse, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11) {

    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getFees() { return fees; }
    public void setFees(String fees) { this.fees = fees; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getMaxStudents() { return maxStudents; }
    public void setMaxStudents(String maxStudents) { this.maxStudents = maxStudents; }

    public String getCurrentStudents() { return currentStudents; }
    public void setCurrentStudents(String currentStudents) { this.currentStudents = currentStudents; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public String getBatchTime() { return batchTime; }
    public void setBatchTime(String batchTime) { this.batchTime = batchTime; }

    public String getBatchDays() { return batchDays; }
    public void setBatchDays(String batchDays) { this.batchDays = batchDays; }
} 