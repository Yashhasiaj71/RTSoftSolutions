package com.example.rtsoftsolutions.Models;

public class Batch {
    private String batch_name;
    private String course_name;
    private String start_date;
    private String end_date;
    private String no_of_students;

    // 🔹 Empty constructor (required for Firebase)
    public Batch() {
    }

    // 🔹 Full constructor
    public Batch(String batch_name, String course_name, String start_date, String end_date, String no_of_students) {
        this.batch_name = batch_name;
        this.course_name = course_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.no_of_students = no_of_students;
    }

    // 🔹 Getters
    public String getBatch_name() {
        return batch_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getNo_of_students() {
        return no_of_students;
    }

    // 🔹 Setters
    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setNo_of_students(String no_of_students) {
        this.no_of_students = no_of_students;
    }
}