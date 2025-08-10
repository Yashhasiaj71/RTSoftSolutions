package com.example.rtsoftsolutions.Models;

public class Attendance {
    private String date;
    private String course_name;
    private String batch_name;
    private String student_name;
    private boolean isPresent;

    // 🔹 Empty constructor (required for Firebase)
    public Attendance() {
    }

    // 🔹 Full constructor
    public Attendance(String date, String course_name, String batch_name, String student_name, boolean isPresent) {
        this.date = date;
        this.course_name = course_name;
        this.batch_name = batch_name;
        this.student_name = student_name;
        this.isPresent = isPresent;
    }

    // 🔹 Getters
    public String getDate() {
        return date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public boolean isPresent() {
        return isPresent;
    }

    // 🔹 Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
