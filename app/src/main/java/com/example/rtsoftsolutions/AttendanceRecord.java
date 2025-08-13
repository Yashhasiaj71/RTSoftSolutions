package com.example.rtsoftsolutions;

public class AttendanceRecord {
    public String studentId;
    public String studentName;
    public String courseName;
    public String batchName;
    public String date; // yyyy-MM-dd
    public String status; // Present, Absent, Leave
    public String reason;

    public AttendanceRecord() {}

    public AttendanceRecord(String studentId, String studentName, String courseName, String batchName,
                            String date, String status, String reason) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseName = courseName;
        this.batchName = batchName;
        this.date = date;
        this.status = status;
        this.reason = reason;
    }
}
