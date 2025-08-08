package com.example.rtsoftsolutions.Models;

public class Student {
    private String name;
    private String email;
    private String phoneNo;
    private String aadharNo;
    private String address;
    private String fatherName;
    private String motherName;
    private int totalFees;

    // 🔹 Empty constructor (required for Firebase)
    public Student() {
    }

    // 🔹 Full constructor
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

    // 🔹 Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public String getAddress() {
        return address;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public int getTotalFees() {
        return totalFees;
    }

    // 🔹 Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setTotalFees(int totalFees) {
        this.totalFees = totalFees;
    }
}

