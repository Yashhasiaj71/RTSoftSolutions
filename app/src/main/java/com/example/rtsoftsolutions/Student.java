package com.example.rtsoftsolutions;

public class Student {
    public String name;
    public String email;
    public String phoneNo;
    public String aadharNo;
    public String address;
    public String fatherName;
    public String motherName;
    public int totalFees;

    public Student() {} // Needed for Firebase

    public Student(String name, String email, String phoneNo, String aadharNo,
                   String address, String fatherName, String motherName, int totalFees) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.aadharNo = aadharNo;
        this.address = address;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.totalFees = totalFees;
    }
}

