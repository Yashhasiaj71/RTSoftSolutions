package com.example.rtsoftsolutions.Models;

import java.util.List;

public class Course {
    private String course_name;
    private List<String> course_batches;
    private String course_subject;
    private String course_students;
    private long course_fees ;

    // 🔹 Empty constructor (required for Firebase)
    public Course() {
    }

    // 🔹 Full constructor
    public Course(String course_name, List<String> course_batches, String course_subject, String course_students , long course_fees) {
        this.course_name = course_name;
        this.course_batches = course_batches;
        this.course_subject = course_subject;
        this.course_students = course_students;
        this.course_fees = course_fees ;
    }

    // 🔹 Getters
    public String getCourse_name() {
        return course_name;
    }

    public List<String> getCourse_batches() {
        return course_batches;
    }

    public String getCourse_subject() {
        return course_subject;
    }

    public String getCourse_students() {
        return course_students;
    }

    public long getCourse_fees() {
        return course_fees ;
    }

    // 🔹 Setters
    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_batches(List<String> course_batches) {
        this.course_batches = course_batches;
    }

    public void setCourse_subject(String course_subject) {
        this.course_subject = course_subject;
    }

    public void setCourse_students(String course_students) {
        this.course_students = course_students;
    }

    public void setCourse_fees(long coursefees) {
       co this.course_fees = coursefees ;
    }
}
